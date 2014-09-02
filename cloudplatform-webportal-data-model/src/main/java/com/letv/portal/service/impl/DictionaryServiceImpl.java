/*
 * @Title: DictionaryServiceImpl.java
 * @Package com.letv.mms.service.impl
 * @Description: 字典数据信息接口实现类
 * @author 陈光
 * @date 2012-12-5 下午6:01:31
 * @version V1.0
 *
 * Modification History:  
 * Date                Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5         
 * 2012-12-14      陈光             V1.1        1. 更改初始化缓存数据类型,目前只保存 
 *                                             			     {前缀+id:Molde} 这种类型的数据
 *                                             			  2.更改根据字典类型查询数据的实现逻辑
 *                                           			  3.实现IDBOperationService接口定义的方法
 *                 
 */
package com.letv.portal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.letv.common.exception.ValidateException;
import com.letv.common.paging.IPage;
import com.letv.common.paging.impl.Page;
import com.letv.common.util.CommonUtil;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.dao.IDictionaryDao;
import com.letv.portal.dao.IDictionarySpecialDAO;
import com.letv.portal.model.DictionaryModel;
import com.letv.portal.service.IDictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl<DictionaryModel> implements
		IDictionaryService{
	
	private int CACHE_CURRPAGE = 1;
			
	private int CACHE_PAGESIZE = 100;
	
	@Resource
	private IDictionaryDao dictionaryDao;
	@Resource
	private IDictionarySpecialDAO dictionarySpecialDao;
	
	public DictionaryServiceImpl() {
		super(DictionaryModel.class);
	}
	
	/**
	 * 数据库中插入数据
	 */
	@Override
	public void insert(DictionaryModel model) {
		getDao().insert(model);
	}
	/**
	 * 根据model更新数据库中数据
	 */
	@Override
	public void update(DictionaryModel model) {
		getDao().update(model);
	}
	/**
	 * 根据model中字段更新数据库中数据
	 */
	@Override
	public void updateBySelective(DictionaryModel model) {
		getDao().updateBySelective(model);
	}
	/**
	 * 根据ID删除数据
	 */
	@Override
	public void delete(DictionaryModel model) {
		getDao().delete(model);
	}
	/**
	 * 根据ID查询数据库中数据 先在缓存中取,缓存中没有去数据库中查询 将查询到数据同步更新到缓存中
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DictionaryModel selectById(String id) {
		if(null == id){
			throw new ValidateException("字典ID值不能为空!");
		}
		DictionaryModel model =null;
		return model;
	}
	/**
	 * 根据数据model中的条件查询符合条件的数据总数
	 */
	@Override
	public Integer selectByModelCount(DictionaryModel model) {
		Integer count = 0;
		List<DictionaryModel> list = selectByModel(model);
		if(null != list && list.size()>0){
			count = list.size();
		}else{
			count = getDao().selectByModelCount(model);
		}
		return count;
	}
	/**
	 * 根据条件集合查询符合条件的记录总数
	 */
	@Override
	public <K,V> Integer selectByMapCount(Map<K,V> map) {
		DictionaryModel model = CommonUtil.convertMapToBean(DictionaryModel.class, map);
		Integer count = selectByModelCount(model);
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictionaryModel> selectByModel(DictionaryModel model) {
		List<DictionaryModel> list = new ArrayList<DictionaryModel>();
		List<DictionaryModel> resultList = new ArrayList<DictionaryModel>();
		//如果查询条件为null 返回全部数据
		if(null == model){
			resultList = list;
		}else{
			/**
			 * 根据传入的model进行过滤获得结果集合
			 */
			for (DictionaryModel dicModel : list) {
				if(CommonUtil.isFitModel(dicModel, model)){
					resultList.add(dicModel);
				}
			}
		}
		return resultList;
	}
	/**
	 * 根据条件map查询数据集合
	 */
	@Override
	public <K,V> List<DictionaryModel> selectByMap(Map<K,V> map) {
		DictionaryModel model = CommonUtil.convertMapToBean(DictionaryModel.class, map);
		List<DictionaryModel> list =selectByModel(model);
		return list;
	}

	/**
	 * 根据字典分类查询数据集合
	 */
	public List<DictionaryModel> getDicInfoListByDictype(int typeId) {
		List<DictionaryModel> list = new ArrayList<DictionaryModel>();
		DictionaryModel model = new DictionaryModel();
		model.setTypeId(typeId);
		list = selectByModel(model);
		return list;
	}

	/**
	 * 初始化数据到缓存中
	 */
	@Override
	public void initDicInfoDataToCache() {
		loadData();
	}
	
	@Override
	public IBaseDao<DictionaryModel> getDao() {
		return dictionaryDao;
	}
	/**
	 * 根据ID 从数据库查询数据,将查到的数据存放到缓存中
	 */
	@Override
	public DictionaryModel getDataFromDbByKey(String key) {
		DictionaryModel model = null;
		String modelId = key;
		System.out.println("1");
		System.out.println("1");
		model = dictionaryDao.selectById(modelId);
		return model;
	}
	/**
	 * 加载数据到缓存
	 */
	@Override
	public List<DictionaryModel> loadData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<DictionaryModel> resultList = new ArrayList<DictionaryModel>();
		IPage page = new Page();
		page.setCurrentPage(CACHE_CURRPAGE);
		page.setRecordsPerPage(CACHE_PAGESIZE);
		do {
			list = dictionarySpecialDao.getDicWithList(null, page);
			for (Map<String, Object> map : list) {
				DictionaryModel model = CommonUtil.convertMapToBean(DictionaryModel.class, map);
				resultList.add(model);
			}
			page.setCurrentPage(page.getCurrentPage() + 1);
		} while (page.getCurrentPage() <= page.getTotalPages());
		return resultList;
	}
}
