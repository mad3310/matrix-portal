package com.letv.portal.task.gce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.letv.common.result.ApiResultObject;
import com.letv.portal.model.gce.GceCluster;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.model.task.service.ITaskEngine;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.service.gce.IGceClusterService;
import com.letv.portal.service.gce.IGceContainerService;
import com.letv.portal.service.gce.IGceServerService;

@Service("taskGceNginxConfigService")
public class TaskGceNginxConfigServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	@Autowired
	private IGceClusterService gceClusterService;
	@Autowired
	private IGceServerService gceServerService;
	@Autowired
	private IGceContainerService gceContainerService;
	@Autowired
	private ITaskEngine taskEngine;
	@Value("${gce.engine.category}")
	private String GCE_ENGINE_CATEGORY;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceNginxConfigServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
		boolean isConfig = (Boolean) params.get("isConfig");
		if(!isConfig) {
			tr.setSuccess(true);
			tr.setResult("no doing in this step.");
			return tr;
		}
		Long nginxClusterId = super.getLongFromObject(params.get("gceClusterId"));
		Long jettyClusterId = super.getLongFromObject(params.get("pGceClusterId"));
		
		GceCluster nginxCluster = this.gceClusterService.selectById(nginxClusterId);
		List<GceContainer> nginxContainers = gceContainerService.selectByGceClusterId(nginxClusterId);
		List<GceContainer> jettyContainers = gceContainerService.selectByGceClusterId(jettyClusterId);
		
		StringBuffer sb = new StringBuffer();
		
		for (GceContainer jettyContainer : jettyContainers) {
			
			/*String containerPorts = jettyContainer.getBindContainerPort();
			String hostPorts = jettyContainer.getBingHostPort();
			if(StringUtils.isEmpty(containerPorts) || StringUtils.isEmpty(hostPorts)) {
				tr.setSuccess(false);
				tr.setResult("jetty containerPorts or hostPorts is null");
				return tr;
			}
			String[] containerPortArry = containerPorts.split(",");
			String[] hostPortArry = hostPorts.split(",");
			int j = -1;
			for (int i = 0; i < containerPortArry.length; i++) {
				if("8080".equals(containerPortArry[i])) {
					j = i;
					break;
				}
			}
			if(j<0){
				tr.setSuccess(false);
				tr.setResult("jetty containerPorts or hostPorts is null");
				return tr;
			}
			sb.append(jettyContainer.getHostIp()).append(":").append(hostPortArry[j]).append(",");*/
			sb.append(jettyContainer.getIpAddr()).append(":").append("8080").append(",");
			
		}
		
		Map<String, String> param = new HashMap<String,String>();
		
		param.put("containerClusterName", nginxCluster.getClusterName());
		param.put("upstreamName", "newupstream");
		param.put("serverPorts", sb.length()>0?sb.substring(0, sb.length()-1):sb.toString());
		
		ApiParam apiParam;
		for (GceContainer c : nginxContainers) {
			apiParam = super.getApiParam(c, ManageType.MANAGER, c.getMgrBindHostPort());
			ApiResultObject resultObject =  this.gcePythonService.nginxProxyConfig(param,apiParam.getIp(),apiParam.getPort(),nginxCluster.getAdminUser(),nginxCluster.getAdminPassword());
			tr = analyzeRestServiceResult(resultObject);
		}
		
		tr.setParams(params);
		return tr;
	}
	
	@Override
	public void callBack(TaskResult tr) {
		super.rollBack(tr);

		Map<String,Object> params = (Map<String, Object>) tr.getParams();
		boolean isContinue = (Boolean) params.get("isContinue");
		if(!isContinue) {
			return;
		}
		Map<String,Object> nextParams = (Map<String, Object>) params.get("nextParams");
		this.taskEngine.run(GCE_ENGINE_CATEGORY, nextParams);
	}
	
}
