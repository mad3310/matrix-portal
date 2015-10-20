package com.letv.portal.controller.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.common.util.StringUtil;
import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.UserModel;
import com.letv.portal.service.IUserService;
import com.letv.portal.service.app.IAppService;
import com.letv.portal.service.app.IH5PageInfoService;
@Controller
@RequestMapping("/app")
public class AppController {
	
	@Autowired
	private IH5PageInfoService h5PageInfoService;
	@Autowired
	private IAppService appService;
	@Autowired
	private IUserService userService;
	
	
	/**
	  * @Title: h5PageList
	  * @Description: 获取所有h5页面
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月19日 下午4:01:14
	  */
	@RequestMapping(value ="/h5models.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject h5PageList(Page page,HttpServletRequest request,ResultObject obj) {
		obj.setData(this.h5PageInfoService.getH5PageInfos());
		return obj;
	}
	
	/**
	  * @Title: homeInfo
	  * @Description: 获取主页相关数据(RDS监控异常、待审核数据库、任务流异常)
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月19日 下午4:01:37
	  */
	@RequestMapping(value ="/homeInfo.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject homeInfo(Page page,HttpServletRequest request,ResultObject obj) {
		obj.setData(this.appService.getHomeInfo());
		return obj;
	}
	/**
	  * @Title: getRdsFault
	  * @Description: RDS监控异常
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月22日 上午6:48:39
	  */
	@RequestMapping(value ="/rdsFault.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject getRdsFault(Page page,HttpServletRequest request,ResultObject obj) {
		Page p = this.appService.getRdsFault(page);
		if(p.getTotalRecords()==0) {
			obj.setAlertMessage("获取数据超时");
			obj.addMsg("appService.getRdsFault have no result");
			obj.setResult(0);
		}
		obj.setData(p);
		return obj;
	}
	/**
	  * @Title: getPendingAuditDb
	  * @Description: 获取待审核数据库
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月22日 上午6:49:02
	  */
	@RequestMapping(value ="/dbChecker.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject getPendingAuditDb(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData(this.appService.getPendingAuditDb(page, params));
		return obj;
	}
	
	/**
	  * @Title: checkUpdate
	  * @Description: 根据平台类型查询最新版本信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:19:49
	  */
	@RequestMapping(value ="/checkUpdate.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject checkUpdate(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		Map<String, Object> ret = this.appService.checkUpdate(params);
		if(ret == null) {
			obj.setResult(0);
			obj.setAlertMessage("未获取到最新版本");
			obj.addMsg("appVersionService.getLastestVersionInfo have no result");
		} else {
			obj.setData(ret);
		}
		return obj;
	}
	/**
	  * @Title: getMessages
	  * @Description: 获取通知消息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月22日 上午7:02:07
	  */
	@RequestMapping(value ="/messages.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject getMessages(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData(this.appService.getMessages(page, params));
		return obj;
	}
	@RequestMapping(value ="/editUserInfo.do",method=RequestMethod.POST)   
	public @ResponseBody ResultObject editUserInfo(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		this.appService.editUserInfo(params);
		obj.addMsg("返回成功");
		obj.setAlertMessage("设置成功");
		return obj;
	}
	
	/**
	  * @Title: reportSuggestion
	  * @Description: 保存反馈建议信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 上午10:20:07
	  */
	@RequestMapping(value ="/reportsuggestion.do",method=RequestMethod.POST)   
	public @ResponseBody ResultObject reportSuggestion(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		this.appService.reportSuggestion(params);
		obj.addMsg("返回成功");
		obj.setAlertMessage("感谢您的反馈");
		return obj;
	}
	/**
	  * @Title: mclusterFailedList
	  * @Description: 获取失败的mcluster信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 下午3:41:59
	  */
	@RequestMapping(value ="/mclusterFailedList",method=RequestMethod.GET)   
	public @ResponseBody ResultObject mclusterFailedList(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		if(params !=null && params.containsKey("ipAddr")) {
			params.put("ipAddr", StringUtil.transSqlCharacter((String)params.get("ipAddr")));
		}
		List<ContainerModel> containers = this.appService.failedList(params, 1l);
		if(containers == null) {
			obj.setResult(0);
			obj.setAlertMessage("获取cluster信息异常");
			obj.addMsg("appService.mclusterFailedList have no result");
		} else {
			obj.setData(containers);
		}
		return obj;
	}
	
	/**
	  * @Title: mclusterFailedList
	  * @Description: 获取失败的node信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 下午3:41:59
	  */
	@RequestMapping(value ="/nodeFailedList",method=RequestMethod.GET)   
	public @ResponseBody ResultObject nodeFailedList(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		if(params !=null && params.containsKey("ipAddr")) {
			params.put("ipAddr", StringUtil.transSqlCharacter((String)params.get("ipAddr")));
		}
		List<ContainerModel> containers = this.appService.failedList(params, 2l);
		if(containers == null) {
			obj.setResult(0);
			obj.setAlertMessage("获取node信息异常");
			obj.addMsg("appService.nodeFailedList have no result");
		} else {
			obj.setData(containers);
		}
		return obj;
	}
	/**
	  * @Title: mclusterFailedList
	  * @Description: 获取失败的db信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月20日 下午3:41:59
	  */
	@RequestMapping(value ="/dbFailedList",method=RequestMethod.GET)   
	public @ResponseBody ResultObject dbFailedList(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		if(params !=null && params.containsKey("ipAddr")) {
			params.put("ipAddr", StringUtil.transSqlCharacter((String)params.get("ipAddr")));
		}
		List<ContainerModel> containers = this.appService.failedList(params, 3l);
		if(containers == null) {
			obj.setResult(0);
			obj.setAlertMessage("获取db信息异常");
			obj.addMsg("appService.dbFailedList have no result");
		} else {
			obj.setData(containers);
		}
		return obj;
	}
	
	/**
	  * @Title: getUserInfo
	  * @Description: 根据用户名和邮箱获取用户信息
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年8月27日 上午9:18:38
	  */
	@RequestMapping(value ="/userInfo.do",method=RequestMethod.GET)   
	public @ResponseBody ResultObject getUserInfo(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		UserModel user = null;
		if(!StringUtils.isEmpty((String)params.get("userName")) && !StringUtils.isEmpty((String)params.get("email"))) {
			user = this.userService.getUserByNameAndEmail((String)params.get("userName"), (String)params.get("email"));
		}
		if(user == null) {
			obj.setResult(0);
			obj.setAlertMessage("获取用户信息异常");
			obj.addMsg("userService.getUserByNameAndEmail have no result：userName-"+(String)params.get("userName"));
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("userName", user.getUserName());
			map.put("email", user.getEmail());
			map.put("phone", user.getPhone());
			map.put("iconUrl", user.getIconUrl());
			obj.setData(map);
		}
		return obj;
	}
	
}
