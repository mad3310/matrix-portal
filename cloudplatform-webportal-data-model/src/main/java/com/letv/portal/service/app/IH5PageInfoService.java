package com.letv.portal.service.app;

import java.util.Map;

import com.letv.portal.model.app.H5PageInfoModel;
import com.letv.portal.service.IBaseService;

public interface IH5PageInfoService extends IBaseService<H5PageInfoModel> {
	/**
	  * @Title: getH5PageInfos
	  * @Description: 获取所有的h5页面
	  * @return Map<String, Object>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:15:09
	  */
	Map<String, Object> getH5PageInfos();
	/**
	  * @Title: getH5Urls
	  * @Description: 根据pageId查询各个业务类型的h5-url
	  * @return Map<String,String>   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月19日 下午2:15:41
	  */
	Map<String, String> getH5Urls();
}
