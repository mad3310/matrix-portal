package com.letv.portal.junitBase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-applicationContext.xml"})
public abstract class AbstractTest{
	
	@Before 
	public void setup() throws  Exception {
	}
    
}