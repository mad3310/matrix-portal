package com.letv.portal.proxy.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.ConfigUtil;
import com.letv.portal.enumeration.DbStatus;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.proxy.IDbProxy;
import com.letv.portal.proxy.IMclusterProxy;
import com.letv.portal.python.service.IBuildTaskService;
import com.letv.portal.service.IBaseService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;
import com.mysql.jdbc.StringUtils;

@Component
public class DbProxyImpl extends BaseProxyImpl<DbModel> implements
		IDbProxy{
	
	private final static Logger logger = LoggerFactory.getLogger(DbProxyImpl.class);
	
	@Autowired
	private IDbService dbService;
	@Autowired
	private IContainerService containerService;
	@Autowired
	private IMclusterProxy mclusterProxy;
	@Autowired
	private IBuildTaskService buildTaskService;
	
	@Autowired(required=false)
	private SessionServiceImpl sessionService;
	
	@Value("${error.email.to}")
	private String ERROR_MAIL_ADDRESS;
	
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	@Override
	public IBaseService<DbModel> getService() {
		return dbService;
	}
	public DbModel dbList(Long dbId){
		DbModel db = this.dbService.selectById(dbId);
		db.setContainers(this.containerService.selectByMclusterId(db.getMclusterId()));
		return db;
	}
	@Override
	public void auditAndBuild(Map<String, Object> params) {
		
		Integer status = (Integer) params.get("status");
		Long mclusterId = (Long) params.get("mclusterId");
		Long dbId = (Long) params.get("dbId");
		String mclusterName = (String) params.get("mclusterName");		
		String auditInfo = (String) params.get("auditInfo");
		Long hclusterId = (Long) params.get("hclusterId");
		
		DbModel dbModel = new DbModel();
		dbModel.setId(dbId);
		dbModel.setStatus(status);
		
		MclusterModel mcluster = new MclusterModel();
		
		if(DbStatus.BUILDDING.getValue().equals(status)) {//审核成功
			//判断mclsuterId是否为空
			if(mclusterId == null) { //创建新的mcluster集群
				mcluster.setMclusterName(mclusterName);
				mcluster.setHclusterId(hclusterId == null?this.dbService.selectById(dbId).getHclusterId():hclusterId);
				this.mclusterProxy.insert(mcluster);
				dbModel.setMclusterId(mcluster.getId());
				this.dbService.updateBySelective(dbModel);
				this.buildTaskService.buildMcluster(mcluster,dbId);
			} else {
				dbModel.setMclusterId(mclusterId);
				this.dbService.updateBySelective(dbModel);
				this.buildTaskService.buildDb(dbId);
			}
		} else { //审核失败
			dbModel.setAuditInfo(auditInfo);
			this.dbService.updateBySelective(dbModel);
		}
			
		
	}
	@Override
	public void saveAndBuild(DbModel dbModel) {
		Long userId = sessionService.getSession().getUserId();
		dbModel.setCreateUser(userId);
		dbModel.setStatus(DbStatus.DEFAULT.getValue());
		dbModel.setDeleted(true);
		this.dbService.insert(dbModel);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("createUser", userId);
		
		List<DbModel> list = this.dbService.selectByMap(map);
		if(list.size() <= ConfigUtil.getint("db.auto.build.count")) {
			//创建mcluster集群
			Map<String,Object> params = new HashMap<String,Object>();
			
			params.put("dbId", dbModel.getId());
			params.put("mclusterName", userId + "_" + dbModel.getDbName());
			params.put("status", DbStatus.BUILDDING.getValue());
			params.put("hclusterId", dbModel.getHclusterId());
			
			this.auditAndBuild(params);
		} else {
			//邮件通知
			Map<String,Object> emailParams = new HashMap<String,Object>();
			//用户${createUser}于${createTime}申请数据库${dbName}，
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			emailParams.put("createUser", sessionService.getSession().getUserName());
			emailParams.put("createTime", sdf.format(new Date()));
			emailParams.put("dbName", dbModel.getDbName());
			MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",ERROR_MAIL_ADDRESS,"乐视云平台web-portal系统通知","createDb.ftl",emailParams);
			try {
				defaultEmailSender.sendMessage(mailMessage);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
	@Override
	public DbModel monitor4conn(Long dbId, Long chartId, Long strategy) {
		// TODO Auto-generated method stub
		Long mclusterId = this.dbService.selectById(dbId).getMclusterId();
		
		return null;
	}
	
}
