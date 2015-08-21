package com.letv.portal.task.gce.service.impl;

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

@Service("taskGceInitLogUserAndPwdService")
public class TaskGceInitLogUserAndPwdServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceInitLogUserAndPwdServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;

		//执行业务
		GceCluster cluster = super.getGceCluster(params);
		List<GceContainer> containers = super.getContainers(params);
		
		ApiParam apiParam;
		for (GceContainer gceContainer : containers) {
			
			
			apiParam = super.getApiParam(gceContainer, ManageType.LOGS, gceContainer.getLogBindHostPort());
			ApiResultObject resultObject = this.gcePythonService.initUserAndPwd4Manager(apiParam.getIp(),apiParam.getPort(), cluster.getAdminUser(), cluster.getAdminPassword());
			tr = analyzeRestServiceResult(resultObject);
			if(!tr.isSuccess())
				break;
		}
		tr.setParams(params);
		return tr;
	}
	
}
