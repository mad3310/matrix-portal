package com.letv.portal.model.app;

import com.letv.common.model.BaseModel;

/**
 * 反馈建议实体类
 *
 */
public class AppSuggestionModel extends BaseModel {

	private static final long serialVersionUID = 4894529482499154716L;

	private String contact;//联系方式:电话/邮箱
	private String content;//内容
	/*
	 * 描述
	 */
	private String descn;
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	
}
