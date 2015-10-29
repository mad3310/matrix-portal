package com.letv.portal.controller.cloudgce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.letv.portal.model.gce.GceServer;
import com.letv.portal.service.gce.IGceServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.exception.ValidateException;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.model.DbModel;
import com.letv.portal.service.IDbService;

/**Program Name: SkipController <br>
 * Description:  用于页面跳转       list、detail、form、……<br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年10月8日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Controller("gceSkip")
public class SkipController {
	@Autowired(required=false)
	private SessionServiceImpl sessionService;
	@Autowired
	private IGceServerService gceServerService;
	
	/**
	 * Methods Name: gceList<br>
	 * Description: 跳转gce列表
	 * @author name: yaokuo
	 * @param mav
	 * @return
	 */
	@RequestMapping(value ="/list/gce",method=RequestMethod.GET)
	public ModelAndView toSlbList(ModelAndView mav){
		mav.setViewName("/cloudgce/gceList");
		return mav;
	}
	
	/**Methods Name: gceDetail <br>
	 * Description: 跳转至gce详情<br>
	 * @author name: yaokuo
	 * @param gceId
	 * @param mav
	 * @return
	 */
	@RequestMapping(value ="/detail/gce/{gceId}",method=RequestMethod.GET)
	public ModelAndView gceDetail(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId",gceId);
		mav.setViewName("/cloudgce/layout");
		return mav;
	}
	
	/**
	 * Methods Name: gceCreate<br>
	 * Description: 跳转gce创建页面
	 * @author name: yaokuo
	 * @param mav
	 * @return
	 */
	@RequestMapping(value ="/detail/gceCreate",method=RequestMethod.GET)
	public ModelAndView toGceCreate(ModelAndView mav){
		mav.setViewName("/cloudgce/gceCreate");
		return mav;
	}
	
	@RequestMapping(value ="/detail/gceBaseInfo/{gceId}",method=RequestMethod.GET)
	public ModelAndView toGceCreate(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId",gceId);
		mav.setViewName("/cloudgce/baseInfo");
		return mav;
	}
	
	@RequestMapping(value ="/monitor/gce/cpu/{gceId}",method=RequestMethod.GET)
	public ModelAndView toCpuUsed(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId",gceId);
		mav.setViewName("/cloudgce/monitor/cpu");
		return mav;
	}
	@RequestMapping(value ="/monitor/gce/network/{gceId}",method=RequestMethod.GET)
	public ModelAndView toNetwork(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId",gceId);
		mav.setViewName("/cloudgce/monitor/network");
		return mav;
	}
	@RequestMapping(value ="/monitor/gce/memory/{gceId}",method=RequestMethod.GET)
	public ModelAndView toMemory(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId", gceId);
		mav.setViewName("/cloudgce/monitor/memory");
		return mav;
	}
	@RequestMapping(value ="/monitor/gce/disk/{gceId}",method=RequestMethod.GET)
	public ModelAndView toDisk(@PathVariable Long gceId,ModelAndView mav){
		isAuthorityGce(gceId);
		mav.addObject("gceId",gceId);
		mav.setViewName("/cloudgce/monitor/disk");
		return mav;
	}
	
	private void isAuthorityGce(Long gceId) {
		if(gceId == null)
			throw new ValidateException("参数不合法");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", gceId);
		map.put("createUser", sessionService.getSession().getUserId());
		List<GceServer> gceServers = this.gceServerService.selectByMap(map);
		if(gceServers == null || gceServers.isEmpty())
			throw new ValidateException("参数不合法");
	}
}
