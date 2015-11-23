package com.letv.portal.service.slb.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.slb.ISlbContainerDao;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.slb.SlbContainer;
import com.letv.portal.service.impl.BaseServiceImpl;
import com.letv.portal.service.slb.ISlbContainerService;

@Service("slbContainerService")
public class SlbContainerServiceImpl extends BaseServiceImpl<SlbContainer> implements ISlbContainerService{
	
	private final static Logger logger = LoggerFactory.getLogger(SlbContainerServiceImpl.class);
	
	@Resource
	private ISlbContainerDao slbContainerDao;

	public SlbContainerServiceImpl() {
		super(SlbContainer.class);
	}

	@Override
	public IBaseDao<SlbContainer> getDao() {
		return this.slbContainerDao;
	}

	@Override
	public List<SlbContainer> selectBySlbClusterId(Long slbClusterId) {
		return this.slbContainerDao.selectBySlbClusterId(slbClusterId);
	}

	@Override
	public SlbContainer selectByName(String containerName) {
		return this.slbContainerDao.selectByName(containerName);
	}

	@Override
	public void deleteByClusterId(Long id) {
		this.slbContainerDao.deleteByClusterId(id);
	}

}
