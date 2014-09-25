package com.letv.portal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.letv.common.dao.QueryParam;
import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IDbApplyStandardDao;
import com.letv.portal.dao.IDbDao;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IDbApplyStandardService;

@Service("dbApplyStandardService")
public class DbApplyStandardServiceImpl extends BaseServiceImpl<DbApplyStandardModel> implements
		IDbApplyStandardService{
	
	private final static Logger logger = LoggerFactory.getLogger(DbApplyStandardServiceImpl.class);
	
	@Resource
	private IDbApplyStandardDao dbApplyStandardDao;
	@Resource
	private IDbDao dbDao;

	@Value("${error.email.to}")
	private String ERROR_MAIL_ADDRESS;
	
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	public DbApplyStandardServiceImpl() {
		super(DbApplyStandardModel.class);
	}

	@Override
	public IBaseDao<DbApplyStandardModel> getDao() {
		return this.dbApplyStandardDao;
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.dbApplyStandardDao.selectPageByMap(param));
		page.setTotalRecords(this.dbApplyStandardDao.selectByMapCount(params));
		return page;
		
	}
	
	@Override
	public void insert(DbApplyStandardModel t) {
		
		String uuid = UUID.randomUUID().toString();
		DbModel dbModel = new DbModel();
		dbModel.setDbName(t.getApplyCode());
		dbModel.setClusterId(t.getClusterId());
		dbModel.setCreateUser(t.getCreateUser());
		dbModel.setId(uuid);
		this.dbDao.insert(dbModel);
		
		t.setBelongDb(uuid);
		super.insert(t);
		
		//邮件通知
		Map<String,Object> map = new HashMap<String,Object>();
		//用户${createUser}于${createTime}申请数据库${dbName}，
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("createUser", t.getCreateUser());
		map.put("createTime", sdf.format(new Date()));
		map.put("dbName", t.getApplyCode());
		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",ERROR_MAIL_ADDRESS,"乐视云平台web-portal系统通知","createDb.ftl",map);
		try {
			defaultEmailSender.sendMessage(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@Override
	public DbApplyStandardModel selectByDbId(String belongDb) {
		return this.dbApplyStandardDao.selectByDbId(belongDb);
	}
	
}
