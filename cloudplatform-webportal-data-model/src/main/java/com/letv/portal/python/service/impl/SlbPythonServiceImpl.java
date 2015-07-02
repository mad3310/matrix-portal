package com.letv.portal.python.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.util.HttpClient;
import com.letv.portal.constant.Constant;
import com.letv.portal.model.task.TaskResult;
import com.letv.portal.python.service.ISlbPythonService;
 
@Service("slbPythonService")
public class SlbPythonServiceImpl implements ISlbPythonService{
	
	private final static Logger logger = LoggerFactory.getLogger(SlbPythonServiceImpl.class);
	
	private final static String URL_HEAD = "http://";
	private final static String URL_PORT = "8888";	
	
	@Override
	public String createContainer(Map<String,String> params,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(URL_PORT).append("/containerCluster");
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String checkContainerCreateStatus(String gceClusterName,String ip,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(URL_PORT).append("/containerCluster/createResult/").append(gceClusterName);
		String result = HttpClient.get(url.toString(),username,password);
		return result;
	}

	@Override
	public String initZookeeper(String nodeIp,Map<String,String> parmas) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(URL_PORT).append("/admin/conf");
		
		String result = HttpClient.post(url.toString(), parmas);
		return result;
	}

	@Override
	public String initUserAndPwd4Manager(String nodeIp,String username,String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(URL_PORT).append("/admin/user");
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("adminUser", username);
		map.put("adminPassword", password);
		
		String result = HttpClient.post(url.toString(), map,username,password);
		return result;
	}

	@Override
	public String createContainer1(Map<String, String> params, String ip,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(URL_PORT).append("/cluster");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String syncContainer2(Map<String, String> params,String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/sync");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startCluster(String nodeIp1,String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/start");
		
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	@Override
	public String createContainer2(Map<String, String> params, String ip,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(":").append(URL_PORT).append("/cluster/node");
		
		String result = HttpClient.post(url.toString(), params,username,password);
		return result;
	}

	@Override
	public String CheckClusterStatus(String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster");
		
		String result = HttpClient.get(url.toString(),adminUser,adminPassword);
		return result;
	}

	@Override
	public String getVipIp(Map<String, String> params,String nodeIp1, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/resource/ip");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String commitProxyConfig(Map<String, String> params, String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/node/config");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String stop(Map<String, String> params, String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/stop");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String start(Map<String, String> params, String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/start");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String restart(Map<String, String> params, String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster/restart");
		
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String checkStatus(String nodeIp1,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(URL_PORT).append("/cluster");
		
		String result = HttpClient.get(url.toString(),adminUser,adminPassword);
		return result;
	}
	
}
