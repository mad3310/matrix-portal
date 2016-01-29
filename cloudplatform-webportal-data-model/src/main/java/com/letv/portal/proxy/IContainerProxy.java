package com.letv.portal.proxy;

import java.util.List;
import java.util.Map;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.monitor.ContainerMonitorModel;

/**Program Name: IContainerProxy <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年10月7日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IContainerProxy extends IBaseProxy<ContainerModel> {
	
	/**Methods Name: start <br>
	 * Description: 启动container<br>
	 * @author name: liuhao1
	 * @param containerId
	 */
	public void start(Long containerId);

	/**Methods Name: stop <br>
	 * Description: 停止container<br>
	 * @author name: liuhao1
	 * @param containerId
	 */
	public void stop(Long containerId);

	/**Methods Name: checkStatus <br>
	 * Description: 检查container状态<br>
	 * @author name: liuhao1
	 */
	public void checkStatus();

	/**
	 * Methods Name: selectMonitorMclusterDetail <br>
	 * Description: 查询集群的详细监控信息或者列表<br>
	 * @author name: wujun
	 * @param map
	 * @return
	 */
	public List<ContainerMonitorModel> selectMonitorMclusterDetailOrList(Map map);
	/**
	 * Methods Name: getMonitorDetailNodeAndDbData <br>
	 * Description: 集群监控详细信息展示<br>
	 * @author name: wujun
	 * @param ip
	 * @return
	 */
	public ContainerMonitorModel selectMonitorDetailNodeAndDbData(String ip);	
	/**
	 * Methods Name: getMonitorDetailClusterData <br>
	 * Description: 集群监控主要信息展示<br>
	 * @author name: wujun
	 * @param ip
	 * @return
	 */
	public ContainerMonitorModel selectMonitorDetailClusterData(String ip);

	void deleteAndBuild(ContainerModel containerModel);
}
