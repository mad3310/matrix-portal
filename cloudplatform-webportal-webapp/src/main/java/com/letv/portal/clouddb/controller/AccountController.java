package com.letv.portal.clouddb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.letv.common.util.ConfigUtil;


@Controller
@RequestMapping("/account")
public class AccountController {
	private final static Logger logger = LoggerFactory.getLogger(AccountController.class);
	private final static String CAS_AUTH_HTTP = ConfigUtil.getString("cas.auth.http");
	private final static String CAS_LOCAL_HTTP = ConfigUtil.getString("cas.local.http");
	
	//集成cas登录方案
	/*@RequestMapping("/login")   //http://localhost:8080/account/login
	public String login(HttpServletRequest request,HttpServletResponse response) {
		
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("password");
		if(StringUtils.isNullOrEmpty(loginName)) {
			return "/account/login";
		}
		
		if(!"yaokuo@letv.com".equals(loginName) ) {
			request.setAttribute("error", "用户名或密码错误！");
			return "/account/login";
		}
		
		if(!"000000".equals(password)) {
			request.setAttribute("error", "用户名或密码错误！");
			return "/account/login";
		}
		
		request.getSession().setAttribute("username", loginName);
		request.getSession().setAttribute("userId", loginName);
		request.getSession().setAttribute("role", "user");
		return "redirect:/db/list";

	}*/
	/**Methods Name: logout <br>
	 * Description: 用户登出<br>
	 * @author name: liuhao1
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)   //http://localhost:8080/account/logout
	public String logout(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().invalidate();
		String redirect = "redirect:" + CAS_AUTH_HTTP + "/cas/logout?service=" + CAS_LOCAL_HTTP;
		logger.debug("logout redirect==>" + redirect);
		return redirect;
	}
	
	

}
