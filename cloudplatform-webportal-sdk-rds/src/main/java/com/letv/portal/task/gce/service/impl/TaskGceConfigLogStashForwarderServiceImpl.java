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
import com.letv.portal.model.log.LogContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.python.service.ILogPythonService;

@Service("taskGceConfigLogStashForwarderService")
public class TaskGceConfigLogStashForwarderServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	@Autowired
	private ILogPythonService logPythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceConfigLogStashForwarderServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		//执行业务
		List<GceContainer> containers = super.getContainers(params);
		GceCluster cluster = super.getGceCluster(params);
		Map<String,String> map = new HashMap<String,String>();
				
		List<LogContainer> logContainers = super.getLogContainers(params);
		String logStashIp = logContainers.get(0).getIpAddr();
		for (GceContainer gceContainer : containers) {
			map.put("ip", logStashIp);
			map.put("paths", "/var/log/letv/*\",\"/var/log/nginx/*\",\"var/log/jetty_stdout.log");
			
			ApiResultObject resultObject = this.logPythonService.configLogStashForwarder(map,gceContainer.getHostIp(),gceContainer.getLogBindHostPort(), cluster.getAdminUser(), cluster.getAdminPassword());
			tr = analyzeRestServiceResult(resultObject);
			if(!tr.isSuccess())
				break;
		}
		
		tr.setParams(params);
		return tr;
	}
	
}
