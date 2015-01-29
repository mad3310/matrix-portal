package com.letv.portal.clouddb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.letv.common.session.Executable;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.ConfigUtil;
import com.letv.common.util.CookieUtil;
import com.letv.common.util.HttpsClient;
import com.letv.portal.model.UserLogin;
import com.letv.portal.proxy.ILoginProxy;
import com.letv.portal.service.ILoginService;
import com.mysql.jdbc.StringUtils;

@Controller
public class LoginController{
	
	private static String WEB_URL = "http://www.letv.com";
	
	private final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private ILoginService loginManager;
	@Autowired
	private SessionServiceImpl sessionService;
	
	@Autowired
	private ILoginProxy loginProxy;

	@Value("${oauth.auth.http}")
	private String OAUTH_AUTH_HTTP;
	@Value("${webportal.local.http}")
	private String WEBPORTAL_LOCAL_HTTP;
	
	/**Methods Name: login <br>
	 * Description: cas登录完成后，跳转到本页面做相关用户记录，然后跳转到业务界面<br>
	 * @author name: liuhao1
	 * @param request
	 * @param mav
	 * @return
	 */
	@RequestMapping("/oauth/callback")
	public ModelAndView afterlogin(HttpServletRequest request,HttpServletResponse response,ModelAndView mav) throws Exception {
		
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		String code = request.getParameter("code");
		
		if(StringUtils.isNullOrEmpty(code) && !StringUtils.isNullOrEmpty(clientId)) {
			CookieUtil.addCookie(response, "clientId", clientId, 10);
			CookieUtil.addCookie(response, "clientSecret", clientSecret, 10);
			StringBuffer buffer = new StringBuffer();
			buffer.append(OAUTH_AUTH_HTTP).append("/authorize?client_id=").append(clientId).append("&response_type=code&redirect_uri=").append(WEBPORTAL_LOCAL_HTTP).append("/oauth/callback");
			logger.debug("getAuthorize :" + buffer.toString());
			mav.setViewName("redirect:" + buffer.toString());
			return mav;
		} 
		clientId = StringUtils.isNullOrEmpty(clientId)?CookieUtil.getCookieByName(request, "clientId").getValue():clientId;
		clientSecret = StringUtils.isNullOrEmpty(clientSecret)?CookieUtil.getCookieByName(request, "clientSecret").getValue():clientSecret;
		code = StringUtils.isNullOrEmpty(code)?request.getParameter("code"):code;
		
		String accessToken = this.getAccessToken(clientId, clientSecret, code);
		String username = this.getUsername(accessToken);
		
		UserLogin userLogin = new UserLogin();
		userLogin.setLoginName(username);
		userLogin.setLoginIp(this.getIp(request));
		Session session = this.loginProxy.saveOrUpdateUserAndLogin(userLogin);
		session.setClientId(clientId);
		session.setClientSecret(clientSecret);
		
		request.getSession().setAttribute(Session.USER_SESSION_REQUEST_ATTRIBUTE, session);
		sessionService.runWithSession(session, "Usersession changed", new Executable<Session>(){
            @Override
            public Session execute() throws Throwable {
               return null;
            }
         });
        
		mav.setViewName("redirect:/dashboard");
		return mav;
	}
	
	
	private String getAuthorize(String clientId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/authorize?client_id=").append(clientId).append("&response_type=code&redirect_uri=").append(WEBPORTAL_LOCAL_HTTP).append("/oauth/callback");
		logger.debug("getAuthorize :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString());
		Map<String,Object> resultMap = this.transResult(result);
		return (String) resultMap.get("code");
	}
	private String getAccessToken(String clientId,String clientSecret,String code) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/accesstoken?grant_type=authorization_code&code=").append(code).append("&client_id=").append(clientId).append("&client_secret=").append(clientSecret).append("&redirect_uri=").append(WEBPORTAL_LOCAL_HTTP).append("/oauth/callback");
		logger.debug("getAccessToken :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString());
		Map<String,Object> resultMap = this.transResult(result);
		return (String) resultMap.get("access_token");
	}
	private String getUsername(String accessToken) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/userinfo?access_token=").append(accessToken);
		logger.debug("getUsername :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString());
		return result;
	}
	
	private Map<String,Object> transResult(String result){
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String,Object> jsonResult = new HashMap<String,Object>();
		try {
			jsonResult = resultMapper.readValue(result, Map.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	/**
     * 从HttpServletRequest实例中获取IP地址
     * 
     * @return 请求方IP地址
     */
	public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") > 0) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }
	
}
