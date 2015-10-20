package com.letv.portal.model.app;

import com.letv.common.model.BaseModel;

/**
 * 通知信息实体类
 *
 */
public class MessageModel extends BaseModel {

	private static final long serialVersionUID = 4894529482499154716L;

	private String title;
	/*
	 * 描述
	 */
	private String descn;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	
}
