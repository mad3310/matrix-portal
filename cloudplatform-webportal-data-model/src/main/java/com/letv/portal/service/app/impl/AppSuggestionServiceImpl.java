package com.letv.portal.service.app.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.app.IAppSuggestionDao;
import com.letv.portal.model.app.AppSuggestionModel;
import com.letv.portal.service.app.IAppSuggestionService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("appSuggestionService")
public class AppSuggestionServiceImpl extends BaseServiceImpl<AppSuggestionModel> implements IAppSuggestionService{
	
	private final static Logger logger = LoggerFactory.getLogger(AppSuggestionServiceImpl.class);
	
	@Resource
	private IAppSuggestionDao appSuggestionDao;
	
	public AppSuggestionServiceImpl() {
		super(AppSuggestionModel.class);
	}

	@Override
	public IBaseDao<AppSuggestionModel> getDao() {
		return this.appSuggestionDao;
	}

	@Override
	public void reportSuggestion(Map<String, Object> params) {
		this.appSuggestionDao.saveSuggestion(params);
	}

	
}
