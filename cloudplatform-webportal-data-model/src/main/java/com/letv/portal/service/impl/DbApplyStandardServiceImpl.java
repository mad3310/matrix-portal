package com.letv.portal.service.impl;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.letv.common.dao.QueryParam;
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
	
	@Resource
	private IDbApplyStandardDao dbApplyStandardDao;
	@Resource
	private IDbDao dbDao;

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
		dbModel.setDbName(t.getApplyName());
		dbModel.setClusterId(t.getClusterId());
		dbModel.setCreateUser(t.getCreateUser());
		dbModel.setId(uuid);
		this.dbDao.insert(dbModel);
		
		t.setBelongDb(uuid);
		super.insert(t);
	}

	@Override
	public DbApplyStandardModel selectByDbId(String belongDb) {
		return this.dbApplyStandardDao.selectByDbId(belongDb);
	}
	
}
