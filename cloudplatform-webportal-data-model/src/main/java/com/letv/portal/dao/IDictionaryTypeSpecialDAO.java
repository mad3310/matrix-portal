/*
 * @Title: IDictionaryTypeSpecialDAO.java
 * @Package com.letv.mms.dao
 * @Description: 字典分类信息的分页查询
 * @author 陈光
 * @date 2012-12-5 下午5:50:59
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

public interface IDictionaryTypeSpecialDAO extends IBaseSpecialDAO{
	//字典分类信息的分页查询
	public List<Map<String,Object>> getDicWithList(Map<String,Object> map,IPage page);
	
}
