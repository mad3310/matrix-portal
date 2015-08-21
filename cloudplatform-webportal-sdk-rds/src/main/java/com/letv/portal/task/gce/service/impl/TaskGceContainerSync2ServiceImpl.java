package com.letv.portal.task.gce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.common.result.ApiResultObject;
import com.letv.portal.model.gce.GceCluster;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;

@Service("taskGceContainerSync2Service")
public class TaskGceContainerSync2ServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceContainerSync2ServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		//执行业务
		List<GceContainer> containers = super.getContainers(params);
		if(containers.size()==1) {
			tr.setResult("only one node.");
			tr.setParams(params);
			tr.setSuccess(true);
			return tr;
		}
		
		GceContainer container = containers.get(1);
		
		ApiParam apiParam = super.getApiParam(container, ManageType.MANAGER, container.getMgrBindHostPort());
		
		GceCluster cluster = super.getGceCluster(params);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("clusterUUID", containers.get(0).getContainerUuid());
		ApiResultObject resultObject =  this.gcePythonService.syncContainer2(map,apiParam.getIp(),apiParam.getPort(),cluster.getAdminUser(),cluster.getAdminPassword());
		tr = analyzeRestServiceResult(resultObject);
		
		tr.setParams(params);
		return tr;
	}
	
}
