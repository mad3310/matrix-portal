package com.letv.portal.service.cloudgce;

import com.letv.portal.junitBase.AbstractTest;
import com.letv.portal.service.common.IZookeeperInfoService;
import com.letv.portal.service.gce.IGceImageService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GceImageTest extends AbstractTest{

	@Autowired
	private IGceImageService gceImageService;

	private final static Logger logger = LoggerFactory.getLogger(
			GceImageTest.class);


    @Test
    public void testBatchInsert() {
    	this.gceImageService.pushImage(1L,"1,48");
    }
}
