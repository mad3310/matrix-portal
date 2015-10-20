package com.letv.portal.service.app.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.letv.common.paging.impl.Page;
import com.letv.portal.enumeration.MonitorStatus;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.app.AppVersionModel;
import com.letv.portal.model.app.MessageModel;
import com.letv.portal.model.monitor.BaseMonitor;
import com.letv.portal.model.task.service.ITaskChainIndexService;
import com.letv.portal.proxy.IDashBoardProxy;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IUserService;
import com.letv.portal.service.app.IAppService;
import com.letv.portal.service.app.IAppSuggestionService;
import com.letv.portal.service.app.IAppVersionService;
import com.letv.portal.service.app.IH5PageInfoService;
import com.letv.portal.service.app.IMessageService;

@Service("appService")
public class AppServiceImpl implements IAppService{
	private final static Logger logger = LoggerFactory.getLogger(AppServiceImpl.class);
	@Resource
	private IH5PageInfoService h5PageInfoService;
	@Resource
	private IMessageService messageService;
	@Resource
	private IDashBoardProxy dashBoardProxy;
	@Autowired
	private IDbService dbService;
	@Autowired
	private ITaskChainIndexService taskChainIndexService;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private IAppVersionService appVersionService;
	@Autowired
	private IAppSuggestionService appSuggestionService;
	@Resource
	private IContainerService containerService;
	@Autowired
	private IBuildTaskService buildTaskService;
	@Autowired
	private IUserService userService;
	
	@Override
	public Map<String, List<Object>> getHomeInfo() {
		Map<String,Integer> clusterStatus = new HashMap<String, Integer>();
		Map<String,Integer> nodeStatus = new HashMap<String, Integer>();
		Map<String,Integer> dbStatus = new HashMap<String, Integer>();
		CountDownLatch latch = new CountDownLatch(3);
		MonitorInfoThread info1 = new MonitorInfoThread(1l, clusterStatus, latch);
		MonitorInfoThread info2 = new MonitorInfoThread(2l, nodeStatus, latch);
		MonitorInfoThread info3 = new MonitorInfoThread(3l, dbStatus, latch);
		threadPoolTaskExecutor.execute(info1);
		threadPoolTaskExecutor.execute(info2);
		threadPoolTaskExecutor.execute(info3);
		
		Map<String, String> urls = this.h5PageInfoService.getH5Urls();
		
		Map<String, List<Object>> data = new HashMap<String, List<Object>>();
		
		//*********待审核数据库列表start****************
		List<Object> pendingAuditDbInfos = new ArrayList<Object>();
		List<DbModel> dbs = this.dbService.selectPendingAuditDb();
		for (DbModel dbModel : dbs) {
			Map<String, Object> pendingAuditDbInfo = new HashMap<String, Object>();
			pendingAuditDbInfo.put("dbname", dbModel.getDbName());
			pendingAuditDbInfo.put("user", dbModel.getUser().getUserName());
			pendingAuditDbInfo.put("datetime", dbModel.getCreateTime());
			pendingAuditDbInfo.put("url", urls.get("pendingAuditDbUrl").replace("{id}", "?id="+dbModel.getId()));
			pendingAuditDbInfos.add(pendingAuditDbInfo);
		}
		data.put("DBChecker", pendingAuditDbInfos);
		//*********待审核数据库列表end****************
		
		//*********任务流异常start****************
		List<Object> taskInfos = new ArrayList<Object>();
		List<Map<String, Object>> failInfos = this.taskChainIndexService.selectFailedChainIndex();
		for (Map<String, Object> map : failInfos) {
			Map<String, Object> taskInfo = new HashMap<String, Object>();
			taskInfo.put("name", map.get("taskName"));
			taskInfo.put("type", 0);
			taskInfo.put("url", urls.get("taskErrorUrl"));
			taskInfos.add(taskInfo);
		}
		data.put("TaskFault", taskInfos);
		//*********任务流异常end****************
		
		//*********rds监控异常start****************
		try {
			latch.await(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			logger.error("latch.await had error", e1);
		}
		List<Object> rdsInfos = new ArrayList<Object>();
		if(clusterStatus.size()!=0 && nodeStatus.size()!=0 && dbStatus.size()!=0) {
			Map<String, Object> clusterInfo = new HashMap<String, Object>();
			clusterInfo.put("type", 0);
			clusterInfo.put("count", (int)clusterStatus.get("except")+(int)clusterStatus.get("timeout")+
					(int)clusterStatus.get("crash")+(int)clusterStatus.get("serious")+(int)clusterStatus.get("general"));
			clusterInfo.put("url", urls.get("clusterUrl"));
			rdsInfos.add(clusterInfo);
			Map<String, Object> nodeInfo = new HashMap<String, Object>();
			nodeInfo.put("type", 1);
			nodeInfo.put("count", (int)nodeStatus.get("except")+(int)nodeStatus.get("timeout")+
					(int)nodeStatus.get("crash")+(int)clusterStatus.get("serious")+(int)clusterStatus.get("general"));
			nodeInfo.put("url", urls.get("nodeUrl"));
			rdsInfos.add(nodeInfo);
			Map<String, Object> dbInfo = new HashMap<String, Object>();
			dbInfo.put("type", 2);
			dbInfo.put("count", (int)dbStatus.get("except")+(int)dbStatus.get("timeout")+
					(int)dbStatus.get("crash")+(int)clusterStatus.get("serious")+(int)clusterStatus.get("general"));
			dbInfo.put("url", urls.get("dbUrl"));
			rdsInfos.add(dbInfo);
		}
			
		data.put("RDSFault", rdsInfos);
		//*********rds监控异常end****************
		
		return data;
	}
	
	private class MonitorInfoThread extends Thread {
		private Map<String, Integer> result;
		private Long type;
		private CountDownLatch latch;
		
		public MonitorInfoThread(long type, Map<String,Integer> result, CountDownLatch latch) {
			this.type = type;
			this.result = result;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			try {
				Map<String, Integer> ret = dashBoardProxy.selectMonitorAlertWithMultiThread(type);
				result.putAll(ret);
			} catch (Exception e) {
				result.putAll(new HashMap<String,Integer>());
			} finally {
				latch.countDown();
			}
		}
	}

	@Override
	public Map<String, Object> checkUpdate(Map<String, Object> params) {
		AppVersionModel version = this.appVersionService.getLastestVersionInfo(params);
		if(version==null) {
			logger.error("appVersionService.getLastestVersionInfo have no result, params is :"+params.toString());
			return null;
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("verCode", version.getVersionCode());
		ret.put("verName", version.getVersionName());
		ret.put("forceUpdate", version.getForceUpdate()==0?"false":"true");
		ret.put("url", version.getUrl());
		ret.put("desc", version.getDescn());
		return ret;
	}

	@Override
	public void reportSuggestion(Map<String, Object> params) {
		this.appSuggestionService.reportSuggestion(params);
	}

	@Override
	public List<ContainerModel> failedList(Map<String, Object> params, long type) {
		List<ContainerModel> containers = this.containerService.selectVaildVipContainers(params);
		CountDownLatch latch = new CountDownLatch(containers==null?0:containers.size());
		List<ContainerModel> ret = new CopyOnWriteArrayList<ContainerModel>();
		for (ContainerModel containerModel : containers) {
			MonitorDataThread data = new MonitorDataThread(type, containerModel, ret, latch);
			threadPoolTaskExecutor.execute(data);
		}
		try {
			latch.await(3, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.error("latch.await had error", e);
		}
		return ret;
	}
	
	private class MonitorDataThread extends Thread {
		private Long type;
		private List<ContainerModel> ret;
		private ContainerModel containerModel;
		private CountDownLatch latch;
		
		public MonitorDataThread(long type, ContainerModel containerModel, List<ContainerModel> ret, CountDownLatch latch) {
			this.type = type;
			this.ret = ret;
			this.containerModel = containerModel;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			try {
				BaseMonitor baseMonitor = buildTaskService.getMonitorData(containerModel.getIpAddr(), type);
				if(baseMonitor!=null && baseMonitor.getResult()!=0) {
					containerModel.setStatus(baseMonitor.getResult());
					ret.add(containerModel);
				}
			} catch (Exception e) {
				containerModel.setStatus(MonitorStatus.CRASH.getValue());
				ret.add(containerModel);
			} finally {
				latch.countDown();
			}
		}
	}

	@Override
	public Page getRdsFault(Page page) {
		Map<String,Integer> clusterStatus = new HashMap<String, Integer>();
		Map<String,Integer> nodeStatus = new HashMap<String, Integer>();
		Map<String,Integer> dbStatus = new HashMap<String, Integer>();
		CountDownLatch latch = new CountDownLatch(3);
		MonitorInfoThread info1 = new MonitorInfoThread(1l, clusterStatus, latch);
		MonitorInfoThread info2 = new MonitorInfoThread(2l, nodeStatus, latch);
		MonitorInfoThread info3 = new MonitorInfoThread(3l, dbStatus, latch);
		threadPoolTaskExecutor.execute(info1);
		threadPoolTaskExecutor.execute(info2);
		threadPoolTaskExecutor.execute(info3);
		
		Map<String, String> urls = this.h5PageInfoService.getH5Urls();
		
		Map<String, List<Object>> data = new HashMap<String, List<Object>>();
		
		List<Object> rdsInfos = new ArrayList<Object>();
		
		try {
			latch.await(5, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			logger.error("latch.await had error", e1);
		}
		if(clusterStatus.size()!=0 && nodeStatus.size()!=0 && dbStatus.size()!=0) {
			Map<String, Object> clusterInfo = new HashMap<String, Object>();
			clusterInfo.put("type", 0);
			clusterInfo.put("count", clusterStatus.get("except")+clusterStatus.get("timeout")+
					clusterStatus.get("crash")+clusterStatus.get("serious")+clusterStatus.get("general"));
			clusterInfo.put("url", urls.get("clusterUrl"));
			rdsInfos.add(clusterInfo);
			logger.debug("clusterStatus:"+clusterStatus.toString());
			Map<String, Object> nodeInfo = new HashMap<String, Object>();
			nodeInfo.put("type", 1);
			nodeInfo.put("count", nodeStatus.get("except")+nodeStatus.get("timeout")+
					nodeStatus.get("crash")+nodeStatus.get("serious")+nodeStatus.get("general"));
			nodeInfo.put("url", urls.get("nodeUrl"));
			rdsInfos.add(nodeInfo);
			logger.debug("nodeStatus:"+nodeStatus.toString());
			Map<String, Object> dbInfo = new HashMap<String, Object>();
			dbInfo.put("type", 2);
			dbInfo.put("count", dbStatus.get("except")+dbStatus.get("timeout")+
					dbStatus.get("crash")+dbStatus.get("serious")+dbStatus.get("general"));
			dbInfo.put("url", urls.get("dbUrl"));
			rdsInfos.add(dbInfo);
			logger.debug("dbStatus:"+dbStatus.toString());
		}
		data.put("RDSFault", rdsInfos);
		page.setTotalRecords(rdsInfos.size());
		page.setData(data);
		return page;
	}

	@Override
	public Page getPendingAuditDb(Page page, Map<String, Object> params) {
		Map<String, String> urls = this.h5PageInfoService.getH5Urls();
		
		Map<String, List<Object>> data = new HashMap<String, List<Object>>();
		
		List<Object> pendingAuditDbInfos = new ArrayList<Object>();
		params.put("status", 0);
		Page p = this.dbService.getPagePendingAuditDb(page, params);
		List<DbModel> dbs = (List<DbModel>) p.getData();
		for (DbModel dbModel : dbs) {
			Map<String, Object> pendingAuditDbInfo = new HashMap<String, Object>();
			pendingAuditDbInfo.put("dbname", dbModel.getDbName());
			pendingAuditDbInfo.put("user", dbModel.getUser().getUserName());
			pendingAuditDbInfo.put("datetime", dbModel.getCreateTime());
			pendingAuditDbInfo.put("url", urls.get("pendingAuditDbUrl")==null?"":urls.get("pendingAuditDbUrl").replace("{id}", "?id="+dbModel.getId()));
			pendingAuditDbInfos.add(pendingAuditDbInfo);
		}
		data.put("DBChecker", pendingAuditDbInfos);
		p.setData(data);
		return p;
	}

	@Override
	public Page getMessages(Page page, Map<String, Object> params) {
		Map<String, String> urls = this.h5PageInfoService.getH5Urls();
		Page p = this.messageService.selectPageByParams(page, params);
		
		List<Object> retMessages = new ArrayList<Object>();
		List<MessageModel> messages = (List<MessageModel>) p.getData();
		for (MessageModel message : messages) {
			Map<String, Object> retMessage = new HashMap<String, Object>();
			retMessage.put("title", message.getTitle());
			retMessage.put("describe", message.getDescn());
			retMessage.put("datetime", message.getCreateTime());
			retMessage.put("url", urls.get("messageUrl")==null?"":urls.get("messageUrl").replace("{id}", "?id="+message.getId()));
			retMessages.add(retMessage);
		}
		p.setData(retMessages);
		return p;
	}

	@Override
	public void editUserInfo(Map<String, Object> params) {
		this.userService.updateByMap(params);
	}

}
