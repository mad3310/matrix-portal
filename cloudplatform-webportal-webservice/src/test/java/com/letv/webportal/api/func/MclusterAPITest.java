package com.letv.webportal.api.func;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.letv.portal.model.MclusterModel;


public class MclusterAPITest {
	public static Logger logger = LoggerFactory.getLogger(MclusterAPITest.class);
	public static String API_URL = "http://localhost:8080/api";
	
	@Test
	public void testList(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		String url = API_URL + "/mcluster/list";
		logger.debug(url);
		
		Map<String, String> params = new HashMap<String, String>();  
        params.put("mclusterName", "test"); 
        
		String result = restTemplate.postForObject(url, params,String.class);
		logger.debug("result===>" + result);
	}
	
	@Test
	public void testSave(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		
		String url = API_URL + "/mcluster/save";
		logger.debug(url);
		MclusterModel model = new MclusterModel(null,"testJunit","0","liuhao1@letv.com");
		String result = restTemplate.postForObject(url, model, String.class);
		logger.debug("result===>" + result);
	}
	@Test
	public void testGetInfoById(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		
		String url = API_URL + "/mcluster/getById/{clusterId}";
		logger.debug(url);
		String result = restTemplate.getForObject(url, String.class,"0945e7a3-778a-40fc-a721-a7986906b7f4");
		logger.debug("result===>" + result);
	}
	@Test
	public void testGetInfoByIdInMap(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		
		String url = API_URL + "/mcluster/getById/{clusterId}";
		Map<String, String> params = new HashMap<String, String>();  
        params.put("clusterId", "0945e7a3-778a-40fc-a721-a7986906b7f4"); 
        
		logger.debug(url);
		String result = restTemplate.getForObject(url, String.class,params);
		logger.debug("result===>" + result);
	}
	
	
	
}
