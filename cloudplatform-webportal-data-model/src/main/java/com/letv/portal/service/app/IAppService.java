package com.letv.portal.service.app;

import java.util.List;
import java.util.Map;

import com.letv.common.paging.impl.Page;
import com.letv.portal.model.ContainerModel;


public interface IAppService {
	/**
	  * @Title: getHomeInfo
	  * @Description: 获取app主页展示信息（RDS监控异常、待审核数据库、任务流异常）
	  * @return Map<String,List<Object>>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:12:18
	  */
	Map<String, List<Object>> getHomeInfo();
	/**
	  * @Title: getRdsFault
	  * @Description: 获取app主页RDS监控异常信息
	  * @return Page   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月21日 下午6:00:43
	  */
	Page getRdsFault(Page page);
	/**
	  * @Title: getRdsFault
	  * @Description: 获取app主页待审核数据库信息
	  * @return Page   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月21日 下午6:00:43
	  */
	Page getPendingAuditDb(Page page, Map<String, Object> params);
	/**
	  * @Title: getMessages
	  * @Description: 获取所有通知消息
	  * @param page
	  * @param params
	  * @return Page   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月22日 上午7:03:43
	  */
	Page getMessages(Page page, Map<String, Object> params);
	/**
	  * @Title: checkUpdate
	  * @Description: 根据平台类型获取最新app版本信息
	  * @param params
	  * @return Map<String,Object>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:13:39
	  */
	Map<String, Object> checkUpdate(Map<String, Object> params);
	/**
	  * @Title: reportSuggestion
	  * @Description: 保存反馈建议
	  * @param params void   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:14:14
	  */
	void reportSuggestion(Map<String, Object> params);
	/**
	  * @Title: editUserInfo
	  * @Description: 修改用户信息
	  * @param params void   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月22日 上午7:17:18
	  */
	void editUserInfo(Map<String, Object> params);
	
	/**
	  * @Title: failedList
	  * @Description: 获取异常的列表
	  * @param params
	  * @param type 1-表示mcluster，2-表示node，3-表示db
	  * @return List<ContainerModel>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月21日 上午9:24:06
	  */
	List<ContainerModel> failedList(Map<String, Object> params, long type);
}
