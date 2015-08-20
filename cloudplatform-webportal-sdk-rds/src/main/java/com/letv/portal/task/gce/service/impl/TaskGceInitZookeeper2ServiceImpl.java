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

@Service("taskGceInitZookeeper2Service")
public class TaskGceInitZookeeper2ServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceInitZookeeper2ServiceImpl.class);
	
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
		String nodeIp2 = containers.get(1).getHostIp();
		String port = containers.get(1).getMgrBindHostPort();
		ZookeeperInfo zk = super.getMinusedZk();
		Map<String, String> zkParm = new HashMap<String,String>();
		zkParm.put("zkAddress", zk.getIp());
		zkParm.put("zkPort", zk.getPort());
		ApiResultObject resultObject = this.gcePythonService.initZookeeper(nodeIp2,port,zkParm);
		tr = analyzeRestServiceResult(resultObject);
		
		tr.setParams(params);
		return tr;
	}
	
}
