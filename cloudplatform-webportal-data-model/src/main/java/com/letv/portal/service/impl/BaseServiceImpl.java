/*
 * @Title: BaseServiceImpl.java
 * @Package com.letv.mms.service.impl
 * @Description: TODO
 * @author xufei1 <xufei1@letv.com>
 * @date 2012-11-22 上午11:02:50
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-11-22                          
 */
package com.letv.portal.service.impl;

import java.util.List;
import java.util.Map;

import com.letv.common.exception.ValidateException;
import com.letv.portal.dao.IBaseDao;
import com.letv.portal.model.ISoftDelete;
import com.letv.portal.service.IBaseService;

/**
 * <p>
 * </p>
 * 
 * @author xufei1 <xufei1@letv.com> Create at:2012-11-22 上午11:02:50
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T>{
	
	protected final Class<T> entityClass;
	
	private final boolean isSoftDelete;
	
	public BaseServiceImpl(Class<T> entityClass)
	{
		this.entityClass = entityClass;
		
		isSoftDelete = ISoftDelete.class.isAssignableFrom(this.entityClass);
	}

	@Override
	public void insert(T t) {
		getDao().insert(t);
	}

	@Override
	public void update(T t) {
		getDao().update(t);
	}

	@Override
	public void updateBySelective(T t) {
		getDao().updateBySelective(t);
	}

	@Override
	public void delete(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			if (!softDeleteObject.isDeleted())
				softDeleteObject.setDeleted(true);
		}
		
		getDao().delete(t);
	}

	@Override
	public T selectById(String id) {
		if (null == id) 
			throw new ValidateException("select操作中，id不可为空！");
		
		T t = getDao().selectById(id);
		return t;
	}

	@Override
	public Integer selectByModelCount(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			softDeleteObject.setDeleted(false);
		}
		
		Integer countNum = getDao().selectByModelCount(t);
		return countNum;
	}

	@Override
	public <K,V> Integer selectByMapCount(Map<K,V> map) {
		Integer countNum = getDao().selectByMapCount(map);
		return countNum;
	}

	@Override
	public List<T> selectByModel(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			softDeleteObject.setDeleted(false);
		}
		
		List<T> lists = getDao().selectByModel(t);
		return lists;
	}

	@Override
	public <K,V> List<T> selectByMap(Map<K,V> map) {
		List<T> lists = getDao().selectByMap(map);
		return lists;
	}
	
	@Override
	public final boolean hasSoftDelete() {
		return isSoftDelete;
	}
	
	public abstract IBaseDao<T> getDao();
}
