package com.letv.portal.proxy.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.common.paging.impl.Page;
import com.letv.portal.proxy.IBaseProxy;
import com.letv.portal.service.IBaseService;

public abstract class BaseProxyImpl<T> implements IBaseProxy<T>{
	
	private final static Logger logger = LoggerFactory.getLogger(BaseProxyImpl.class);
	
	@Override
	public <K, V> Page selectPageByParams(int currentPage,int recordsPerPage,Map<K, V> params) {
		return selectPageByParams(currentPage, recordsPerPage, params, null, null);
	}
	@Override
	public <K, V> Page selectPageByParams(int currentPage,int recordsPerPage,Map<K, V> params,String orderBy,Boolean isAsc) {
		Page page = new Page(currentPage,recordsPerPage);
		return getService().selectPageByParams(page,params,orderBy,isAsc);
	}
	
	@Override
	public void insert(T t) {
		getService().insert(t);
	}
	@Override
	public void update(T t) {
		getService().update(t);
		
	}
	@Override
	public void updateBySelective(T t) {
		getService().updateBySelective(t);
		
	}
	@Override
	public void delete(T t) {
		getService().delete(t);
	}
	@Override
	public T selectById(Long id) {
		return getService().selectById(id);
	}
	@Override
	public Integer selectByModelCount(T t) {
		return getService().selectByModelCount(t);
	}
	@Override
	public <K, V> Integer selectByMapCount(Map<K, V> map) {
		return getService().selectByMapCount(map);
	}
	@Override
	public List<T> selectByModel(T t) {
		return getService().selectByModel(t);
	}
	@Override
	public <K, V> List<T> selectByMap(Map<K, V> map) {
		return  getService().selectByMap(map);
	}
	
	
	
	/**Methods Name: getService <br>
	 * Description: 获取对应service<br>
	 * @author name: liuhao1
	 * @return
	 */
	public abstract IBaseService<T> getService();

}
