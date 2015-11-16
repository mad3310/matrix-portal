package com.letv.portal.service.slb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.common.exception.ValidateException;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.slb.ISlbConfigDao;
import com.letv.portal.dao.slb.ISlbServerDao;
import com.letv.portal.enumeration.GceStatus;
import com.letv.portal.enumeration.SlbAgentType;
import com.letv.portal.enumeration.SlbStatus;
import com.letv.portal.model.slb.SlbCluster;
import com.letv.portal.model.slb.SlbConfig;
import com.letv.portal.model.slb.SlbServer;
import com.letv.portal.service.impl.BaseServiceImpl;
import com.letv.portal.service.slb.ISlbClusterService;
import com.letv.portal.service.slb.ISlbConfigService;
import com.letv.portal.service.slb.ISlbServerService;

@Service("slbServerService")
public class SlbServerServiceImpl extends BaseServiceImpl<SlbServer> implements ISlbServerService{
	
	private final static Logger logger = LoggerFactory.getLogger(SlbServerServiceImpl.class);
	
	@Resource
	private ISlbServerDao slbServerDao;
	@Resource
	private ISlbConfigDao slbConfigDao;
	@Autowired
	private ISlbClusterService slbClusterService;
	@Resource
	private ISlbConfigService slbConfigService;
	@Value("${slb.code}")
	private String SLB_CODE;
	public SlbServerServiceImpl() {
		super(SlbServer.class);
	}

	@Override
	public IBaseDao<SlbServer> getDao() {
		return this.slbServerDao;
	}
	
	@Override
	public void insert(SlbServer t) {
		if(t == null)
			throw new ValidateException("参数不合法");
		t.setStatus(SlbStatus.NORMAL.getValue());
		super.insert(t);
	}
	@Override
	public Map<String,Object> save(SlbServer slbServer) {
		
		slbServer.setStatus(SlbStatus.BUILDDING.getValue());
		
		StringBuffer clusterName = new StringBuffer();
		clusterName.append(slbServer.getCreateUser()).append("_").append(SLB_CODE).append("_").append(slbServer.getSlbName());
		
		/*function 验证clusterName是否存在*/
		Boolean isExist= this.slbClusterService.isExistByName(clusterName.toString());
		int i = 0;
		while(!isExist) {
			isExist= this.slbClusterService.isExistByName(clusterName.toString() + (i+1));
			i++;
		}
		if(i>0)
			clusterName.append(i);
		//数据库添加slbCluster
		SlbCluster slbCluster = new SlbCluster();
		slbCluster.setHclusterId(slbServer.getHclusterId());
		slbCluster.setClusterName(clusterName.toString());
		slbCluster.setStatus(SlbStatus.BUILDDING.getValue());
		slbCluster.setCreateUser(slbServer.getCreateUser());
		slbCluster.setAdminUser("root");
		slbCluster.setAdminPassword(clusterName.toString());
		
		this.slbClusterService.insert(slbCluster);
		
		//数据库添加slbServer
		slbServer.setSlbClusterId(slbCluster.getId());
		this.slbServerDao.insert(slbServer);
		
		Map<String,Object> params = new HashMap<String,Object>();
    	params.put("slbClusterId", slbCluster.getId());
    	params.put("slbId", slbServer.getId());
    	params.put("serviceName", slbServer.getSlbName());
    	params.put("clusterName", slbCluster.getClusterName());
		return params;
	}
	@Override
	public <K, V> Page selectPageByParams(Page page, Map<K, V> params) {
		page = super.selectPageByParams(page, params);
		List<SlbServer> data = (List<SlbServer>) page.getData();
		
		for (SlbServer slbServer : data) {
			List<SlbConfig> slbConfigs = this.slbConfigService.selectBySlbServerId(slbServer.getId());
			slbServer.setSlbConfigs(slbConfigs);
		}
		page.setData(data);
		return page;
	}

	@Override
	public void updateSlbStatusById(Long id, SlbStatus status) {
		SlbServer slbServer = new SlbServer();
		slbServer.setId(id);
		slbServer.setStatus(status.getValue());
		
		this.slbServerDao.updateBySelective(slbServer);
	}

	@Override
	public Integer selectCountByStatus(Integer value) {
		return this.slbServerDao.selectCountByStatus(value);
	}

}
