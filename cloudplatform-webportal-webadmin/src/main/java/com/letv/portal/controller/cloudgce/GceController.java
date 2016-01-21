package com.letv.portal.controller.cloudgce;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.letv.common.exception.ValidateException;
import com.letv.common.paging.impl.Page;
import com.letv.common.result.ResultObject;
import com.letv.common.util.HttpUtil;
import com.letv.portal.model.adminoplog.AoLogType;
import com.letv.portal.model.gce.GceImage;
import com.letv.portal.proxy.IGceProxy;
import com.letv.portal.service.IUserService;
import com.letv.portal.service.adminoplog.AoLog;
import com.letv.portal.service.adminoplog.ClassAoLog;
import com.letv.portal.service.gce.IGceImageService;
import com.letv.portal.service.gce.IGceServerService;

@ClassAoLog(module="GCE管理")
@Controller
@RequestMapping("/gce")
public class GceController {
	
	@Autowired
	private IGceImageService gceImageService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGceServerService gceServerService;
	@Autowired
	private IGceProxy gceProxy;
	
	private final static Logger logger = LoggerFactory.getLogger(GceController.class);

	@RequestMapping(value="/image",method=RequestMethod.GET)   
	public @ResponseBody ResultObject imageList(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData(this.gceImageService.selectPageByParams(page, params));
		return obj;
	}

	@AoLog(desc="创建GCE镜像",type=AoLogType.INSERT)
	@RequestMapping(value="/image",method=RequestMethod.POST)   
	public @ResponseBody ResultObject addImage(GceImage gceImage,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		this.gceImageService.insert(gceImage);
		return obj;
	}
	
	@AoLog(desc="删除GCE镜像",type=AoLogType.DELETE)
	@RequestMapping(value="/image/{id}",method=RequestMethod.DELETE)   
	public @ResponseBody ResultObject delImage(@PathVariable Long id,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		if(id == null)
			throw new ValidateException("参数不合法");
		GceImage gceImage = this.gceImageService.selectById(id);
		if(gceImage == null)
			throw new ValidateException("参数不合法");
		this.gceImageService.delete(gceImage);
		return obj;
	}

	@AoLog(desc="修改GCE镜像",type=AoLogType.UPDATE)
	@RequestMapping(value="/image/{id}",method=RequestMethod.POST)   
	public @ResponseBody ResultObject updateImage(@PathVariable Long id,GceImage gceImage,HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		if(id == null)
			throw new ValidateException("参数不合法");
		GceImage oldGceImage = this.gceImageService.selectById(id);
		if(oldGceImage == null)
			throw new ValidateException("参数不合法");
		this.gceImageService.updateBySelective(gceImage);
		return obj;
	}
	@AoLog(desc="上传GCE镜像",type=AoLogType.UPDATE)
	@RequestMapping(value="/image/push/{id}",method=RequestMethod.POST)
	public @ResponseBody ResultObject pushImage(@PathVariable Long id,String hclusterIds) {
		ResultObject obj = new ResultObject();
		if(null == id || StringUtils.isEmpty(hclusterIds))
			throw new ValidateException("参数不合法");
		this.gceImageService.pushImage(id,hclusterIds);
		obj.addMsg("镜像上传中，需1-2分钟后方可使用该镜像");
		return obj;
	}
	
	@RequestMapping(value="/user",method=RequestMethod.GET)   
	public @ResponseBody ResultObject gceAllUser(HttpServletRequest request) {
		ResultObject obj = new ResultObject();
		Map<String, Object> map = new HashMap<String, Object>();
		
		obj.setData(this.userService.selectByMap(map));
		return obj;
	}
	
	/**
	  * @Title: list
	  * @Description: 获取指定页数指定条数的gceServer列表
	  * @param page
	  * @param request
	  * @param obj
	  * @return ResultObject   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年6月26日 下午1:47:31
	  */
	@RequestMapping(method=RequestMethod.GET)   
	public @ResponseBody ResultObject list(Page page,HttpServletRequest request,ResultObject obj) {
		Map<String,Object> params = HttpUtil.requestParam2Map(request);
		obj.setData(this.gceServerService.selectPageByParams(page, params));
		return obj;
	}

	@AoLog(desc="重启GCE服务",type=AoLogType.RESTART)
	@RequestMapping(value="/restart",method=RequestMethod.POST)   
	public @ResponseBody ResultObject restart(Long id,ResultObject obj) {
		this.gceProxy.restart(id);
		return obj;
	}
	@AoLog(desc="启动GCE服务",type=AoLogType.START)
	@RequestMapping(value="/start",method=RequestMethod.POST)   
	public @ResponseBody ResultObject start(Long id,ResultObject obj) {
		this.gceProxy.start(id);
		return obj;
	}
	@AoLog(desc="停止GCE服务",type=AoLogType.STOP)
	@RequestMapping(value="/stop",method=RequestMethod.POST)   
	public @ResponseBody ResultObject stop(Long id,ResultObject obj) {
		this.gceProxy.stop(id);
		return obj;
	}
	@AoLog(desc="扩容GCE服务",type=AoLogType.UPDATE)
	@RequestMapping(value="/memory",method=RequestMethod.POST)   
	public @ResponseBody ResultObject capacity(@RequestParam Long id,@RequestParam int multiple,ResultObject obj) {
		this.gceProxy.capacity(id,multiple);
		return obj;
	}
	
}
