package com.letv.portal.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IDbApplyStandardDao;
import com.letv.portal.dao.IHostDao;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.HostModel;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.IMclusterService;

@Service("hostService")
public class HostServiceImpl extends BaseServiceImpl<HostModel> implements
		IHostService{
	
	private final static Logger logger = LoggerFactory.getLogger(HostServiceImpl.class);
	
	private static final String PYTHON_URL = "";
	private static final String SUCCESS_CODE = "";
	
	@Resource
	private IHostDao hostDao;
	@Resource
	private IDbApplyStandardDao dbApplyStandardDao;
	
	@Resource
	private IContainerDao containerDao;
	
	@Resource
	private IMclusterService mclusterService;

	public HostServiceImpl() {
		super(HostModel.class);
	}

	@Override
	public IBaseDao<HostModel> getDao() {
		return this.hostDao;
	}

	@Override
	public void updateNodeCount(String hostId,String type) {
		HostModel host = this.hostDao.selectById(hostId);
		Integer number = "+".equals(type)?host.getNodesNumber()+1:host.getNodesNumber()-1;
		host.setNodesNumber(number);
		this.hostDao.updateNodesNumber(host);
		
	}

}
