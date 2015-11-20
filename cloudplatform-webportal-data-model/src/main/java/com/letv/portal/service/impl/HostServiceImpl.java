package com.letv.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.common.dao.QueryParam;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.IContainerDao;
import com.letv.portal.dao.IHostDao;
import com.letv.portal.enumeration.HostType;
import com.letv.portal.model.HostModel;
import com.letv.portal.service.IHclusterService;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.IMclusterService;

@Service("hostService")
public class HostServiceImpl extends BaseServiceImpl<HostModel> implements
		IHostService{
	
	private final static Logger logger = LoggerFactory.getLogger(HostServiceImpl.class);
	
	private static final String PYTHON_URL = "";
	private static final String SUCCESS_CODE = "";
	
	@Resource
	private IHostDao hostDao;
	@Resource
	private IContainerDao containerDao;
	
	@Resource
	private IMclusterService mclusterService;
	@Resource
	private IHclusterService hclusterService;

	public HostServiceImpl() {
		super(HostModel.class);
	}

	@Override
	public IBaseDao<HostModel> getDao() {
		return this.hostDao;
	}

	@Override
	public void updateNodeCount(Long hostId,String type) {
		HostModel host = this.hostDao.selectById(hostId);
		Integer number = "+".equals(type)?host.getNodesNumber()+1:host.getNodesNumber()-1;
		host.setNodesNumber(number);
		this.hostDao.updateNodesNumber(host);
		
	}

	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.hostDao.selectPageByMap(param));
		page.setTotalRecords(this.hostDao.selectByMapCount(params));
		return page;
	}
	public List<HostModel> selectByHclusterId(Long hclusterId){
		return this.hostDao.selectByHclusterId(hclusterId);
	}
	public List<HostModel> selectByIpOrHostName(HostModel hostModel){
		Map<String, String> map = new HashMap<String, String>();
		List<HostModel> list = new ArrayList<HostModel>();
		if(hostModel!=null){
			if(hostModel.getHostIp()!=null){
				map.put("hostIp", hostModel.getHostIp());
			}else {
				map.put("hostName", hostModel.getHostName());			
			}
			list = this.hostDao.selectByNameOrIp(map);
		}
		return list;
	}

	@Override
	public HostModel selectByIp(String hostIp) {
		return this.hostDao.selectByIp(hostIp);
	}

	@Override
	public List<HostModel> isExistContainerOnHost(HostModel hostModel) {
		return this.hostDao.isExistContainerOnHost(hostModel);
	}
	
	@Override
	public HostModel getHostByHclusterId(Long hclusterId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("hclusterId", hclusterId);
		map.put("type", HostType.MASTER.getValue());
		List<HostModel> hostModels = this.selectByMap(map);
		if(null == hostModels || hostModels.isEmpty())
			return null;
		return hostModels.get(0);
	}

	@Override
	public Integer selectCountByHclusterType(String type) {
		return this.hostDao.selectCountByHclusterType(type);
	}
}
