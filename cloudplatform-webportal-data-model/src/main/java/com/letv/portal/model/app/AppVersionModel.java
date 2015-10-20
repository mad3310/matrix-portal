package com.letv.portal.model.app;

import com.letv.common.model.BaseModel;

/**
 * app版本实体类
 *
 */
public class AppVersionModel extends BaseModel {

	private static final long serialVersionUID = 4894529482499154716L;

	private Integer versionCode;//版本编号
	private String versionName;//版本名称
	private String platform;//平台
	private Integer forceUpdate;//是否强制升级：1-是，0-否
	private String url;//下载地址
	/*
	 * 描述
	 */
	private String descn;
	
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Integer getForceUpdate() {
		return forceUpdate;
	}
	public void setForceUpdate(Integer forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	
}
