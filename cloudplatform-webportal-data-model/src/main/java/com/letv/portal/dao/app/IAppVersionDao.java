package com.letv.portal.dao.app;

import java.util.Map;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.app.AppVersionModel;

public interface IAppVersionDao extends IBaseDao<AppVersionModel> {

	/**
	  * @Title: getLastestVersionInfo
	  * @Description: 根据平台类型获取最新app版本信息
	  * @param params
	  * @return AppVersionModel   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:11:38
	  */
	AppVersionModel getLastestVersionInfo(Map<String, Object> params);
}	
