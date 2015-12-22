package com.letv.portal.proxy;

import java.util.Date;
import java.util.List;

import com.letv.portal.model.MonitorIndexModel;
import com.letv.portal.model.monitor.MonitorViewYModel;



public interface IMonitorProxy {
    
	
	/**
	 * Methods Name: collectMclusterServiceData <br>
	 * Description: 搜集集群服务数据任务<br>
	 * @author name: wujun
	 * @return
	 */
	 void collectMclusterServiceData();

	 List<MonitorViewYModel> getDbData(Long serverId,Long chartId, Integer strategy,boolean isTimeAveraging,int format);
	 List<MonitorViewYModel> getData(String type,Long serverId,Long chartId, Integer strategy,boolean isTimeAveraging,int format);

	/**Methods Name: deleteOutData <br>
	 * Description: 删除过时数据：当前时间往前一个月<br>
	 * @author name: liuhao1
	 */
	 void deleteOutData();
	 void deleteOutData(String tableName, String columnName, Date date);

	void collectClusterServiceData();
	void collectOSSServiceData();
	
	/**
	  * @Title: addMonitorPartition
	  * @Description: 添加分区（每天24点之前执行一次，添加第8天2个分区0:00-12:00,12:00-24:00）   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年7月16日 下午6:51:42
	  */
	void addMonitorPartition();
	
	/**
	  * @Title: deleteMonitorPartitionThirtyDaysAgo
	  * @Description: 删除30天以前的分区  
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年7月17日 上午10:26:53
	  */
	void deleteMonitorPartitionThirtyDaysAgo();
	/**
	  * @Title: collectMysqlMonitorData
	  * @Description: 根据监控表基础数据和调用Python接口查询不经常变化数据组合到mysql监控相关表   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年7月28日 上午11:25:06
	  */
	void collectMysqlMonitorData();
	
	/**
	  * @Title: collectMysqlMonitorBaseData
	  * @Description: 获取mysql监控基础数据   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年7月28日 下午5:36:33
	  */
	void collectMysqlMonitorBaseData();
	/**
	  * @Title: collectMysqlMonitorBaseSpaceData
	  * @Description: 获取mysql数据库和表大小  
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年7月30日 下午5:01:58
	  */
	void collectMysqlMonitorBaseSpaceData();
	
	/**
	  * @Title: monitorErrorReport
	  * @Description: 监控错误信息发送报告   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月6日 下午6:24:51
	  */
	 void monitorErrorReport();
	
	/**
	  * @Title: deleteMonitorErrorData
	  * @Description: 删除监控错误表历史数据   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月10日 下午3:57:59
	  */
	 void deleteMonitorErrorData();
}
