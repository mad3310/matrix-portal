package com.letv.portal.service.gce.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.gce.IGceContainerExtDao;
import com.letv.portal.model.gce.GceContainerExt;
import com.letv.portal.service.gce.IGceContainerExtService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("gceContainerExtService")
public class GceContainerExtServiceImpl extends BaseServiceImpl<GceContainerExt> implements IGceContainerExtService{
	
	private final static Logger logger = LoggerFactory.getLogger(GceContainerExtServiceImpl.class);
	
	@Resource
	private IGceContainerExtDao gceContainerExtDao;

	public GceContainerExtServiceImpl() {
		super(GceContainerExt.class);
	}

	@Override
	public IBaseDao<GceContainerExt> getDao() {
		return this.gceContainerExtDao;
	}


	@Override
	public void deleteByClusterId(Long id) {
		this.gceContainerExtDao.deleteByClusterId(id);
	}
}
