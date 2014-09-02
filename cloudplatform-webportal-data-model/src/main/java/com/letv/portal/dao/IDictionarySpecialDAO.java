/*
 * @Title: IDictionarySpecialDAO.java
 * @Package com.letv.mms.dao
 * @Description: 字典信息的分页查询
 * @author 陈光
 * @date 2012-12-5 下午5:13:35
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5                          
 */
package com.letv.portal.dao;

import java.util.List;
import java.util.Map;
import com.letv.common.dao.IBaseSpecialDAO;
import com.letv.common.paging.IPage;
public interface IDictionarySpecialDAO extends IBaseSpecialDAO{
	/**
	 * 根据条件和分页对象进行分页查询
	 * @param map
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getDicWithList(Map<String,Object> map,IPage page);
	
}
