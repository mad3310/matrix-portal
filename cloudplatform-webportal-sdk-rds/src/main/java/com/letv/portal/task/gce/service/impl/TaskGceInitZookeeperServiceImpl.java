package com.letv.portal.task.gce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.common.result.ApiResultObject;
import com.letv.portal.model.common.ZookeeperInfo;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;

@Service("taskGceInitZookeeperService")
public class TaskGceInitZookeeperServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceInitZookeeperServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		//执行业务
		List<GceContainer> containers = super.getContainers(params);
		GceContainer container = containers.get(0);
		
		ApiParam apiParam = super.getApiParam(container, ManageType.MANAGER, container.getMgrBindHostPort());
		
		List<ZookeeperInfo> zk = super.selectMinusedZkByHclusterId(super.getGceCluster(params).getHclusterId(),1);
		Map<String, String> zkParm = new HashMap<String,String>();
		zkParm.put("zkAddress", zk.get(0).getIp());
		zkParm.put("zkPort", zk.get(0).getPort());
		ApiResultObject resultObject = this.gcePythonService.initZookeeper(apiParam.getIp(),apiParam.getPort(),zkParm);
		tr = analyzeRestServiceResult(resultObject);
		
		tr.setParams(params);
		return tr;
	}
	
}
