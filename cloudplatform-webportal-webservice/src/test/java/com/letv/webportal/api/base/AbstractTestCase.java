package com.letv.webportal.api.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:webservice-applicationContext.xml" })
public abstract class AbstractTestCase{
	
	public static String API_URL = "http://localhost/8080/api";
	
	@Autowired
	public RestTemplate restTemplate;
	
	@Before 
	public void setup() throws  Exception {
	}
    
}
