package com.letv.portal.service.oauth;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.letv.common.exception.OauthException;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.HttpsClient;
import com.letv.mms.cache.ICacheService;
import com.letv.mms.cache.factory.CacheFactory;
import com.letv.portal.proxy.ILoginProxy;
import com.letv.portal.service.ILoginService;
import com.letv.portal.service.impl.oauth.IOauthService;
import com.mysql.jdbc.StringUtils;


/**Program Name: OauthLoginApi <br>
 * Description:  提供基础的oauth api服务<br>
 * @author name: howie <br>
 * Written Date: 2015年7月15日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@Service("oauthService")
public class OauthServiceImpl  implements IOauthService{
	
	public final static Logger logger = LoggerFactory.getLogger(OauthServiceImpl.class);
	public final static int OAUTH_API_RETRY_COUNT = 3;
	
	@Autowired
	public ILoginService loginManager;
	@Autowired
	public SessionServiceImpl sessionService;
	
	private ICacheService<?> cacheService = CacheFactory.getCache();
	
	@Autowired
	public ILoginProxy loginProxy;

	@Value("${oauth.auth.http}")
	public String OAUTH_AUTH_HTTP;
	@Value("${oauth.token.cache.expire}")
	public long OAUTH_TOKEN_CACHE_EXPIRE;
	
	public String retryOauthApi(String result,String url){
		int i = 1;
		while(StringUtils.isNullOrEmpty(result) && i<=OAUTH_API_RETRY_COUNT) {
			result = HttpsClient.sendXMLDataByGet(url,1000,1000);
			i++;
		}
		return result;
	}
	
	@Override
	public String getAccessToken(String clientId,String clientSecret,String code) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/accesstoken?grant_type=authorization_code&code=").append(code).append("&client_id=").append(clientId).append("&client_secret=").append(clientSecret);
		logger.debug("getAccessToken :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString(),1000,1000);
		retryOauthApi(result,buffer.toString());
		if(StringUtils.isNullOrEmpty(result))
			throw new OauthException("长时间未操作，请重新登录");
		Map<String,Object> resultMap = this.transResult(result);
		return (String) resultMap.get("access_token");
	}
	
	@Override
	public Map<String,Object> getUserdetailinfo(String accessToken) {
		
		Map<String,Object>  resultMap = (Map<String, Object>) this.cacheService.get(accessToken, null);
		
		if(resultMap != null) 
			return resultMap;
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/userdetailinfo?access_token=").append(accessToken);
		logger.debug("getUserdetailinfo :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString(),1000,1000);
		retryOauthApi(result,buffer.toString());
		if(StringUtils.isNullOrEmpty(result))
			throw new OauthException("长时间未操作，请重新登录");
		resultMap = this.transResult(result);
		if(resultMap !=null)
			this.cacheService.set(accessToken, resultMap,OAUTH_TOKEN_CACHE_EXPIRE);
		
		return resultMap;
	}

	@Override
	public String getAuthorize(String clientId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(OAUTH_AUTH_HTTP).append("/authorizenoredirect?response_type=code&client_id=").append(clientId);
		logger.debug("getAuthorize :" + buffer.toString());
		String result = HttpsClient.sendXMLDataByGet(buffer.toString(),1000,1000);
		retryOauthApi(result,buffer.toString());
		if(StringUtils.isNullOrEmpty(result))
			throw new OauthException("长时间未操作，请重新登录");
		Map<String,Object> resultMap = this.transResult(result);
		return (String) resultMap.get("code");
	}

	@Override
	public Map<String, Object> getUserdetailinfo(String clientId, String clientSecret) {
		return getUserdetailinfo(getAccessToken(clientId,clientSecret,getAuthorize(clientId)));
	}

	public Map<String,Object> transResult(String result){
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String,Object> jsonResult = new HashMap<String,Object>();
		try {
			jsonResult = resultMapper.readValue(result, Map.class);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
}