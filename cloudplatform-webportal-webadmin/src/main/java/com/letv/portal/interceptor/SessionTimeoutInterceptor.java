package com.letv.portal.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.letv.common.result.ResultObject;
import com.letv.portal.clouddb.controller.ContainerController;
import com.letv.portal.service.User;

/**
 * 处理session超时的拦截器
 */
public class SessionTimeoutInterceptor  implements HandlerInterceptor{
	private final static Logger logger = LoggerFactory.getLogger(SessionTimeoutInterceptor.class);
    
	public String[] allowUrls;//还没发现可以直接配置不拦截的资源，所以在代码里面来排除
	
	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");  
		if(null != allowUrls && allowUrls.length>=1)
			for(String url : allowUrls) {  
				if(requestUrl.contains(url)) {  
					return true;  
				}  
			}
		
		if(request.getSession().getAttribute("loginName") == null ) {
			logger.debug("please login");
			boolean isAjaxRequest = (request.getHeader("x-requested-with") != null)? true:false;
			
			if (isAjaxRequest) {
				responseJson(request,response,"长时间未操作，请重新登录");
			} else {
				response.sendRedirect("/account/login");
			}
			return false;
		}
		return true;
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}
	
	private void responseJson(HttpServletRequest req, HttpServletResponse res, String message) {
    	PrintWriter out = null;
		try {
			res.setContentType("text/html;charset=UTF-8");
			out = res.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ResultObject resultObject = new ResultObject(0);
		resultObject.addMsg(message);
		out.append(JSON.toJSONString(resultObject, SerializerFeature.WriteMapNullValue));
		out.flush();
	}

}