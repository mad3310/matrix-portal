/*
 * @Title: IDictionaryService.java
 * @Package com.letv.mms.service
 * @Description: 定义字典服务相关接口 
 * @author 陈光 
 * @date 2012-12-5 下午5:57:02
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5                          
 */
package com.letv.portal.service;

import java.util.List;

import com.letv.portal.model.DictionaryModel;

public interface IDictionaryService extends IBaseService<DictionaryModel> {
	/**
	 * 根据分类ID查询字典数据信息
	 * 
	 * @param type
	 * @return
	 */
	public List<DictionaryModel> getDicInfoListByDictype(int type);

	/**
	 * 初始化数据到缓存
	 */
	public void initDicInfoDataToCache();
	
	
	public DictionaryModel getDataFromDbByKey(String key);
	
	public List<DictionaryModel> loadData();
}
