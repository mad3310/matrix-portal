package com.letv.portal.controller.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.letv.common.result.ResultObject;
import com.letv.common.session.SessionServiceImpl;
import com.letv.portal.proxy.ILoginProxy;
import com.letv.portal.service.impl.oauth.IOauthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class LoginController {

	private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SessionServiceImpl sessionService;
	@Autowired
	private IOauthService oauthService;
	
	@Autowired
	private ILoginProxy loginProxy;

	@Value("${oauth.auth.http}")
	private String OAUTH_AUTH_HTTP;
	@Value("${webportal.api.http}")
	private String WEBPORTAL_API_HTTP;
	
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

        if(StringUtils.isEmpty(code) && !StringUtils.isEmpty(clientId)) {
            OAuthClientRequest req = OAuthClientRequest
                    .authorizationLocation(OAUTH_AUTH_HTTP+"/authorize")
                    .setClientId(clientId)
                    .setRedirectURI(WEBPORTAL_API_HTTP+"/oauth/callback?client_id="+clientId+"&clientSecret"+clientSecret)
                    .buildQueryMessage();
            mav.setViewName(req.getLocationUri());
            return mav;
        }
        clientId = request.getParameter("client_id");
        clientSecret = request.getParameter("client_secret");
		code = StringUtils.isEmpty(code)?request.getParameter("code"):code;
        if(StringUtils.isEmpty(clientId) || StringUtils.isEmpty(code)) {
            logger.info("login failed by client_id:{},client_secret:{}.",clientId,clientSecret);
            responseJson(request, response, "login failed by client_id:{"+clientId+"},client_secret:"+clientSecret);
        }
		String accessToken = this.oauthService.getAccessToken(clientId, clientSecret, code, WEBPORTAL_API_HTTP);

		if(StringUtils.isEmpty(accessToken)) {
			logger.info("login failed by client_id:{},client_secret:{}.",clientId,clientSecret);
			responseJson(request, response, "login failed by client_id:{"+clientId+"},client_secret:"+clientSecret);
		}
		responseJson(request, response, "login failed by client_id:{"+clientId+"},client_secret:"+clientSecret);
		return  null;
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
