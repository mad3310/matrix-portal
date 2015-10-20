package com.letv.portal.service.app;

import java.util.Map;

import com.letv.portal.model.app.AppVersionModel;
import com.letv.portal.service.IBaseService;


public interface IAppVersionService extends IBaseService<AppVersionModel> {
	/**
	  * @Title: checkUpdate
	  * @Description: 根据平台类型获取最新app版本信息
	  * @param params
	  * @return Map<String,Object>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:13:39
	  */
	AppVersionModel getLastestVersionInfo(Map<String, Object> params);
}
