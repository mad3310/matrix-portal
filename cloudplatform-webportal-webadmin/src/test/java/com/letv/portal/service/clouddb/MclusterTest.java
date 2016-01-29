package com.letv.portal.service.clouddb;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.letv.portal.junitBase.AbstractTest;
import com.letv.portal.model.MclusterModel;
import com.letv.portal.proxy.IMclusterProxy;
import com.letv.portal.service.IMclusterService;
 
public class MclusterTest extends AbstractTest{

	@Autowired
	private IMclusterService mclusterService;
	@Autowired
	private IMclusterProxy mclusterProxy;
	
	private final static Logger logger = LoggerFactory.getLogger(
			MclusterTest.class);

  
    @Test
    public void testDelete() {
    	MclusterModel mcluster = this.mclusterService.selectById(28L);
    	this.mclusterService.delete(mcluster);
    }
    @Test
    public void testSelectValidMclusters() {
    	List<MclusterModel> selectValidMclusters = this.mclusterService.selectValidMclusters(0);
    	for (MclusterModel mclusterModel : selectValidMclusters) {
			System.out.println(mclusterModel.getMclusterName());
		}
    	List<MclusterModel> selectValidMclusters1 = this.mclusterService.selectValidMclusters(1);
    	for (MclusterModel mclusterModel : selectValidMclusters1) {
    		System.out.println(mclusterModel.getMclusterName());
    	}
    	List<MclusterModel> selectValidMclusters2 = this.mclusterService.selectValidMclusters(2);
    	for (MclusterModel mclusterModel : selectValidMclusters2) {
    		System.out.println(mclusterModel.getMclusterName());
    	}
    	List<MclusterModel> selectValidMclusters3 = this.mclusterService.selectValidMclusters(3);
    	for (MclusterModel mclusterModel : selectValidMclusters3) {
    		System.out.println(mclusterModel.getMclusterName());
    	}
    	List<MclusterModel> selectValidMclusters4 = this.mclusterService.selectValidMclusters(4);
    	for (MclusterModel mclusterModel : selectValidMclusters4) {
    		System.out.println(mclusterModel.getMclusterName());
    	}
    }
    
    @Test
    public void testRestartDb() {
    	this.mclusterProxy.restartDb(22L);
    }
	@Test
	public void testAddContainer() {
		this.mclusterProxy.addContainerOnMcluster(3378L,1);
	}
    
}
