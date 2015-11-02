package com.letv.portal.service.impl.oauth;

import java.util.Map;


/**Program Name: OauthLoginApi <br>
 * Description:  提供基础的oauth api服务<br>
 * @author name: howie <br>
 * Written Date: 2015年7月15日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public interface IOauthService {
	
	String getAccessToken(String clientId, String clientSecret, String code);
	Map<String,Object> getUserdetailinfo(String accessToken);
	String getAuthorize(String clientId);
	Map<String,Object> getUserdetailinfo(String clientId,String clientSecret);

}