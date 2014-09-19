package com.letv.portal.python.service;

import com.letv.portal.model.DbUserModel;

/**Program Name: IPythonService <br>
 * Description:  与底层python rest交互接口<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月10日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */

public interface IPythonService {
	
	/**Methods Name: createContainer <br>
	 * Description: 创建container，执行一次,传入mclusterName<br>
	 * @author name: liuhao1
	 * @param mclusterName
	 * @return
	 */
	public String createContainer(String mclusterName);
	
	/**Methods Name: checkContainerCreateStatus <br>
	 * Description: 检查container创建状态,通过检查策略进行检查<br>
	 * @author name: liuhao1
	 * @return
	 */
	public String checkContainerCreateStatus(String mclusterName);
	
	/**Methods Name: initZookeeper <br>
	 * Description: 初始化zookeeper节点<br>
	 * @author name: liuhao1
	 * @param nodeIp 初始化的ip地址
	 * @return
	 */

	public String initZookeeper(String nodeIp);
	
	/**Methods Name: initUserAndPwd4Manager <br>
	 * Description: 初始化mcluster管理用户名密码<br>
	 * @author name: liuhao1
	 * @param nodeIp 初始化的ip地址
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String initUserAndPwd4Manager(String nodeIp,String username,String password);
	
	/**Methods Name: postMclusterInfo <br>
	 * Description: 提交mcluster集群信息<br>
	 * @author name: liuhao1
	 * @param mclusterName  mcluster名称
	 * @param nodeIp  节点ip
	 * @param nodeName 节点名称
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String postMclusterInfo(String mclusterName,String nodeIp,String nodeName,String username,String password);
	
	/**Methods Name: initMcluster <br>
	 * Description: 初始化集群<br>
	 * @author name: liuhao1
	 * @param nodeIp  节点ip
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String initMcluster(String nodeIp,String username,String password);

	/**Methods Name: postContainerInfo <br>
	 * Description: 提交节点信息<br>
	 * @author name: liuhao1
	 * @param nodeIp  节点ip
	 * @param nodeName 节点名称
	 * @return
	 */
	public String postContainerInfo(String nodeIp,String nodeName,String username,String password);
	
	/**Methods Name: syncContainer <br>
	 * Description: 同步节点信息<br>
	 * @author name: liuhao1
	 * @param nodeIp  同步的主节点ip
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String syncContainer(String nodeIp,String username,String password);
	
	/**Methods Name: startMcluster <br>
	 * Description: 启动集群<br>
	 * @author name: liuhao1
	 * @param nodeIp  启动的主节点ip
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String startMcluster(String nodeIp,String username,String password);
	
	/**Methods Name: checkContainerStatus <br>
	 * Description: 检查集群状态：检查节点状态<br>
	 * @author name: liuhao1
	 * @param nodeIp  检查的主节点ip
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String checkContainerStatus(String nodeIp,String username,String password);
	
	/**Methods Name: createDb <br>
	 * Description: 创建数据库<br>
	 * @author name: liuhao1
	 * @param nodeIp
	 * @param dbName
	 * @param dbUserName
	 * @param ipAddress
	 * @param username 集群用户名
	 * @param password 集群密码
	 * @return
	 */
	public String createDb(String nodeIp,String dbName,String dbUserName,String ipAddress,String username,String password);
	

	/**Methods Name: createDbUser <br>
	 * Description: 创建数据库用户<br>
	 * @author name: liuhao1
	 * @param dbUserModel
	 * @param dbName
	 * @param username
	 * @param password
	 * @return
	 */
	public String createDbUser(DbUserModel dbUserModel,String dbName,String nodeIp,String username,String password);
	
	/**Methods Name: startGbalancer <br>
	 * Description: 启动gbalancer<br>
	 * @author name: liuhao1
	 * @param nodeIp
	 * @param user
	 * @param pwd
	 * @param ipListPort
	 * @param port
	 * @param args
	 * @param username
	 * @param password
	 * @return
	 */
	public String startGbalancer(String nodeIp,String user,String pwd,String ipListPort,String port,String args,String username,String password);
	
}
