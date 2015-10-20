package com.letv.portal.service.app.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.app.IAppVersionDao;
import com.letv.portal.model.app.AppVersionModel;
import com.letv.portal.service.app.IAppVersionService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("appVersionService")
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionModel> implements IAppVersionService{
	
	private final static Logger logger = LoggerFactory.getLogger(AppVersionServiceImpl.class);
	
	@Resource
	private IAppVersionDao appVersionDao;
	
	public AppVersionServiceImpl() {
		super(AppVersionModel.class);
	}

	@Override
	public IBaseDao<AppVersionModel> getDao() {
		return this.appVersionDao;
	}

	@Override
	public AppVersionModel getLastestVersionInfo(Map<String, Object> params) {
		return this.appVersionDao.getLastestVersionInfo(params);
	}
	
	
}
