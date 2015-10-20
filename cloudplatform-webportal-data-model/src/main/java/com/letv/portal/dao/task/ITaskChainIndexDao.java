package com.letv.portal.dao.task;

import java.util.List;
import java.util.Map;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.task.TaskChainIndex;

public interface ITaskChainIndexDao extends IBaseDao<TaskChainIndex> {
	/**
	  * @Title: selectFailedChainIndex
	  * @Description: 获取失败的ChainIndex
	  * @return List<Map<String,Object>> 返回WEBPORTAL_TEMPLATE_TASK表中的Name和失败条数  
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:09:29
	  */
	List<Map<String, Object>> selectFailedChainIndex();
}
