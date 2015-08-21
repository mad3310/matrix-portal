package com.letv.portal.task.gce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.common.result.ApiResultObject;
import com.letv.portal.enumeration.MclusterStatus;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.gce.GceCluster;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.service.gce.IGceContainerService;

@Service("taskGceContainer1CreateService")
public class TaskGceContainer1CreateServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	@Autowired
	private IGceContainerService gceContainerService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceContainer1CreateServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		//执行业务
		List<GceContainer> containers = super.getContainers(params);
		GceContainer container = containers.get(0);
		
		ApiParam apiParam = super.getApiParam(container, ManageType.MANAGER, container.getMgrBindHostPort());
		
		GceCluster cluster = super.getGceCluster(params);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("clusterName", cluster.getClusterName());
		map.put("dataNodeName", container.getContainerName());
		map.put("dataNodeIp", apiParam.getIp());
		map.put("dataNodeExternalPort", apiParam.getPort());
		
		ApiResultObject resultObject =this.gcePythonService.createContainer1(map,apiParam.getIp(),apiParam.getPort(),cluster.getAdminUser(),cluster.getAdminPassword());
		tr = analyzeRestServiceResult(resultObject);
		if(tr.isSuccess()) {
			Map data = (Map) ((Map)transToMap(resultObject.getResult()).get("response")).get("data");
			container.setContainerUuid((String)data.get("uuid"));
			this.gceContainerService.updateBySelective(container);
		}
		tr.setParams(params);
		return tr;
	}
	
}
