package com.letv.portal.service.gce;

import com.letv.portal.enumeration.GceType;
import com.letv.portal.junitBase.AbstractTest;
import com.letv.portal.model.gce.GceServer;
import com.letv.portal.proxy.IGceProxy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GceServerServiceTest extends AbstractTest{

	@Autowired
	private IGceProxy gceProxy;
	
	private final static Logger logger = LoggerFactory.getLogger(
			GceServerServiceTest.class);

    @Test
    public void testSelectPageByParams() {
		GceServer gceServer = new GceServer();
		gceServer.setCreateUser(5L);
		gceServer.setHclusterId(48L);
		gceServer.setType(GceType.TOMCAT);
		gceServer.setGceName("test");
		gceServer.setBuyNum(1);
		gceServer.setCreateNginx(false);
		this.gceProxy.saveAndBuild(gceServer,null,null);
    }
    

}
