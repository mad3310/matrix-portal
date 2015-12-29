package com.letv.portal.python.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.result.ApiResultObject;
import com.letv.common.util.HttpClient;
import com.letv.portal.python.service.ICbasePythonService;

@Service("cbasePythonService")
public class CbasePythonServiceImpl implements ICbasePythonService {

	private final static Logger logger = LoggerFactory
			.getLogger(CbasePythonServiceImpl.class);

	private final static String URL_HEAD = "http://";
	private final static String URL_PORT = ":8888";

	@Override
	public ApiResultObject createContainer(Map<String, String> params, String ip,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT)
				.append("/containerCluster");
		String result = HttpClient.post(url.toString(), params, username,
				password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject checkContainerCreateStatus(String cbaseClusterName,
			String ip, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(ip).append(URL_PORT)
				.append("/containerCluster/createResult/").append(cbaseClusterName);
		String result = HttpClient.get(url.toString(), username, password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject initUserAndPwd4Manager(String nodeIp, String port,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port)
				.append("/settings/web");

		Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		map.put("port", port);

		String result = HttpClient.post(url.toString(), map);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject configClusterMemQuota(String nodeIp1, String port,
			String memoryQuota, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port)
				.append("/pools/default");

		Map<String, String> map = new HashMap<String, String>();
		map.put("memoryQuota", memoryQuota);
		String result = postCbaseManager(url.toString(), nodeIp1,
                port, map, adminUser, adminPassword);
		return new ApiResultObject(result,url.toString());
	}
	private static String postCbaseManager(String url, String ip, String port,
										  Map<String, String> params, String username, String password) {

		CloseableHttpClient httpclient = HttpClients.createDefault();

		String body = null;

		HttpHost targetHost = new HttpHost(ip, Integer.valueOf(port), "http");

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials(username, password));

		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());

		// Add AuthCache to the execution context
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);

		logger.info("create httppost:" + url);
		HttpPost post = postForm(url, params);

		HttpResponse response = sendRequest(httpclient, targetHost, post,
				context);
		logger.info("get response from http server..");
		if (response == null) {
			logger.info("get response from http server.. faild");
			return null;
		}

		HttpEntity entity = response.getEntity();

		logger.info("response status: " + response.getStatusLine());

		try {
			logger.info(EntityUtils.toString(entity));
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		body = Integer.toString(response.getStatusLine().getStatusCode());

		httpclient.getConnectionManager().shutdown();

		return body;
	}
    private static CloseableHttpResponse sendRequest(
            CloseableHttpClient httpclient, HttpHost targetHost, HttpPost post,
            HttpClientContext context) {
        logger.info("execute post...");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(targetHost, post, context);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {
        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        if (params != null && !params.isEmpty()) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
                logger.info("param-->" + key + ":" + params.get(key));
            }
        }

        try {
            logger.info("set utf-8 form entity to httppost");
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }
	@Override
	public ApiResultObject addNodeToCluster(String srcNodeIp, String addNodeIp,
			String port, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(srcNodeIp).append(":").append(port)
				.append("/controller/addNode");

		Map<String, String> map = new HashMap<String, String>();
		map.put("hostname", addNodeIp);
		map.put("user", username);
		map.put("password", password);

		String result = postCbaseManager(url.toString(), srcNodeIp,
                port, map, username, password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject rebalanceCluster(String nodeIp, String port,
			String knownNodes, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port)
				.append("/controller/rebalance");

		Map<String, String> map = new HashMap<String, String>();
		map.put("ejectedNodes", "");
		map.put("knownNodes", knownNodes);

		String result = postCbaseManager(url.toString(), nodeIp,
                port, map, username, password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject checkClusterRebalanceStatus(String nodeIp, String port,
			String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port)
				.append("/pools/default/rebalanceProgress");

		String result = HttpClient.get(url.toString(), username,
				password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject createPersistentBucket(String nodeIp, String port,
			String bucketName, String ramQuotaMB, String authType,
			String saslPassword, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port)
				.append("/pools/default/buckets");

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", bucketName);
		map.put("ramQuotaMB", ramQuotaMB);
		map.put("authType", authType);
		map.put("saslPassword", saslPassword);
		map.put("replicaNumber", "1");

		String result = postCbaseManager(url.toString(), nodeIp,
                port, map, username, password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject createUnPersistentBucket(String nodeIp, String port,
			String bucketName, String ramQuotaMB, String authType,
			String saslPassword, String username, String password) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp).append(":").append(port)
				.append("/pools/default/buckets");

		Map<String, String> map = new HashMap<String, String>();
		map.put("name", bucketName);
		map.put("ramQuotaMB", ramQuotaMB);
		map.put("authType", authType);
		map.put("saslPassword", saslPassword);
		map.put("bucketType", "memcached");

		String result = postCbaseManager(url.toString(), nodeIp,
                port, map, username, password);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject CheckClusterStatus(String nodeIp1, String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port)
				.append("/cluster");

		String result = HttpClient
				.get(url.toString(), adminUser, adminPassword);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject stop(Map<String, String> params, String nodeIp1, String port,
			String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port)
				.append("/cluster/stop");

		String result = HttpClient.post(url.toString(), params, adminUser,
				adminPassword);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject start(Map<String, String> params, String nodeIp1,
			String port, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port)
				.append("/cluster/start");

		String result = HttpClient.post(url.toString(), params, adminUser,
				adminPassword);
		return new ApiResultObject(result,url.toString());
	}

	@Override
	public ApiResultObject restart(Map<String, String> params, String nodeIp1,
			String port, String adminUser, String adminPassword) {
		StringBuffer url = new StringBuffer();
		url.append(URL_HEAD).append(nodeIp1).append(":").append(port)
				.append("/cluster/reload");

		String result = HttpClient.post(url.toString(), params, adminUser,
				adminPassword);
		return new ApiResultObject(result,url.toString());
	}

}
