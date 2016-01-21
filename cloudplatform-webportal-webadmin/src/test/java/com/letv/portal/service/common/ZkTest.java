package com.letv.portal.service.common;

import com.letv.portal.junitBase.AbstractTest;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.proxy.IMclusterProxy;
import com.letv.portal.service.IMclusterService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ZkTest extends AbstractTest{

	@Autowired
	private IZookeeperInfoService zookeeperInfoService;

	private final static Logger logger = LoggerFactory.getLogger(
			ZkTest.class);


    @Test
    public void testBatchInsert() {
    	this.zookeeperInfoService.batchInsert("test","127.0.0.1,127.0.0.2",1L);
    }
    
}
