package com.letv.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.StringUtils;

/**Program Name: HttpClient <br>
 * Description:  httpclient封装<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年9月11日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@SuppressWarnings("deprecation")
public class HttpClient {
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	public static String post(String url, Map<String, String> params) {
		return post(url, params, null, null);
	}
	
	public static String post(String url, Map<String, String> params,String username,String password) {
		
		DefaultHttpClient httpclient = getHttpclient(username,password);
		String body = null;
		
		logger.info("create httppost:" + url);
		HttpPost post = postForm(url, params);
		
		body = invoke(httpclient, post);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	
	public static String get(String url) {
		return get(url, null, null);
	}
	
	public static String get(String url,String username,String password) {
		DefaultHttpClient httpclient = getHttpclient(username,password);
		String body = null;
		
		logger.info("create httpget:" + url);
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
		
	
	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		
		return body;
	}

	private static String paseResponse(HttpResponse response) {
		logger.info("get response from http server..");
		HttpEntity entity = response.getEntity();
		
		logger.info("response status: " + response.getStatusLine());
		String charset = EntityUtils.getContentCharSet(entity);
		logger.info(charset);
		
		String body = null;
		try {
			body = EntityUtils.toString(entity);
			logger.info(body);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		logger.info("execute post...");
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params){
		
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try {
			logger.info("set utf-8 form entity to httppost");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}
	private static DefaultHttpClient getHttpclient(String username,String password){
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		if(!StringUtils.isNullOrEmpty(username)) {
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			UsernamePasswordCredentials usernamePassword = new UsernamePasswordCredentials(username, password);
			credsProvider.setCredentials(AuthScope.ANY, usernamePassword);
			httpclient.setCredentialsProvider(credsProvider);
		}
	    return httpclient;
	}
}
