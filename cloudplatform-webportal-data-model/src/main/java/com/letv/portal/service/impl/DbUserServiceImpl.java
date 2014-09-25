package com.letv.portal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.letv.portal.constant.Constant;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IDbUserDao;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.service.IDbUserService;


@Service("dbUserService")
public class DbUserServiceImpl extends BaseServiceImpl<DbUserModel> implements
		IDbUserService{
	
	private final static Logger logger = LoggerFactory.getLogger(DbUserServiceImpl.class);
	
	@Resource
	private IDbUserDao dbUserDao;
	
	@Value("${error.email.to}")
	private String ERROR_MAIL_ADDRESS;
	
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	public DbUserServiceImpl() {
		super(DbUserModel.class);
	}
	
	@Override
	public IBaseDao<DbUserModel> getDao() {
		return dbUserDao;
	}
	
	@Override
	public List<DbUserModel> selectByDbId(String dbId) {
		return this.dbUserDao.selectByDbId(dbId);
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.dbUserDao.selectPageByMap(param));
		page.setTotalRecords(this.dbUserDao.selectByMapCount(params));
		return page;
	}

	@Override
	public Map<String, String> selectCreateParams(String id) {
		return this.dbUserDao.selectCreateParams(id);
	}

	@Override
	public void updateStatus(DbUserModel dbUserModel) {
		this.dbUserDao.updateStatus(dbUserModel);
	}
	
	@Override
	public List<DbUserModel> selectByIpAndUsername(DbUserModel dbUserModel){
		return this.dbUserDao.selectByIpAndUsername(dbUserModel);
	}
	
	@Override
	public void insert(DbUserModel dbUserModel){
		
		
		//计算相关参数
		int maxQueriesPerHour = 1000;   //每小时最大查询数(用户可填,系统自动填充,管理员审核修改)
		int maxUpdatesPerHour=1000;   //每小时最大更新数(用户可填,系统自动填充,管理员审核修改)
		int maxConnectionsPerHour=100;   //每小时最大连接数(用户可填,系统自动填充,管理员审核修改)
		int maxUserConnections=10;   //用户最大链接数(用户可填,系统自动填充,管理员审核修改)
		
		int maxConcurrency = dbUserModel.getMaxConcurrency();
		
		if(!Constant.DB_USER_TYPE_MANAGER.equals(dbUserModel.getType())) {
			maxUserConnections = maxConcurrency;
			maxConnectionsPerHour = maxConcurrency*2*60*60;
			maxQueriesPerHour = maxConcurrency*2*60*60;
			maxUpdatesPerHour = maxConcurrency*60*60;
		} 
		dbUserModel.setMaxUserConnections(maxUserConnections);
		dbUserModel.setMaxConnectionsPerHour(maxConnectionsPerHour);
		dbUserModel.setMaxQueriesPerHour(maxQueriesPerHour);
		dbUserModel.setMaxUpdatesPerHour(maxUpdatesPerHour);
		
		super.insert(dbUserModel);
		//邮件通知
		Map<String,Object> map = new HashMap<String,Object>();
		//用户${createUser}于${createTime}申请数据库${dbName}，
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("createUser", dbUserModel.getCreateUser());
		map.put("createTime", sdf.format(new Date()));
		map.put("dbUserName", dbUserModel.getUsername());
		MailMessage mailMessage = new MailMessage("乐视云平台web-portal系统",ERROR_MAIL_ADDRESS,"乐视云平台web-portal系统通知","createDbUser.ftl",map);
		
		try {
			defaultEmailSender.sendMessage(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
}
