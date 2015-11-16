package com.letv.portal.service.swift.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.swift.ISwiftServerDao;
import com.letv.portal.model.swift.SwiftServer;
import com.letv.portal.service.impl.BaseServiceImpl;
import com.letv.portal.service.swift.ISwiftServerService;

@Service("swiftServerService")
public class SwiftServerServiceImpl extends BaseServiceImpl<SwiftServer> implements ISwiftServerService{
	
	private final static Logger logger = LoggerFactory.getLogger(SwiftServerServiceImpl.class);
	
	@Resource
	private ISwiftServerDao swiftServerDao;
	
	public SwiftServerServiceImpl() {
		super(SwiftServer.class);
	}

	@Override
	public IBaseDao<SwiftServer> getDao() {
		return this.swiftServerDao;
	}

	@Override
	public Integer selectCountByStatus(Integer value) {
		return this.swiftServerDao.selectCountByStatus(value);
	}
}
