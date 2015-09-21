package com.letv.portal.service.cloudvm.impl;

import com.letv.common.dao.IBaseDao;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.dao.cloudvm.ICloudvmVmCountDao;
import com.letv.portal.model.cloudvm.CloudvmVmCount;
import com.letv.portal.service.cloudvm.ICloudvmVmCountService;
import com.letv.portal.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxianguang on 2015/8/19.
 */
@Service("vmCountService")
public class CloudvmVmCountServiceImpl extends BaseServiceImpl<CloudvmVmCount> implements
		ICloudvmVmCountService {
	@Resource
	private ICloudvmVmCountDao vmCountDao;

	@Autowired
	private SessionServiceImpl sessionService;

	public CloudvmVmCountServiceImpl() {
		super(CloudvmVmCount.class);
	}

	@Override
	public IBaseDao<CloudvmVmCount> getDao() {
		return vmCountDao;
	}

	@Override
	public CloudvmVmCount getVmCountOfCurrentUser() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("createUser", sessionService.getSession().getUserId());
		List<CloudvmVmCount> resultList = vmCountDao.selectByMap(params);
		if (resultList.isEmpty()) {
			return null;
		} else {
			return resultList.get(0);
		}
	}

	@Override
	public void createVmCountOfCurrentUser(int count) {
		CloudvmVmCount vmCount = new CloudvmVmCount(sessionService.getSession().getUserId(),
				count);
		vmCountDao.insert(vmCount);
	}

	@Override
	public void updateVmCountOfCurrentUser(int count) {
		CloudvmVmCount vmCount = getVmCountOfCurrentUser();
		if (vmCount == null) {
			createVmCountOfCurrentUser(count);
		} else {
			vmCount.setVmCount(count);
			vmCountDao.update(vmCount);
		}
	}
}
