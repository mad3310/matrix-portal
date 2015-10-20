package com.letv.portal.model.task.service;

import java.util.List;
import java.util.Map;

import com.letv.portal.model.task.TaskChainIndex;
import com.letv.portal.service.IBaseService;

public interface ITaskChainIndexService extends IBaseService<TaskChainIndex>{

	TaskChainIndex  selectByServiceAndClusterName(String serviceName,String clusterName);
	List<Map<String, Object>> selectFailedChainIndex();
}
