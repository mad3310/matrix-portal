package com.letv.portal.proxy.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.result.ApiResultObject;
import com.letv.portal.enumeration.BackupStatus;
import com.letv.portal.enumeration.DbStatus;
import com.letv.portal.model.BackupResultModel;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.proxy.IBackupProxy;
import com.letv.portal.python.service.IPythonService;
import com.letv.portal.service.IBackupService;
import com.letv.portal.service.IBaseService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IHclusterService;
import com.letv.portal.service.IMclusterService;
import com.mysql.jdbc.StringUtils;


@Component("backupProxy")
public class BackupProxyImpl extends BaseProxyImpl<BackupResultModel> implements IBackupProxy{
	
	private final static Logger logger = LoggerFactory.getLogger(BackupProxyImpl.class);
	
	@Autowired
	private IBackupService backupService;
	@Autowired
	private IMclusterService mclusterService;
	@Autowired
	private IContainerService containerService;
	@Autowired
	private IHclusterService hclusterService;
	@Autowired
	private IDbService dbService;
	@Autowired
	private IPythonService pythonService;
	@Value("${service.notice.email.to}")
	private String SERVICE_NOTICE_MAIL_ADDRESS;
	@Value("${python.db.backup.interval.time}")
	private long DB_BACKUP_INTERVAL_TIME;
	@Value("${default.backup.ignore}")
	private String DEFAULT_BACKUP_IGNORE;
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	@Autowired
	private SchedulingTaskExecutor threadPoolTaskExecutor;

	@Override
	public IBaseService<BackupResultModel> getService() {
		return backupService;
	}
	
	@Override
	public void backupTask(final int count) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("type", "rds");
		List<HclusterModel> hclusters = this.hclusterService.selectByMap(params);
		
		for (final HclusterModel hcluster : hclusters) {
			this.threadPoolTaskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					backupByHcluster(count,hcluster);
				}
			});
		}
	}
	
	private void backupByHcluster(int count,HclusterModel hcluster) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("hclusterId", hcluster.getId());
		List<MclusterModel> mclusters = this.mclusterService.selectValidMclusters(count,params);
		
		while(mclusters != null && !mclusters.isEmpty()) {
			for (MclusterModel mclusterModel : mclusters) {
				//进行备份。
				this.wholeBackup4Db(mclusterModel);
			}
			try {
				Thread.sleep(DB_BACKUP_INTERVAL_TIME);
			} catch (InterruptedException e) {
			}
			mclusters = this.addBackupMcluster(null,hcluster.getId(), count);
		}
	}
	
	private List<MclusterModel> addBackupMcluster(Long mclusterId,Long hclusterId,int addNewCount) {
		Map<String, Object> params = new HashMap<String,Object>();
		params.clear();
		params.put("hclusterId", hclusterId);
		BackupResultModel recentBackup = this.selectRecentBackup(params);
		if(recentBackup == null)
			return null;
		return  this.mclusterService.selectNextValidMclusterById(null == mclusterId?recentBackup.getMclusterId():mclusterId, hclusterId,addNewCount);
	}

	@Override
	public void wholeBackup4Db(MclusterModel mcluster) {
		Date date = new Date();
		if(mcluster == null)
			return;
		ContainerModel container = this.selectValidVipContianer(mcluster.getId(), "mclustervip");
		List<DbModel> dbModels = this.dbService.selectDbByMclusterId(mcluster.getId());
		if(container == null || dbModels.isEmpty())  {
			//发送告知邮件，数据有问题。
			return;
		}
		
		BackupResultModel backup = this.wholeBackup4Db(mcluster,container);
		
		for (DbModel dbModel : dbModels) {
			//将备份记录写入数据库。
			backup.setMclusterId(mcluster.getId());
			backup.setHclusterId(mcluster.getHclusterId());
			backup.setDbId(dbModel.getId());
			backup.setBackupIp(container.getIpAddr());
			backup.setStartTime(date);
			super.insert(backup);
		}
	}
	
	private BackupResultModel wholeBackup4Db(MclusterModel mcluster,ContainerModel container){
		BackupResultModel backupResult = new BackupResultModel();
		ApiResultObject result = this.pythonService.wholeBackup4Db(container.getIpAddr(),mcluster.getAdminUser(),mcluster.getAdminPassword());
		String resultMessage = result.getResult();
		if(DEFAULT_BACKUP_IGNORE.contains(mcluster.getMclusterName())) {
			backupResult.setStatus(BackupStatus.FAILD);
			backupResult.setResultDetail("Ignore the backup for current mcluster.");
			return backupResult;
		}
		if(StringUtils.isNullOrEmpty(resultMessage)) {
			backupResult.setStatus(BackupStatus.FAILD);
			backupResult.setResultDetail("backup api result is null:" + result.getUrl());
		} else if(resultMessage.contains("\"code\": 200")) {
			backupResult.setStatus(BackupStatus.BUILDING);
		} else {
			backupResult.setStatus(BackupStatus.FAILD);
			backupResult.setResultDetail(resultMessage + ":" + result.getUrl());
		}
		return backupResult;
	}
	
	private ContainerModel selectValidVipContianer(Long mclusterId,String type){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mclusterId", mclusterId);
		map.put("type", type);
		List<ContainerModel> containers = this.containerService.selectAllByMap(map);
		if(containers.isEmpty()) {
			return null;
		}
		return containers.get(0);
	}

	@Override
	public void checkBackupStatus(BackupResultModel backup) {
		ApiResultObject result = this.pythonService.checkBackup4Db(backup.getBackupIp());
		backup = analysisBackupResult(backup, result);
		
		Date date = new Date();
		backup.setEndTime(date);
		this.backupService.updateBySelective(backup);
		
		if(BackupStatus.FAILD.equals(backup.getStatus())) {
			logger.info("check backup faild");
			//发送邮件通知
			sendBackupFaildNotice(backup.getDb().getDbName(),backup.getMcluster().getMclusterName(),backup.getResultDetail(),backup.getStartTime(),backup.getBackupIp());
		}
	}
	
	private BackupResultModel analysisBackupResult(BackupResultModel backup,ApiResultObject resultObject) {
		String result = resultObject.getResult();
		if(StringUtils.isNullOrEmpty(result)) {
			backup.setStatus( BackupStatus.FAILD);
			backup.setResultDetail("Connection refused:" + resultObject.getUrl());
			return backup;
		}
		if(result.contains("\"code\": 200") && result.contains("backup success")) {
			backup.setStatus( BackupStatus.SUCCESS);
			backup.setResultDetail("backup success");
			return backup;
		}
		if(result.contains("\"code\": 200") && result.contains("processing")) {
			backup.setStatus( BackupStatus.BUILDING);
			backup.setResultDetail("backup is processing");
			
			int hours = new Date().getHours();
			int startHours = backup.getStartTime().getHours();
			if(hours-startHours>1 || hours-startHours<0) {
				backup.setStatus( BackupStatus.ABNORMAL);
				backup.setResultDetail("more than one hour for bakcup");
			}
			return backup;
		}
		if(result.contains("\"code\": 200") && result.contains("expired")) {
			backup.setStatus( BackupStatus.FAILD);
			backup.setResultDetail("backup expired:" + resultObject.getUrl());
			return backup;
		}
		if(result.contains("\"code\": 411")) {
			backup.setStatus( BackupStatus.FAILD);
			backup.setResultDetail(result.substring(result.indexOf("\"errorDetail\": \"")+1, result.lastIndexOf("\"},")) + resultObject.getUrl());
			return backup;
		} 
		backup.setStatus( BackupStatus.FAILD);
		backup.setResultDetail("api not found:" + resultObject.getUrl());

		return backup;
	}
	
	
	private BackupResultModel selectRecentBackup(Map<String,Object> params) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar curDate = Calendar.getInstance();
		curDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH),curDate.get(Calendar.DATE), 0, 0, 0);
		params.put("startTime", format.format(new Date(curDate.getTimeInMillis())));
		List<BackupResultModel> results = this.backupService.selectByStatusAndDateOrderByMclusterId(params);
		if(results.isEmpty())
			return null;
		return results.get(0);
	}

	private void sendBackupFaildNotice(String dbName,String mclusterName,String resultDetail,Date startTime,String backupIp) {
		logger.info("check backup faild:send email--" + dbName + mclusterName);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dbName", dbName);
		params.put("mclusterName", mclusterName);
		params.put("resultDetail", resultDetail);
		params.put("startTime", format.format(startTime));
		params.put("backupIp", backupIp);
		
		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",SERVICE_NOTICE_MAIL_ADDRESS,"乐视云平台web-portal系统报警通知","backupFaildNotice.ftl",params);
     	defaultEmailSender.sendMessage(mailMessage);
	}
	
	@Override
	@Async
	public void deleteOutData() {
		Map<String,Object> map = new  HashMap<String,Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -15);    //得到前15天
		long date = cal.getTimeInMillis();
		Date monthAgo = new Date(date);
		map.put("startTime", monthAgo);
		
		List<Map<String,Object>> ids = this.backupService.selectExtremeIdByMonitorDate(map);
		if(ids.isEmpty() || ids.get(0) == null || ids.get(0).isEmpty()) {
			return;
		}
		Map<String, Object> extremeIds = ids.get(0);
		Long max = (Long)extremeIds.get("maxId");
		Long min = (Long)extremeIds.get("minId");
		if(max == null || max == 0 || max == min)
			return;
		Long j = min;
		for (Long i = min; i <= max; i+=100) {
			j = i-100;
			map.put("min", j);
			map.put("max", i);
			this.backupService.deleteOutDataByIndex(map);
		}
		map.put("min", max-100);
		map.put("max", max);
		this.backupService.deleteOutDataByIndex(map);
	}

	@Override
	public void backupTaskReport() {
		Map<String, Object> params = new HashMap<String,Object>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar curDate = Calendar.getInstance();
		curDate = new GregorianCalendar(curDate.get(Calendar.YEAR), curDate.get(Calendar.MONTH),curDate.get(Calendar.DATE), 0, 0, 0);
		params.put("startTime", format.format(new Date(curDate.getTimeInMillis())));
		List<BackupResultModel> backupResults = this.selectByMap(params);
		
		for (BackupResultModel backupResultModel : backupResults) {
			this.checkBackupStatus(backupResultModel);
		}
		backupResults = this.selectByMap(params);
		
		int mclusters = this.mclusterService.selectValidMclusterCount();
		int dbs = this.dbService.selectCountByStatus(DbStatus.NORMAL.getValue());
		int totalDb = backupResults.size();
		int successDb = 0;
		int failedDb = 0;
		int buildingDb = 0;
		int abNormalDb = 0;
		for (BackupResultModel backupResultModel : backupResults) {
			if(BackupStatus.SUCCESS.equals(backupResultModel.getStatus())) {
				successDb++;
				continue;
			}
			if(BackupStatus.FAILD.equals(backupResultModel.getStatus())) {
				failedDb++;
				continue;
			}
			if(BackupStatus.BUILDING.equals(backupResultModel.getStatus())) {
				buildingDb++;
				continue;
			}
			if(BackupStatus.ABNORMAL.equals(backupResultModel.getStatus())) {
				abNormalDb++;
				continue;
			}
		}
		
		backupResults = this.backupService.selectByMapGroupByMcluster(params);
		int totalCluster = backupResults.size();
		int successCluster = 0;
		int failedCluster = 0;
		int buildingCluster = 0;
		int abNormalCluster = 0;
		for (BackupResultModel backupResultModel : backupResults) {
			if(BackupStatus.SUCCESS.equals(backupResultModel.getStatus())) {
				successCluster++;
				continue;
			}
			if(BackupStatus.FAILD.equals(backupResultModel.getStatus())) {
				failedCluster++;
				continue;
			}
			if(BackupStatus.BUILDING.equals(backupResultModel.getStatus())) {
				buildingCluster++;
				continue;
			}
			if(BackupStatus.ABNORMAL.equals(backupResultModel.getStatus())) {
				abNormalCluster++;
				continue;
			}
		}
		
		params.clear();
		params.put("dbCount", dbs);
		params.put("dbBackupCount", totalDb);
		params.put("successDb", successDb);
		params.put("failedDb", failedDb);
		params.put("buildingDb", buildingDb);
		params.put("abNormalDb", abNormalDb);
		
		params.put("clusterCount", mclusters);
		params.put("clusterBackupCount", totalCluster);
		params.put("successCluster", successCluster);
		params.put("failedCluster", failedCluster);
		params.put("buildingCluster", buildingCluster);
		params.put("abNormalCluster", abNormalCluster);
		
		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",SERVICE_NOTICE_MAIL_ADDRESS,"乐视云平台web-portal系统备份结果通知","dbBackupReport.ftl",params);
		mailMessage.setHtml(true);
		defaultEmailSender.sendMessage(mailMessage);
	}

}
