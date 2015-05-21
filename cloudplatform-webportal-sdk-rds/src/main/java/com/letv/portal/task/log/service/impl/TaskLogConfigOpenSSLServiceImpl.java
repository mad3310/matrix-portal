package com.letv.portal.task.log.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.portal.model.log.LogCluster;
import com.letv.portal.model.log.LogContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.ILogPythonService;

@Service("taskLogConfigOpenSSLService")
public class TaskLogConfigOpenSSLServiceImpl extends BaseTask4LogServiceImpl implements IBaseTaskService{

	@Autowired
	private ILogPythonService logPythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskLogConfigOpenSSLServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
		if(!(Boolean) params.get("isCreateLog"))  {
			tr.setSuccess(true);
			tr.setResult("no need to create Log");
			return tr;
		}
		//执行业务
		List<LogContainer> containers = super.getContainers(params);
		String nodeIp1 = containers.get(0).getHostIp();
		String port = containers.get(0).getMgrBindHostPort();
		LogCluster cluster = super.getLogCluster(params);
		Map<String,String> map = new HashMap<String,String>();
		map.put("ip", containers.get(0).getIpAddr());
		
		String result = this.logPythonService.configOpenSSL(map,nodeIp1,port, cluster.getAdminUser(), cluster.getAdminPassword());
		tr = analyzeRestServiceResult(result);
		
		tr.setParams(params);
		return tr;
	}
	
}
