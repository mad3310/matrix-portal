package com.letv.portal.clouddb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysql.jdbc.StringUtils;


@Controller
@RequestMapping("/account")
public class AccountController {
	
	@RequestMapping("/login")   //http://localhost:8080/account/login
	public String login(HttpServletRequest request,HttpServletResponse response) {
		
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("password");
		if(StringUtils.isNullOrEmpty(loginName)) {
			return "/account/login";
		}
		
		if(!"sysadmin@letv.com".equals(loginName) ) {
			request.setAttribute("error", "用户名或密码错误！");
			return "/account/login";
		}
		
		if(!"000000".equals(password)) {
			request.setAttribute("error", "用户名或密码错误！");
			return "/account/login";
		}
		
		request.getSession().setAttribute("loginName", loginName);
		request.getSession().setAttribute("userId", loginName);
		request.getSession().setAttribute("role", "sysadmin");
		request.getSession().setMaxInactiveInterval(60*60*60*24);//单位：秒
		return "redirect:/mcluster/list";
		

	}
	@RequestMapping("/logout")   //http://localhost:8080/account/logout
	public String logout(HttpServletRequest request,HttpServletResponse response) {
	
		request.getSession().invalidate();
		return "redirect:/account/login";
	}
	

}
