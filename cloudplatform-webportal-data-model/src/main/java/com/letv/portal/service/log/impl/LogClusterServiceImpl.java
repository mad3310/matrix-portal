package com.letv.portal.service.log.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.common.exception.MatrixException;
import com.letv.portal.dao.log.ILogClusterDao;
import com.letv.portal.enumeration.MclusterStatus;
import com.letv.portal.enumeration.MclusterType;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.log.LogCluster;
import com.letv.portal.model.log.LogContainer;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.impl.BaseServiceImpl;
import com.letv.portal.service.log.ILogClusterService;
import com.letv.portal.service.log.ILogContainerService;

@Service("logClusterService")
public class LogClusterServiceImpl extends BaseServiceImpl<LogCluster> implements ILogClusterService{
	
	private final static Logger logger = LoggerFactory.getLogger(LogClusterServiceImpl.class);
	
	@Resource
	private ILogClusterDao logClusterDao;
	@Resource
	private ILogContainerService logContainerService;
	@Resource
	private IHostService hostService;
	
	public LogClusterServiceImpl() {
		super(LogCluster.class);
	}

	@Override
	public IBaseDao<LogCluster> getDao() {
		return this.logClusterDao;
	}

	@Override
	public Boolean isExistByName(String clusterName) {
		List<LogCluster> mclusters = this.logClusterDao.selectByName(clusterName);
		return mclusters.size() == 0?true:false;
	}
	
	@Override
	public List<LogCluster> selectByName(String clusterName) {
		return this.logClusterDao.selectByName(clusterName);
	}
	
	@Override
	public void asyncClusterCount(Map<String,Object> mm,HclusterModel hcluster) {
		String clusterName = (String) mm.get("clusterName");
		if(StringUtils.isEmpty(clusterName))
			return;
		List<LogCluster> list = this.selectByName(clusterName);
		if(null == list || list.isEmpty()) {
			this.addHandMcluster(mm,hcluster.getId());
		} else {
			LogCluster cluster = list.get(0);
			if(MclusterStatus.BUILDDING.getValue() == cluster.getStatus())
				return;
			addOrUpdateContainer(mm,cluster);
		}
	}

	private void addOrUpdateContainer(Map<String,Object> mm,LogCluster cluster) {
		List<Map<String,Object>> cms = (List<Map<String,Object>>) mm.get("nodeInfo");
		for (Map<String,Object> cm : cms) {
			LogContainer container  = this.logContainerService.selectByName((String) cm.get("containerName"));
			if(null == container) {
				this.addHandContainer(cm, cluster.getId());
				continue;
			} 
		}
	}
	
	private void addHandMcluster(Map mm,Long hclusterId) {
		LogCluster cluster = new LogCluster();
		cluster.setClusterName((String) mm.get("clusterName"));
		cluster.setStatus(MclusterStatus.RUNNING.getValue());	
		cluster.setAdminUser("root");
		cluster.setAdminPassword((String) mm.get("clusterName"));
		cluster.setType(MclusterType.HAND.getValue());
		cluster.setHclusterId(hclusterId);
		cluster.setDeleted(true);
		this.insert(cluster);
		List<Map<String,Object>> cms = (List<Map<String,Object>>) mm.get("nodeInfo");
		for (Map<String,Object> cm : cms) {
			this.addHandContainer(cm,cluster.getId());
		}
	}
	
	private void addHandContainer(Map cm,Long mclusterId) {
		
		LogContainer container = new LogContainer();
		try {
			BeanUtils.populate(container, cm);
		} catch (Exception e) {
			throw new MatrixException("container转换失败", e);
		}
		container.setLogClusterId(mclusterId);
		container.setIpMask((String) cm.get("netMask"));
		container.setStatus(MclusterStatus.RUNNING.getValue());
		//物理机集群维护完成后，修改此处，需要关联物理机id
		HostModel hostModel = this.hostService.selectByIp((String) cm.get("hostIp"));
		if(null != hostModel) {
			container.setHostId(hostModel.getId());
		}
		this.logContainerService.insert(container);
	}

}
