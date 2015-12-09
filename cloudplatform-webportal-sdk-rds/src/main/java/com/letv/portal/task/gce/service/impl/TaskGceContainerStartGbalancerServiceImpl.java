package com.letv.portal.task.gce.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.common.exception.ValidateException;
import com.letv.common.result.ApiResultObject;
import com.letv.portal.constant.Constant;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.model.gce.GceCluster;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.gce.GceContainerExt;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.service.IContainerService;
import com.letv.portal.service.IDbService;
import com.letv.portal.service.IMclusterService;
import com.letv.portal.service.gce.IGceContainerExtService;

@Service("taskGceContainerStartGbalancerService")
public class TaskGceContainerStartGbalancerServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	@Autowired
	private IContainerService containerService;
	@Autowired
	private IDbService dbService;
	@Autowired
	private IGceContainerExtService gceContainerExtService;
	@Autowired
	private IMclusterService mclusterService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceContainerStartGbalancerServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
		
		if(!(Boolean) params.get("isCreateLog"))  {
			tr.setSuccess(true);
			tr.setResult("no need to config RDS");
			return tr;
		}
		//页面下拉框选择，传入
		Long dbId = getLongFromObject(params.get("rdsId"));
		if(dbId == null) {
			tr.setSuccess(true);
			tr.setResult("no config RDS, continue next step.");
			return tr;
		}
		
		DbModel db = this.dbService.selectById(dbId);
		
		//获取GceCLuster
		GceCluster gceCluster = super.getGceCluster(params);
		//获取rds-CLuster
		MclusterModel mclusterModel = this.mclusterService.selectById(db.getMclusterId());
		if(mclusterModel == null)
			throw new ValidateException("mclusterModel is null by mclusterId:" + db.getMclusterId());
		
		//获取rds-container
		List<ContainerModel> rdsContainers = this.containerService.selectByMclusterId(db.getMclusterId());
		if(rdsContainers.isEmpty())
			throw new ValidateException("containers is empty by mclusterId:" + db.getMclusterId());
		String nodeIp1 = rdsContainers.get(0).getIpAddr();
		String nodeIp2 = rdsContainers.get(1).getIpAddr();
		String nodeIp3 = rdsContainers.get(2).getIpAddr();
		
		StringBuffer ipListPort = new StringBuffer();
		ipListPort.append(nodeIp1).append(":3306,").append(nodeIp2).append(":3306,").append(nodeIp3).append(":3306");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("user", "monitor");
		map.put("passwd", mclusterModel.getSstPwd());
		map.put("iplist_port",ipListPort.toString());
		map.put("port", "3306");
		map.put("args", "-daemon");
		map.put("service","mysql");
		
		//获取jetty类型的gce-container
		List<GceContainer> gceContainers = super.getContainers(params);
	
		ApiParam apiParam;
		
		for (GceContainer gceContainer : gceContainers) {
			Map<String, Object> extParam = new HashMap<String, Object>();
			extParam.put("containerId", gceContainer.getId().toString());
			extParam.put("type", "glb");
			List<GceContainerExt> ext = this.gceContainerExtService.selectByMap(extParam);
			String port = null;
			if(null != ext && !ext.isEmpty())
				port = ext.get(0).getBindPort();
			
			apiParam = super.getApiParam(gceContainer, ManageType.GBALANCER, port);
			
			ApiResultObject result = this.gcePythonService.startGbalancer(map, apiParam.getIp(),apiParam.getPort(), gceCluster.getAdminUser(), gceCluster.getAdminPassword());
			logger.info("call startGbalancer, result is : "+result.getResult()+"-"+result.getUrl());
			tr = analyzeRestServiceResult(result);
			//任何一个container创建失败时,停止循环,返回失败的结果
			if(!tr.isSuccess()) {
				break;
			}
		}
		
		tr.setParams(params);
		return tr;
	}
	
	@Override
	public TaskResult analyzeRestServiceResult(ApiResultObject resultObject) {
		TaskResult tr = new TaskResult();
		Map<String, Object> map = transToMap(resultObject.getResult());
		if(map == null) {
			tr.setSuccess(false);
			tr.setResult("api connect failed:" + resultObject.getUrl());
			return tr;
		}
		Map<String,Object> meta = (Map<String, Object>) map.get("meta");
		
		boolean isSucess = Constant.PYTHON_API_RESPONSE_SUCCESS.equals(String.valueOf(meta.get("code")));
		//417类型异常，业务判断是否正常
		boolean judge = Constant.PYTHON_API_RESPONSE_JUDGE.equals(String.valueOf(meta.get("code"))) && "this glb is running".equals(meta.get("errorDetail"));
		
		tr.setSuccess(isSucess);
		if(isSucess) {
			Map<String,Object> response = (Map<String, Object>) map.get("response");
			tr.setResult((String) response.get("message"));
			tr.setParams(response);
		} else {
			if(judge) {
				tr.setSuccess(judge);
				tr.setResult("this glb is already running");
			} else {
				tr.setResult((String) meta.get("errorType") +",the api url:" + resultObject.getUrl());
			}
		}
		return tr;
	}
	
}
