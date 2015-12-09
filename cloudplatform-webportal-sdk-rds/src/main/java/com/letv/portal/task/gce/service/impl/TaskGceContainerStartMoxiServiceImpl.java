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
import com.letv.portal.model.cbase.CbaseBucketModel;
import com.letv.portal.model.cbase.CbaseContainerModel;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.gce.GceContainerExt;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.model.task.service.IBaseTaskService;
import com.letv.portal.python.service.IGcePythonService;
import com.letv.portal.service.cbase.ICbaseBucketService;
import com.letv.portal.service.cbase.ICbaseContainerService;
import com.letv.portal.service.gce.IGceContainerExtService;

@Service("taskGceContainerStartMoxiService")
public class TaskGceContainerStartMoxiServiceImpl extends BaseTask4GceServiceImpl implements IBaseTaskService{

	@Autowired
	private IGcePythonService gcePythonService;
	@Autowired
	private ICbaseContainerService cbaseContainerService;
	@Autowired
	private ICbaseBucketService cbaseBucketService;
	@Autowired
	private IGceContainerExtService gceContainerExtService;
	
	private final static Logger logger = LoggerFactory.getLogger(TaskGceContainerStartMoxiServiceImpl.class);
	
	@Override
	public TaskResult execute(Map<String, Object> params) throws Exception {
		TaskResult tr = super.execute(params);
		if(!tr.isSuccess())
			return tr;
		
		if(!(Boolean) params.get("isCreateLog"))  {
			tr.setSuccess(true);
			tr.setResult("no need to config moxi");
			return tr;
		}
		
		//页面下拉框选择，传入
		Long cbaseBucketId = getLongFromObject(params.get("ocsId"));
		if(cbaseBucketId == null){
			tr.setSuccess(true);
			tr.setResult("no config OCS, continue next step.");
			return tr;
		}
		
		CbaseBucketModel bucket = this.cbaseBucketService.selectById(cbaseBucketId);
		if (bucket == null)
			throw new ValidateException("参数不合法，相关数据不存在");
		
		List<CbaseContainerModel> cbaseContainers = this.cbaseContainerService
				.selectByCbaseClusterId(bucket.getCbaseClusterId());

		StringBuffer cbaseHosts = new StringBuffer();
		for (CbaseContainerModel container : cbaseContainers) {
			if ("cbase".equals(container.getType())) {
				cbaseHosts.append(container.getIpAddr()).append(",");
			}
		}
		
		//获取jetty类型的gce-container
		List<GceContainer> gceContainers = super.getContainers(params);
		

		ApiParam apiParam;
				
		for (GceContainer gceContainer : gceContainers) {
			Map<String,String> map = new HashMap<String,String>();
			
			map.clear();
			map.put("containerId", gceContainer.getId().toString());
			map.put("type", "moxi");
			List<GceContainerExt> ext = this.gceContainerExtService.selectByMap(map);
			String port = null;
			if(null != ext && !ext.isEmpty())
				port = ext.get(0).getBindPort();

			apiParam = super.getApiParam(gceContainer, ManageType.MOXI, port);
			
			//配置moxi
			map.clear();
			map.put("CBASE_HOST", cbaseHosts.length()>0?cbaseHosts.substring(0,cbaseHosts.length()-1):cbaseHosts.toString());
			map.put("CBASE_BUCKET", bucket.getBucketName());
			map.put("CBASE_PWD", bucket.getBucketName());
			ApiResultObject result = this.gcePythonService.configMoxi(map, gceContainer.getHostIp(),ext.get(0).getBindPort());
			tr = analyzeRestServiceResult(result);
			if(!tr.isSuccess()) {
				break;
			}
			
			//启动moxi
			result = this.gcePythonService.startMoxi(apiParam.getIp(),apiParam.getPort());
			tr = analyzeRestServiceResult(result);
			//任何一个container创建失败时,停止循环,返回失败的结果
			if(!tr.isSuccess()) {
				break;
			}
		}
		
		tr.setParams(params);
		return tr;
	}
	
}
