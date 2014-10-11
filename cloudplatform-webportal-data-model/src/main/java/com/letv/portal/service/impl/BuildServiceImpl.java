package com.letv.portal.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IBuildDao;
import com.letv.portal.model.BuildModel;
import com.letv.portal.service.IBuildService;

@Service("buildService")
public class BuildServiceImpl extends BaseServiceImpl<BuildModel> implements
		IBuildService{
	
	private final static Logger logger = LoggerFactory.getLogger(BuildServiceImpl.class);
	
	@Resource
	private IBuildDao buildDao;

	public BuildServiceImpl() {
		super(BuildModel.class);
	}

	@Override
	public IBaseDao<BuildModel> getDao() {
		return this.buildDao;
	}

	@Override
	public List<BuildModel> selectByMclusterId(String mclusterId) {
		return this.buildDao.selectByMclusterId(mclusterId);
	}

	@Override
	public void initStatus(String mclusterId) {
		
		String[] stepMsgs = new String[]{"创建Mcluster节点","检查Mcluster节点状态","初始化Zookeeper节点","初始化mcluster管理用户名密码","提交mcluster集群信息","初始化集群","同步节点1信息","提交节点1信息","同步节点2信息","提交节点2信息 ","启动集群","检查集群状态"};
		
		for (int i = 0; i < stepMsgs.length; i++) {
			BuildModel buildModel = new BuildModel();
			buildModel.setStep(i+1);
			buildModel.setStepMsg(stepMsgs[i]);
			buildModel.setStatus("waitting");
			buildModel.setMclusterId(mclusterId);
			this.buildDao.insert(buildModel);
		}
		
	}

	@Override
	public void updateStatusFail(BuildModel buildModel) {
		this.buildDao.updateStatusFail(buildModel);
		
	}

	@Override
	public void deleteByCluster(String clusterId) {
		this.buildDao.deleteByCluster(clusterId);
	}


}
