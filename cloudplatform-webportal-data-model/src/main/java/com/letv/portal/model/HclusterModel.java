package com.letv.portal.model;

import com.letv.common.model.BaseModel;
/**
 * Program Name: HclusterModel <br>
 * Description:  物理机集群<br>
 * @author name: wujun <br>
 * Written Date: 2014年10月21日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class HclusterModel extends BaseModel{
	private static final long serialVersionUID = 3497965985607790962L;
	
	private String hclusterName; //集群名称
	private Integer status; //状态:
	public String getHclusterName() {
		return hclusterName;
	}
	public void setHclusterName(String hclusterName) {
		this.hclusterName = hclusterName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
