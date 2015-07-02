package com.letv.portal.python.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.util.HttpClient;
import com.letv.portal.python.service.IGcePythonService;
 
@Service("gcePythonService")
public class GcePythonServiceImpl implements IGcePythonService{
	
	private final static Logger logger = LoggerFactory.getLogger(GcePythonServiceImpl.class);
	
	private final static String URL_HEAD = "http://";
	private final static String URL_PORT = ":8888";	
	
	@Override
	public String createContainer(Map<String,String> params,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT).append("/containerCluster");
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String checkContainerCreateStatus(String gceClusterName,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT).append("/containerCluster/createResult/").append(gceClusterName);
		String result = HttpClient.get(url.toString(),username,password);
		return result;
	}

	@Override
	public String initZookeeper(String nodeIp,String port,Map<String,String> params) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port).append("/admin/conf");
		
		String result = HttpClient.post(url.toString(), params);
		return result;
	}

	@Override
	public String initUserAndPwd4Manager(String nodeIp,String port,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port).append("/admin/user");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("adminUser", username);
		map.put("adminPassword", password);
		
		String result = HttpClient.post(url.toString(), map,username,password);
		return result;
	}

	@Override
	public String createContainer1(Map<String, String> params, String ip,String port,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(port).append("/cluster");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String syncContainer2(Map<String, String> params,String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/sync");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startCluster(String nodeIp1,String port, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/start");
		
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	@Override
	public String createContainer2(Map<String, String> params, String ip,
			String port, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(port).append("/cluster/node");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String CheckClusterStatus(String nodeIp1, String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster");
		
		String result = HttpClient.get(url.toString(),adminUser,adminPassword);
		return result;
	}

	@Override
	public String nginxProxyConfig(Map<String, String> params, String ip,
			String port, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(port).append("/cluster/node/config");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}
	@Override
	public String stop(Map<String, String> params, String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/stop");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String start(Map<String, String> params, String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/start");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String restart(Map<String, String> params, String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster/reload");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String checkStatus(String nodeIp1,String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/cluster");
		
		String result = HttpClient.get(url.toString(),adminUser,adminPassword);
		return result;
	}
}
