package com.letv.portal.proxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.FileEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.exception.CommonException;
import com.letv.common.exception.ValidateException;
import com.letv.common.util.HttpsClient;
import com.letv.portal.enumeration.OssServerVisibility;
import com.letv.portal.model.HostModel;
import com.letv.portal.model.UserModel;
import com.letv.portal.model.swift.SwiftServer;
import com.letv.portal.model.task.service.ITaskEngine;
import com.letv.portal.proxy.ISwiftServerProxy;
import com.letv.portal.service.IBaseService;
import com.letv.portal.service.IHostService;
import com.letv.portal.service.IUserService;
import com.letv.portal.service.swift.ISwiftServerService;

@Component
public class SwiftServerProxyImpl extends BaseProxyImpl<SwiftServer> implements ISwiftServerProxy{
	
	private final static Logger logger = LoggerFactory.getLogger(SwiftServerProxyImpl.class);
	
	@Autowired
	private ISwiftServerService swiftServerService;
	
	@Autowired
	private ITaskEngine taskEngine;
	@Autowired
	private IHostService hostService;
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	@Value("${matrix.swift.super.user}")
	private String SWIFT_SUPER_USER;
	@Value("${matrix.swift.super.password}")
	private String SWIFT_SUPER_USER_PWD;
	@Value("${matrix.swift.auth.source}")
	private String SWIFT_SUPER_AUTH_SOURCE;
	@Value("${matrix.swift.file.default.local}")
	private String MATRIX_SWIFT_FILE_DEFAULT_LOCAL;
	@Override
	public IBaseService<SwiftServer> getService() {
		return swiftServerService;
	}
	
	@Override
	public void saveAndBuild(SwiftServer swift) {
		this.swiftServerService.insert(swift);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("swiftId", swift.getId());
		params.put("serviceName", swift.getName());
		this.build(params);
	}

	private void build(Map<String,Object> params) {
    	this.taskEngine.run("OSS_BUY",params);
	}
	
	@Override
	public void deleteAndBuild(Long swiftId) {
		SwiftServer server = this.selectById(swiftId);
		if(server == null)
			throw new ValidateException("oss 服务不存在");
		this.deleteFile(swiftId, "root", true);
		this.deleteBuild(server, this.getSuperToken(server));
		super.delete(server);
	}

	private void deleteBuild(SwiftServer server,String token) {
		HostModel host = this.getHost(server.getHclusterId());
		UserModel user = this.userService.selectById(server.getCreateUser());
		
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", token);
		
		HttpResponse response = HttpsClient.httpDeleteByHeader(getSwiftMainUrl(host.getHostIp(),user.getUserName(),server.getName()),headParams,2000,30000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
	}
	private String getSuperToken(SwiftServer server) {
		HostModel host = this.getHost(server.getHclusterId());
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("x-auth-user", SWIFT_SUPER_USER);
		headParams.put("x-auth-key", SWIFT_SUPER_USER_PWD);
		headParams.put("x-auth-source", SWIFT_SUPER_AUTH_SOURCE);
		HttpResponse response = HttpsClient.httpGetByHeader(getSwiftGetTokenUrl(host.getHostIp()),headParams,2000,30000);
		if(response == null || response.getFirstHeader("X-Auth-Token") == null) {
			throw new CommonException("oss exception:get super token failed");
		}
		String authToken = response.getFirstHeader("X-Auth-Token").getValue();
		return authToken;
	}
	private String getSwiftGetTokenUrl(String ip) {
		StringBuffer sb = new StringBuffer();
		sb.append("https://").append(ip).append(":443").append("/auth/v1.0");
		return sb.toString();
	}
	private String getSwiftMainUrl(String ip,String username,String containerName) {
		StringBuffer sb = new StringBuffer();
		sb.append("https://").append(ip).append(":443").append("/v1/AUTH_").append(username).append("/").append(containerName);
		return sb.toString();
		
	}
	private String getSwiftMainFileUrl(String ip,String username,String containerName,String directory,boolean isDelimiter) {
		StringBuffer sb = new StringBuffer();
		if("root".equals(directory)) {
			directory = "";
		} else {
			directory +="/";
		}
		sb.append("https://").append(ip).append(":443").append("/v1/AUTH_").append(username).append("/").append(containerName)
		.append("?format=json&prefix=").append(directory);
		if(isDelimiter)
			sb.append("&delimiter=/");
		return sb.toString();
		
	}
	private  HostModel getHost(Long hclusterId) {
		if(hclusterId == null)
			throw new ValidateException("hclusterId is null :" + hclusterId);
		HostModel host = this.hostService.getHostByHclusterId(hclusterId);
		if(host == null)
			throw new ValidateException("host is null by hclusterIdId:" + hclusterId);
		
		return host;
	}

	@Override
	public Object getFiles(Long id, String directory) {
		SwiftServer server = this.selectById(id);
		if(server == null)
			throw new ValidateException("oss 服务不存在");
		return this.getFiles(server,  this.getSuperToken(server),directory,true);
	}
	
	private Object getFiles(SwiftServer server,String token,String directory,boolean isDelimiter) {
		HostModel host = this.getHost(server.getHclusterId());
		UserModel user = this.userService.selectById(server.getCreateUser());
		
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", token);
		HttpResponse response = HttpsClient.httpGetByHeader(getSwiftMainFileUrl(host.getHostIp(),user.getUserName(),server.getName(),directory,isDelimiter),headParams,2000,30000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
		String result = "";
		try {
			result = EntityUtils.toString(response.getEntity());
		}  catch (Exception e) {
			throw new CommonException("get oss files failed :" + e.getMessage());
		} 
		return JSONArray.parse(result);
	}

	@Override
	public void changeService(Long id, String level,Long storeSize) {
			
		SwiftServer server = this.selectById(id);
		HostModel host = this.getHost(server.getHclusterId());
		UserModel user = this.userService.selectById(server.getCreateUser());
		
		server.setStoreSize(storeSize);
		server.setVisibilityLevel(OssServerVisibility.PRIVATE);
		
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", getSuperToken(server));
		headParams.put("X-Container-Meta-Quota-Bytes", String.valueOf(server.getStoreSize()*1024*1024*1024));
		headParams.put("X-Container-Write", user.getUserName() + ":" + user.getUserName());
		headParams.put("X-Container-Read", user.getUserName() + ":" + user.getUserName());
		if("PUBLIC".equals(level)) {
			server.setVisibilityLevel(OssServerVisibility.PUBLIC);
			headParams.put("X-Container-Read", ".r:*,.rlistings");
		}
		if(storeSize != null)
			headParams.put("X-Container-Meta-Quota-Bytes", String.valueOf(storeSize*1024*1024*1024));
		HttpResponse response = HttpsClient.httpPutByHeader(getSwiftMainUrl(host.getHostIp(),user.getUserName(),server.getName()),headParams,null,2000,30000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
		
		this.updateBySelective(server);
	}

	@Override
	public String postFiles(Long id, MultipartFile file, String directory) {
		SwiftServer server = this.selectById(id);
		if(server == null)
			throw new ValidateException("oss 服务不存在");
		
		//save file to local
		File localFile = this.saveFileToLocal(file);
		//update file to OSS server
		FileEntity entity = new FileEntity(localFile);
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", getSuperToken(server));
		String fileUrl = getSwiftDetailFileUrl(server,directory,file.getOriginalFilename());
		HttpResponse response = HttpsClient.httpPutByHeader(fileUrl,headParams,entity,2000,30000000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
		//delete from local
		this.removeFileFromLocal(file.getOriginalFilename());
		return fileUrl;
	}
	private File saveFileToLocal(MultipartFile file) {
        String fileName = file.getOriginalFilename();  
        File targetFile = new File(MATRIX_SWIFT_FILE_DEFAULT_LOCAL, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
        	throw new CommonException("save oss file to local failed:"+e.getMessage());
        }  
		return targetFile;
	}
	private boolean removeFileFromLocal(String fileName) {
		File targetFile = new File(MATRIX_SWIFT_FILE_DEFAULT_LOCAL, fileName);  
		return targetFile.delete();
	}

	@Override
	public void addFolder(Long id, String file, String directory) {
		SwiftServer server = this.selectById(id);
		if(server == null)
			throw new ValidateException("oss 服务不存在");
		
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", getSuperToken(server));
		headParams.put("Content-Type", "application/directory");
		
		HttpResponse response = HttpsClient.httpPutByHeader(getSwiftDetailFileUrl(server,directory,file),headParams,null,2000,30000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
	}
	private String getSwiftDetailFileUrl(SwiftServer server,String directory,String fileName) {
		if("root".equals(directory)) {
			directory = "";
		} else {
			directory +="/";
		}
		HostModel host = this.getHost(server.getHclusterId());
		UserModel user = this.userService.selectById(server.getCreateUser());
		StringBuffer sb = new StringBuffer();
		sb.append("https://").append(host.getHostIp()).append(":443").append("/v1/AUTH_").append(user.getUserName()).append("/").append(server.getName()).append("/").append(directory).append(fileName);
		logger.info("getSwiftDetailFileUrl:{}",sb);
		return sb.toString();
	}

	@Override
	public void deleteFile(Long id, String file,boolean isFolder) {
		SwiftServer server = this.selectById(id);
		if(server == null)
			throw new ValidateException("oss 服务不存在");
		String superToken = getSuperToken(server);
		this.deleteFile(server, file, superToken,isFolder);
		
	}

	private void deleteFile(SwiftServer server, String file,String superToken,boolean isFolder) {
		if(isFolder) {
			List<Map<String,Object>> files =(List<Map<String, Object>>) this.getFiles(server, superToken, file,false);
			for (Map<String,Object> object : files) {
				this.deleteFile(server.getId(),  (String) object.get("name"), false);
			}
		}
		if("root".equals(file))
			return;
		Map<String,String> headParams = new HashMap<String,String>();
		headParams.put("X-Auth-Token", superToken);
		HttpResponse response = HttpsClient.httpDeleteByHeader(getSwiftDetailFileUrl(server,"root",file),headParams,2000,30000);
		if(response == null || response.getStatusLine() == null || response.getStatusLine().getStatusCode()>300) {
			throw new CommonException(response == null?"api connect failed":response.getStatusLine().toString());
		}
	}
	
}
