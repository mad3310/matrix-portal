package com.letv.portal.service.app;

import java.util.Map;

import com.letv.portal.model.app.AppSuggestionModel;
import com.letv.portal.service.IBaseService;


public interface IAppSuggestionService extends IBaseService<AppSuggestionModel> {
	/**
	  * @Title: reportSuggestion
	  * @Description: 保存反馈建议
	  * @param params void   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:14:14
	  */
	void reportSuggestion(Map<String, Object> params);
}
