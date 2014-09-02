/*
 * @Title: DictionarySpecialDAOImpl.java
 * @Package com.letv.mms.dao.impl
 * @Description: 字典表数据信息的分页查询
 * @author 陈光
 * @date 2012-12-5 下午4:55:32
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5                          
 */
package com.letv.portal.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.letv.common.dao.impl.BaseSpecialDAOByMybatisImpl;
import com.letv.common.paging.IPage;
import com.letv.portal.dao.IDictionarySpecialDAO;

@Service("dictionarySpecialDao")
public class DictionarySpecialDAOImpl extends BaseSpecialDAOByMybatisImpl
		implements IDictionarySpecialDAO {
	/**
	 * 根据分页对象和查询条件进行分页查询
	 */
	@Override
	public List<Map<String, Object>> getDicWithList(Map<String, Object> map,
			IPage page) {
		if (null == map || map.isEmpty()){
			map = null;
		}
		List<Map<String, Object>> list = queryByPageWithList(
				"Self_DictionaryMapper.getDicAndItemWithList", map, page);
		return list;
	}
}