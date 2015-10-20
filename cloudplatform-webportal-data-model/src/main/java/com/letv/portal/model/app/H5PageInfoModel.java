package com.letv.portal.model.app;

import com.letv.common.model.BaseModel;

/**
 * H5页面信息实体类
 *
 */
public class H5PageInfoModel extends BaseModel {

	private static final long serialVersionUID = 4894529482499154716L;

	private String pageId;
	private String type;
	private String webUrl;
	private String title;
	private String iconUrl;
	/*
	 * 描述
	 */
	private String descn;
	
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}

}
