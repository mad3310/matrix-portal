package com.letv.portal.dao.app;

import java.util.Map;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.app.AppSuggestionModel;

public interface IAppSuggestionDao extends IBaseDao<AppSuggestionModel> {

	/**
	  * @Title: saveSuggestion
	  * @Description: 保存建议
	  * @param params void   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:11:25
	  */
	void saveSuggestion(Map<String, Object> params);
}	
