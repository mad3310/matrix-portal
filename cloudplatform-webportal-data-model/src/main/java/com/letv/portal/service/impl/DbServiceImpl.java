package com.letv.portal.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.QueryParam;
import com.letv.common.email.SimpleTextEmailSender;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IDbApplyStandardDao;
import com.letv.portal.dao.IDbDao;
import com.letv.portal.dao.IIpResourceDao;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IMclusterService;

@Service("dbService")
public class DbServiceImpl extends BaseServiceImpl<DbModel> implements
		IDbService{
	
	private final static Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);
	
	@Resource
	private IDbDao dbDao;
	@Resource
	private IDbApplyStandardDao dbApplyStandardDao;
	
	@Resource
	private IContainerDao containerDao;
	
	@Resource
	private IMclusterService mclusterService;
	
	@Resource
	private IIpResourceDao ipResourceDao;
	@Resource
	private SimpleTextEmailSender simpleTextEmailSender;

	public DbServiceImpl() {
		super(DbModel.class);
	}

	@Override
	public IBaseDao<DbModel> getDao() {
		return this.dbDao;
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.dbDao.selectPageByMap(param));
		page.setTotalRecords(this.dbDao.selectByMapCount(params));
		return page;
		
	}

	@Override
	public void audit(String dbId,String dbApplyStandardId,String status,String mclusterId,String auditInfo) {
		this.dbDao.audit(new DbModel(dbId,status,mclusterId,auditInfo));
		this.dbApplyStandardDao.audit(new DbApplyStandardModel(dbApplyStandardId,status,auditInfo));
	}

	@Override
	public void buildNotice(String dbId,String buildFlag) {
		/*buildFlag = "success".equals(buildFlag)?Constant.DB_AUDIT_STATUS_BUILD_SUCCESS:Constant.DB_AUDIT_STATUS_BUILD_FAIL;
		DbModel dbModel = this.selectById(dbId);
		if(Constant.DB_AUDIT_STATUS_TRUE_BUILD_NEW_MCLUSTER.equals(dbModel.getStatus())) {
			this.mclusterService.buildNotice(dbModel.getClusterId(),Constant.DB_AUDIT_STATUS_BUILD_SUCCESS);
		}
		this.dbDao.audit(new DbModel(dbId,Constant.));*/
	}

	@Override
	public Map<String, String> selectCreateParams(String id) {
		return this.dbDao.selectCreateParams(id);
	}

	@Override
	public List<DbModel> selectByDbName(String dbName) {
		return this.dbDao.selectByDbName(dbName);
	}

}
