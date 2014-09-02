package com.letv.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.client.RestTemplate;
/**
 * 从远程接口读取json数据或xml数据用于解析。
 * readContent();方法，从url地址读取字符串，返回。
 * 具备GZIP解析功能.<br>
 * @author zw
 * 
 */
public final class HttpUtil {

	private static final Log log = LogFactory.getLog(HttpUtil.class);
	
	public static final String API_URL = ConfigUtil.getString("server_api_url");

	public static final String readContent(String url) {
		
		String content="";
	    int count=0;
		while(true){
			count++;
			try{
			    content=readContend(url, 800, 800);
			}catch(Exception e){
				content=readContend(url, 800, 800);	//
			}
			if((StringUtils.isNotBlank(content)
					&&!content.equals("-1"))
					||count==5){
				break;
			}
		}
		return content;
	}

	public static final String readContend(String url, int contimeout, int readtimeout) {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL(url);
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout(contimeout);
			connection.setReadTimeout(readtimeout);
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.connect();

			String contentEncoding = connection.getContentEncoding();// 编码
			InputStream stream;
			if (null != contentEncoding && -1 != contentEncoding.indexOf("gzip")) {
				stream = new GZIPInputStream(connection.getInputStream());
			} else if (null != contentEncoding && -1 != contentEncoding.indexOf("deflate")) {
				stream = new InflaterInputStream(connection.getInputStream());
			} else {
				stream = connection.getInputStream();
			}
			reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				// System.out.println(line);
				sb.append(line).append("\n");
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} catch (IOException e) {
			log.error("url: " + url + ",error:"+e.getMessage());
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					log.error("读关闭错误", e1);
				}
			}

			return "-1";
		} finally {
			url1 = null;
			if (connection != null)
				connection.disconnect();
		}
	}
	
	
	/**Methods Name: requestParam2Map <br>
	 * Description: <br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	public static Map<String, Object> requestParam2Map(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration enume = request.getParameterNames();
		while(enume.hasMoreElements()){
			Object item = enume.nextElement();
			String paramName = item.toString();
			String value = request.getParameter(paramName);
			String[] values = request.getParameterValues(paramName);
			if(values.length>1){
				String va=values[0];
				for(int i=1;i<values.length;i++){
					va+=","+values[i];
				}
				value=va;
			}
			map.put(paramName, value);
		}
		return map;
	}
	
	/**Methods Name: requestParam2ListMap <br>
	 * Description: name重复表单，转换多个map<br>
	 * @author name: liuhao1
	 * @param request
	 * @return
	 */
	public static Map<String, Object> requestParam2ListMap(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration enume = request.getParameterNames();
		while(enume.hasMoreElements()){
			Object item = enume.nextElement();
			String paramName = item.toString();
			String value = request.getParameter(paramName);
			String[] values = request.getParameterValues(paramName);
			if(values.length>1){
				String va=values[0];
				for(int i=1;i<values.length;i++){
					va+=","+values[i];
				}
				value=va;
			}
			map.put(paramName, value);
		}
		return map;
	}
	/**Methods Name: requestParam2Map <br>
	 * Description: 扩展request参数转map:加入额外参数<br>
	 * @author name: liuhao1
	 * @param request
	 * @param extraParams
	 * @return
	 */
	public static Map<String, Object> requestParam2Map(HttpServletRequest request,Map<String,String> extraParams){
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration enume = request.getParameterNames();
		while(enume.hasMoreElements()){
			Object item = enume.nextElement();
			String paramName = item.toString();
			System.out.println(paramName + "..........." +request.getParameter(paramName) );
			String value = request.getParameter(paramName);
			String[] values = request.getParameterValues(paramName);
			if(values.length>1){
				String va=values[0];
				for(int i=1;i<values.length;i++){
					va+=","+values[i];
				}
				value=va;
			}
			map.put(paramName, value);
		}
		map.putAll(extraParams);
		return map;
	}
	public static String requestParamtoString(HttpServletRequest request){
		
		StringBuffer buffer = new StringBuffer("?");
		
		Enumeration enume = request.getParameterNames();
		while(enume.hasMoreElements()){
			Object item = enume.nextElement();
			String paramName = item.toString();
			String value = request.getParameter(paramName);
			String[] values = request.getParameterValues(paramName);
			if(values.length>1){
				String va=values[0];
				for(int i=1;i<values.length;i++){
					va+="|"+values[i];
				}
				value=va;
			}
			buffer.append(paramName).append("=").append(value).append("&");
		}
		return buffer.toString();
	}
	public static String getAPIUrl(HttpServletRequest request,String apiName,Map<String,String> map){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(API_URL).append(apiName).append(requestParamtoString(request));
		if(null != map) {
			Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();  
	        while (iterator.hasNext()) {  
	            Map.Entry<String, String> entry = iterator.next();  
	            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
	        }  
		}
		return buffer.subSequence(0, buffer.length()-1).toString();
	}
	public static String getAPIUrl(String apiName){
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(API_URL).append(apiName);
		return buffer.toString();
	}
	
	public static String getResultFromDBAPI(HttpServletRequest request,String apiName,Map<String,String> extraParams){
		RestTemplate restTemplate = new RestTemplate();
		String message = restTemplate.postForObject(getAPIUrl(request,apiName,extraParams), null,String.class);
		return message;
	}
	/*public static String getResultFromDBAPI(HttpServletRequest request,String apiName,Map<String,String> extraParams){
		RestTemplate restTemplate = new RestTemplate();
		
		String message = restTemplate.postForObject(getAPIUrl(apiName),
													null,
													String.class,
													requestParam2Map(request, extraParams));
		return message;
	}*/
}

