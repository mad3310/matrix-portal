package com.letv.portal.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.letv.common.dao.QueryParam;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IDbApplyStandardDao;
import com.letv.portal.dao.IDbDao;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IContainerService;
import com.mysql.jdbc.StringUtils;

@Service("containerService")
public class ContainerServiceImpl extends BaseServiceImpl<ContainerModel> implements
		IContainerService{
	
	@Resource
	private IContainerDao containerDao;
	@Resource
	private IDbDao dbDao;

	public ContainerServiceImpl() {
		super(ContainerModel.class);
	}

	@Override
	public IBaseDao<ContainerModel> getDao() {
		return this.containerDao;
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.containerDao.selectPageByMap(param));
		page.setTotalRecords(this.containerDao.selectByMapCount(params));
		return page;
		
	}
	
	@Override
	public void insert(ContainerModel t) {
		
//		if(StringUtils.isNullOrEmpty(toTest))
		
		super.insert(t);
	}

	@Override
	public List<ContainerModel> selectByClusterId(String clusterId) {
		return this.containerDao.selectByClusterId(clusterId);
	}

	@Override
	public List<ContainerModel> selectNormalByClusterId(String clusterId) {
		// TODO Auto-generated method stub
		return null;
	}
}
