package com.letv.portal.model.gce;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.GceImageStatus;
import com.letv.portal.model.UserModel;

public class GceImage extends BaseModel {
	
	private static final long serialVersionUID = -7999485658204466572L;

	private Long owner;//所属用户
	
	private String url;
	private String tag;
	private String name;
	private String type;//jetty,nginx
	private GceImageStatus status;
	private String  logUrl;
	private String descn;//镜像描述
	private String netType;
	
	private UserModel createUserModel;
	
	public Long getOwner() {
		return owner;
	}
	public void setOwner(Long owner) {
		this.owner = owner;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public GceImageStatus getStatus() {
		return status;
	}
	public void setStatus(GceImageStatus status) {
		this.status = status;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public UserModel getCreateUserModel() {
		return createUserModel;
	}
	public void setCreateUserModel(UserModel createUserModel) {
		this.createUserModel = createUserModel;
	}
	public String getLogUrl() {
		return logUrl;
	}
	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
}
