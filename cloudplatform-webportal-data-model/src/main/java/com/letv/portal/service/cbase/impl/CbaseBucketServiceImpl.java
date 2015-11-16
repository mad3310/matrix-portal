package com.letv.portal.service.cbase.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.common.exception.ValidateException;
import com.letv.portal.dao.cbase.ICbaseBucketDao;
import com.letv.portal.enumeration.CbaseBucketStatus;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.cbase.CbaseBucketModel;
import com.letv.portal.model.cbase.CbaseClusterModel;
import com.letv.portal.model.cbase.CbaseContainerModel;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.cbase.ICbaseBucketService;
import com.letv.portal.service.cbase.ICbaseClusterService;
import com.letv.portal.service.cbase.ICbaseContainerService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("cbaseBucketService")
public class CbaseBucketServiceImpl extends BaseServiceImpl<CbaseBucketModel>
		implements ICbaseBucketService {

	private final static Logger logger = LoggerFactory
			.getLogger(CbaseBucketServiceImpl.class);

	@Resource
	private ICbaseBucketDao cbaseBucketDao;
	@Autowired
	private ICbaseClusterService cbaseClusterService;
	@Autowired
	private ICbaseContainerService cbaseContainerService;
	@Autowired
	private IHostService hostService;

	public CbaseBucketServiceImpl() {
		super(CbaseBucketModel.class);
	}

	@Override
	public IBaseDao<CbaseBucketModel> getDao() {
		return this.cbaseBucketDao;
	}

	@Override
	public Map<String, Object> save(CbaseBucketModel cbaseBucket) {
		cbaseBucket.setStatus(CbaseBucketStatus.BUILDDING.getValue());
		cbaseBucket.setAuthType("sasl");

		StringBuffer cbaseClusterName = new StringBuffer();
		cbaseClusterName.append(cbaseBucket.getCreateUser()).append("_")
				.append(cbaseBucket.getBucketName());

		CbaseClusterModel cbaseCluster = new CbaseClusterModel();
		cbaseCluster.setHclusterId(cbaseBucket.getHclusterId());
		cbaseCluster.setCbaseClusterName(cbaseClusterName.toString());
		cbaseCluster.setStatus(CbaseBucketStatus.BUILDDING.getValue());
		cbaseCluster.setCreateUser(cbaseBucket.getCreateUser());
		cbaseCluster.setAdminUser("root");
		cbaseCluster.setAdminPassword(cbaseClusterName.toString());

		this.cbaseClusterService.insert(cbaseCluster);

		cbaseBucket.setCbaseClusterId(cbaseCluster.getId());

		this.cbaseBucketDao.insert(cbaseBucket);

		List<HostModel> hostList = hostService.selectByHclusterId(cbaseBucket
				.getHclusterId());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cbaseClusterId", cbaseCluster.getId());
		params.put("cacheId", cbaseBucket.getId());
		params.put("serviceName", cbaseBucket.getBucketName());
		params.put("clusterName", cbaseCluster.getCbaseClusterName());
		params.put("hostSize", hostList.size());
		params.put("hostCount", hostList.size());
		return params;
	}

	@Override
	public List<CbaseBucketModel> selectByBucketNameForValidate(
			String bucketName, Long createUser) {
		if (StringUtils.isEmpty(bucketName) || null == createUser)
			throw new ValidateException("参数不合法");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("bucketName", bucketName);
		params.put("createUser", createUser);
		return this.cbaseBucketDao.selectByBucketNameForValidate(params);
	}

	@Override
	public CbaseBucketModel bucketList(Long cacheId) {
		if (cacheId == null)
			throw new ValidateException("参数不合法");
		CbaseBucketModel bucket = this.selectById(cacheId);
		if (bucket == null)
			throw new ValidateException("参数不合法，相关数据不存在");
		bucket.setContainers(this.cbaseContainerService
				.selectByCbaseClusterId(bucket.getCbaseClusterId()));
		return bucket;
	}

	@Override
	public Map<String, Object> getMoxiConfig(Long cacheId) {
		if (cacheId == null)
			throw new ValidateException("参数不合法");

		CbaseBucketModel bucket = this.selectById(cacheId);
		if (bucket == null)
			throw new ValidateException("参数不合法，相关数据不存在");

		Map<String, Object> moxiParams = new HashMap<String, Object>();

		List<CbaseContainerModel> containers = this.cbaseContainerService
				.selectByCbaseClusterId(bucket.getCbaseClusterId());

		List<String> cbaseHosts = new ArrayList<String>();
		for (CbaseContainerModel container : containers) {
			if ("cbase".equals(container.getType())) {
				cbaseHosts.add(container.getIpAddr());
			}
		}

		moxiParams.put("CBASE_HOST", cbaseHosts);
		moxiParams.put("CBASE_BUCKET", bucket.getBucketName());
		moxiParams.put("CBASE_PWD", bucket.getBucketName());

		return moxiParams;
	}

	@Override
	public Integer selectCountByStatus(Integer value) {
		return this.cbaseBucketDao.selectCountByStatus(value);
	}

}
