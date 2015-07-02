package com.letv.portal.python.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.util.HttpClient;
import com.letv.portal.python.service.ILogPythonService;
 
@Service("logPythonService")
public class LogPythonServiceImpl implements ILogPythonService{
	
	private final static Logger logger = LoggerFactory.getLogger(LogPythonServiceImpl.class);
	
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
	public String configOpenSSL(Map<String, String> map, String nodeIp1, String port, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/openssl/config");
		
		String result = HttpClient.post(url.toString(), map,adminUser,adminPassword);
		return result;
	}

	@Override
	public String cpOpenSSL(Map<String, String> params, String nodeIp1,
			String port, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/openssl/copy");
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}
	@Override
	public String configLogStashForwarder(Map<String, String> params, String nodeIp1,
			String port, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/logstash_forwarder/config");
		String result = HttpClient.post(url.toString(), params,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startLogStashForwarder(Map<String, String> map,
			String nodeIp1, String port, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/logstash_forwarder/restart");
		String result = HttpClient.post(url.toString(), map,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startLogStash(String nodeIp1, String port, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/logstash/start");
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startElesticSearch(String nodeIp1, String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/elasticsearch/start");
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	@Override
	public String startKibana(String nodeIp1, String port, String adminUser,
			String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port).append("/kibana/start");
		String result = HttpClient.post(url.toString(), null,adminUser,adminPassword);
		return result;
	}

	
}
