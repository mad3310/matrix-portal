package com.letv.portal.python.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.util.ConfigUtil;
import com.letv.common.util.HttpClient;
import com.letv.portal.enumeration.DbUserRoleStatus;
import com.letv.portal.model.DbUserModel;
import com.letv.portal.model.HostModel;
import com.letv.portal.python.service.IPythonService;
 
@Service("pythonService")
public class PythonServiceImpl implements IPythonService{
	
	private final static Logger logger = LoggerFactory.getLogger(PythonServiceImpl.class);
	
	private final static String HOST_CREATE_URL = ConfigUtil.getString("host_create_url");//"http://10.200.91.142:8888"; 
	private final static String HOST_LOGIN_URL = ConfigUtil.getString("host_login_url");//"http://10.200.91.142:8888"; 
	private final static String URL_HEAD = "http://";	//ConfigUtil.getString("http://");
	private final static String URL_IP = "";			//ConfigUtil.getString("");
	private final static String URL_PORT = ":8888";		//ConfigUtil.getString("8888");
	private final static String GBALANCER_PORT = ":9888";		//ConfigUtil.getString("8888");
	
	@Override
	public String createContainer(String mclusterName,String ip,String username,String password) {
		String url =  URL_HEAD  + ip + URL_PORT + "/containerCluster";
		Map<String,String> map = new HashMap<String,String>();
		map.put("containerClusterName", mclusterName);
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	@Override
	public String checkContainerCreateStatus(String mclusterName,String ip,String username,String password) {
		String url = URL_HEAD  + ip + URL_PORT + "/containerCluster/createStatus/" + mclusterName;
		String result = HttpClient.get(url,username,password);
		return result;
	}

	@Override
	public String initZookeeper(String nodeIp) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/admin/conf";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("zkAddress", "127.0.0.1");
		
		String result = HttpClient.post(url, map);
		return result;
	}

	@Override
	public String initUserAndPwd4Manager(String nodeIp,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/admin/user";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("adminUser", username);
		map.put("adminPassword", password);
		
		String result = HttpClient.post(url, map);
		return result;
	}

	@Override
	public String postMclusterInfo(String mclusterName,String nodeIp,String nodeName,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("clusterName", mclusterName);
		map.put("dataNodeIp", nodeIp);
		map.put("dataNodeName", nodeName);
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	@Override
	public String initMcluster(String nodeIp,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster/init?forceInit=false";
	
		String result = HttpClient.get(url,username,password);
		return result;
	}

	@Override
	public String postContainerInfo(String nodeIp,String nodeName,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster/node";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("dataNodeIp", nodeIp);
		map.put("dataNodeName", nodeName);
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	@Override
	public String syncContainer(String nodeIp,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster/sync";
		String result = HttpClient.get(url);
		return result;
	}

	@Override
	public String startMcluster(String nodeIp,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster/start";

		Map<String,String> map = new HashMap<String,String>();
		map.put("cluster_flag", "new");
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	@Override
	public String checkContainerStatus(String nodeIp,String username,String password) {
		String url = URL_HEAD  + nodeIp + URL_PORT + "/cluster/check/online_node";
		String result = HttpClient.get(url,username,password);
		return result;
		
	}

	@Override
	public String createDb(String nodeIp,String dbName,String dbUserName,String ipAddress,String username,String password) {
	
		String url = URL_HEAD  + nodeIp + URL_PORT + "/db";
		Map<String,String> map = new HashMap<String,String>();
		map.put("dbName", dbName);
		map.put("userName", dbUserName);
		map.put("ip_address", "127.0.0.1");
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}


	@Override
	public String createDbUser(DbUserModel dbUser, String dbName,String nodeIp,String username, String password) {
		//假设数据
		String url = URL_HEAD  + nodeIp + URL_PORT + "/dbUser";
		
				
		Map<String,String> map = new HashMap<String,String>();
		if(DbUserRoleStatus.WR.getValue() == dbUser.getType()) {
			map.put("role", "wr");
		}
		if(DbUserRoleStatus.MANAGER.getValue() == dbUser.getType()) {
			map.put("role", "manager");
		}
		if(DbUserRoleStatus.RO.getValue() == dbUser.getType()) {
			map.put("role", "ro");
		}
		map.put("dbName", dbName);
		map.put("userName", dbUser.getUsername());
		//自动生成密码
		map.put("user_password", dbUser.getPassword());
		map.put("ip_address", dbUser.getAcceptIp());
		map.put("max_queries_per_hour", String.valueOf(dbUser.getMaxQueriesPerHour()));
		map.put("max_updates_per_hour", String.valueOf(dbUser.getMaxUpdatesPerHour()));
		map.put("max_connections_per_hour", String.valueOf(dbUser.getMaxConnectionsPerHour()));
		map.put("max_user_connections", String.valueOf(dbUser.getMaxUserConnections()));
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	@Override
	public String startGbalancer(String nodeIp,String user,String pwd,String server,String ipListPort,String port,String args,String username,String password) {
		
		String url = URL_HEAD  + nodeIp + GBALANCER_PORT + "/glb/start";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("user", user);
		map.put("passwd", pwd);
		map.put("iplist_port",ipListPort);
		map.put("port", port);
		map.put("args",args);
		map.put("service",server);
		
		String result = HttpClient.post(url, map,username,password);
		return result;
	}

	public String deleteDbUser(DbUserModel dbUserModel,String dbName,String nodeIp,String username, String password){
		String url = URL_HEAD  + nodeIp + URL_PORT + "/dbUser"+"/"+dbName+"/"+dbUserModel.getUsername()+"/"+URLEncoder.encode(dbUserModel.getAcceptIp());
		String result = HttpClient.detele(url, username, password);
		return result;
	}

	@Override
	public String removeMcluster(String mclusterName,String ip,String username,String password) {
		String url = URL_HEAD  + ip + URL_PORT  + "/containerCluster?containerClusterName=" + mclusterName;
		String result = HttpClient.detele(url,username,password);
		return result;
	}

	@Override
	public String startMcluster(String mclusterName,String ip,String username,String password) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("containerClusterName", mclusterName);
		String result = HttpClient.post(URL_HEAD  + ip + URL_PORT  + "/containerCluster/start", map,username,password);
		return result;
	}

	@Override
	public String stopMcluster(String mclusterName,String ip,String username,String password) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("containerClusterName", mclusterName);
		String result = HttpClient.post(URL_HEAD  + ip + URL_PORT  + "/containerCluster/stop", map,username,password);
		return result;
	}

	@Override
	public String startContainer(String containerName,String ip,String username,String password) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("containerName", containerName);
		String result = HttpClient.post(URL_HEAD  + ip + URL_PORT + "/container/start", map,username,password);
		return result;
	}

	@Override
	public String stopContainer(String containerName,String ip,String username,String password) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("containerName", containerName);
		String result = HttpClient.post(URL_HEAD  + ip + URL_PORT + "/container/stop", map,username,password);
		return result;
	}

	@Override
	public String checkMclusterStatus(String mclusterName,String ip,String username,String password) {
		String url = URL_HEAD  + ip + URL_PORT  + "/containerCluster/status/" + mclusterName;
		String result = HttpClient.get(url,username,password);
		return result;
	}

	@Override
	public String checkContainerStatus(String containerName,String ip,String username,String password) {
		String url = URL_HEAD  + ip + URL_PORT  + "/container/status/" + containerName;
		String result = HttpClient.get(url,username,password);
		return result;
	}

	@Override
	public String initHcluster(String hostIp) {
		String url =URL_HEAD+ hostIp+URL_PORT+HOST_LOGIN_URL;
		Map<String,String> map = new HashMap<String,String>();
		map.put("adminUser", "root");
		map.put("adminPassword", "root");
		String result = HttpClient.post(url, map);
		return result;
	}

	@Override
	public String createHost(HostModel hostModel) {
		String url = URL_HEAD+ hostModel.getHostIp()+URL_PORT+HOST_CREATE_URL;
		Map<String,String> map = new HashMap<String,String>();
		map.put("clusterName", hostModel.getHcluster().getHclusterName());
		map.put("dataNodeIp", hostModel.getHostIp());
		map.put("dataNodeName", hostModel.getHostName());
		String result = HttpClient.post(url, map,hostModel.getName(),hostModel.getPassword());	
		return result;
	}

	@Override
	public String checkMclusterCount(String hostIp, String name, String password) {
		String url = URL_HEAD  + hostIp + URL_PORT  + "/containerCluster/sync";
		String result = HttpClient.get(url,name,password);
		return result;
	}

	public String getMclusterStatus(String ip)throws Exception{
		String url = URL_HEAD  + ip + URL_PORT  + "/mcluster/status";
		String result = HttpClient.get(url,1000,2000);
		return result;
		
	}
	public String getMclusterMonitor(String ip)throws Exception{
	    String url = URL_HEAD  + ip + URL_PORT  + "/mcluster/monitor";
	    String result = HttpClient.get(url,1000,2000);
	    return result;
	}
	
	@Override
	public String getMonitorData(String ip, String index) {
		String url = URL_HEAD  + ip + URL_PORT  + index;
		String result = HttpClient.get(url,1000,1000);
		return result;
	}    
	
}
