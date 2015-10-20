package com.letv.portal.proxy;

import java.util.List;
import java.util.Map;



public interface IDashBoardProxy{

	Map<String,Integer> selectManagerResource();

	Map<String,Integer> selectAppResource();

	Map<String,Float> selectDbStorage();
	
	List<Map<String,Object>> selectDbConnect();

	Map<String,Integer> selectMonitorAlert(Long monitorType);
	/**
	  * @Title: selectMonitorAlertWithMultiThread
	  * @Description: 使用多线程方式实现监控警告
	  * @param monitorType
	  * @return Map<String,Integer>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月24日 下午5:47:00
	  */
	Map<String,Integer> selectMonitorAlertWithMultiThread(Long monitorType);
	
}
