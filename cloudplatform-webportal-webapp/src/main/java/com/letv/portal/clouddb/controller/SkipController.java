package com.letv.portal.clouddb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.exception.ValidateException;
import com.letv.common.result.ResultObject;
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
@Controller
public class SkipController {
	
	@Autowired(required=false)
	private SessionServiceImpl sessionService;
	@Autowired
	private IDbService dbService;
	
	@RequestMapping(value ="/dashboard",method=RequestMethod.GET)
	public ModelAndView toDashboard(ModelAndView mav,HttpServletRequest request){
		mav.setViewName("/clouddb/dashboard");
		return mav;
	}
	
	@RequestMapping(value ="/list/mcluster",method=RequestMethod.GET)
	public String toMclusterList(HttpServletRequest request){
		return "/clouddb/mclsuter_list";
	}

	@RequestMapping(value="/detail/mcluster/{mclusterId}", method=RequestMethod.GET)   
	public ModelAndView toMclusterDetail(@PathVariable Long mclusterId,ModelAndView mav) {
		mav.addObject("mclusterId",mclusterId);
		mav.setViewName("/clouddb/mcluster_detail");
		return mav;
	}

	/**
	 * Methods Name: toList <br>
	 * Description: db跳转
	 * @author name: wujun
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/list/db",method=RequestMethod.GET)
	public ModelAndView toDbList(ModelAndView mav){
		mav.setViewName("/clouddb/db_list");
		return mav;
	}
	
	/**
	 * Methods Name: toList <br>
	 * Description: dbDetail跳转
	 * @author name: wujun
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value ="/detail/db/{dbId}",method=RequestMethod.GET)
	public ModelAndView toDbDetail(@PathVariable Long dbId,ModelAndView mav){
		DbModel db = this.dbService.selectById(dbId);
		if(db!=null && db.getCreateUser() == sessionService.getSession().getUserId()) {
			mav.addObject("dbId",dbId);
			mav.setViewName("/clouddb/db_detail");
		} else {
			throw new ValidateException("用户无相关数据");
		}
		return mav;
	}
	
	@RequestMapping(value ="/jettyMonitor",method=RequestMethod.GET)
	public @ResponseBody ResultObject jettyMonitor(ResultObject obj){
		return obj;
	}
}
