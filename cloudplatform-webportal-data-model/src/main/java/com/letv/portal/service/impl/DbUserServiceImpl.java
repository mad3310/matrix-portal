package com.letv.portal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IDbUserDao;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.service.IDbUserService;


@Service("dbUserService")
public class DbUserServiceImpl extends BaseServiceImpl<DbUserModel> implements
		IDbUserService{
	
	private final static Logger logger = LoggerFactory.getLogger(DbUserServiceImpl.class);
	
	@Resource
	private IDbUserDao dbUserDao;
	
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

	



}
