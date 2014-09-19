package com.letv.portal.dao;

import java.util.List;
import java.util.Map;

import com.letv.common.dao.QueryParam;

public interface IBaseDao<T> {
	
	public void insert(T t);
	
	public void update(T t);
	
	public void updateBySelective(T t);
	
	public void delete(T t);
	
	public void deleteFlag(String id);
	
	public T selectById(String id);
	
	public Integer selectByModelCount(T  t);
	
	public <K,V> Integer selectByMapCount(Map<K,V>  map);
	
	public List<T> selectByModel(T t);
	
	public <K,V> List<T> selectByMap(Map<K,V>  map);
	
	
	/**Methods Name: selectPageByMap <br>
	 * Description: 获取分页数据<br>
	 * @author name: liuhao1
	 * @param params
	 * @return
	 */
	public List<Object> selectPageByMap(QueryParam params);
	
}
