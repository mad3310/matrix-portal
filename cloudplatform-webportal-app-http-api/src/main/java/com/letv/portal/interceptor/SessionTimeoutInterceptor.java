package com.letv.portal.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.letv.common.util.IpUtil;
import com.letv.portal.model.UserLogin;
import com.letv.portal.service.impl.oauth.IOauthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.letv.common.result.ResultObject;
import com.letv.common.session.Executable;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.ConfigUtil;
import com.letv.portal.proxy.ILoginProxy;

/**
 * 处理session超时的拦截器
 */
@Component
public class SessionTimeoutInterceptor  implements HandlerInterceptor{
	private final static Logger logger = LoggerFactory.getLogger(SessionTimeoutInterceptor.class);
	
	@Autowired(required=false)
	private SessionServiceImpl sessionService;
	@Autowired
	private IOauthService oauthService;
	@Autowired
	private ILoginProxy loginProxy;

	public String[] allowUrls;

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}

	private boolean allowUrl(HttpServletRequest request) {
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		if("/".equals(requestUrl)) {
			return true;
		}
		//特殊url过滤
		if(null != allowUrls && allowUrls.length>=1) {
			for(String url : allowUrls) {
				if(requestUrl.contains(url)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {

		if(allowUrl(request))
			return true;

		String clientId = request.getHeader("client_id");
		String clientSecret = request.getHeader("client_secret");
		if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret))
			return false;

		Session session = login(clientId,clientSecret,request);

		if(session != null ) {
			sessionService.runWithSession(session, "Usersession changed", new Executable<Session>(){
				@Override
				public Session execute() throws Throwable {
					return null;
				}
			});
			return true;
		}

		//login failed
		logger.info("login failed by clientId:{},clientSecret:{}.",clientId,clientSecret);
		responseJson(request, response, "login failed by clientId:{"+clientId+"},clientSecret:{"+clientSecret+"}.");
		return false;
	}

	private Session login(String clientId, String clientSecret,HttpServletRequest request) {
		Map<String,Object> userDetailInfo = this.oauthService.getUserdetailInfo(clientId,clientSecret);

		if(userDetailInfo == null)
			return null;

		String username = (String) userDetailInfo.get("username");
		String email = (String) userDetailInfo.get("email");

		if(StringUtils.isEmpty(username))
			return null;

		UserLogin userLogin = new UserLogin();
		userLogin.setLoginName(username);
		userLogin.setLoginIp(IpUtil.getIp(request));
		userLogin.setEmail(email);
		Session session = this.loginProxy.saveOrUpdateUserAndLogin(userLogin);
		return null;
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