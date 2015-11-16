package com.letv.portal.service.impl;

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
import com.letv.portal.dao.IHclusterDao;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.service.IHclusterService;

@Service("hclusterService")
public class HClusterServiceImpl extends BaseServiceImpl<HclusterModel> implements
		IHclusterService{
	
	private final static Logger logger = LoggerFactory.getLogger(HClusterServiceImpl.class);
	
	@Resource
	private IHclusterDao hclusterDao;

	public HClusterServiceImpl() {
		super(HclusterModel.class);
	}

	@Override
	public IBaseDao<HclusterModel> getDao() {
		return this.hclusterDao;
	}


	@Override
	public Page findPagebyParams(Map<String, Object> params, Page page) {
		QueryParam param = new QueryParam(params,page);
		page.setData(this.hclusterDao.selectPageByMap(param));
		page.setTotalRecords(this.hclusterDao.selectByMapCount(params));
		return page;
	}
	public Map<String, Object> selectByHclusterId(Long hclusterId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hcluster", hclusterDao.selectHclusterById(hclusterId).getHclusterName());
		map.put("hclusterDetail", hclusterDao.selectByHclusterId(hclusterId));
		return map;
	}

	@Override
	public List<HclusterModel> selectByName(Map<String, String> map) {
		// TODO Auto-generated method stub
		return hclusterDao.selectByName(map);
	}

	@Override
	public List<HclusterModel> isExistHostOnHcluster(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.hclusterDao.isExistHostOnHcluster(map);
	}
	public List<HclusterModel> selectHclusterByStatus(HclusterModel hclusterModel){
		return this.hclusterDao.selectHclusterByStatus(hclusterModel);
	}
	public void updateStatus(HclusterModel hclusterModel){
		this.hclusterDao.update(hclusterModel);
	}

	@Override
	public Integer selectCountByType(String type) {
		return this.hclusterDao.selectCountByType(type);
	}
}
