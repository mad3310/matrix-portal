package com.letv.portal.model.common;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.ZookeeperStatus;
import com.letv.portal.model.HclusterModel;

public class ZookeeperInfo extends BaseModel {
	
	private static final long serialVersionUID = -8112440978709519461L;

	private String name;
	private String ip;
	private String port;
	private Integer used;
	private ZookeeperStatus status;
	private String descn;
	
	private Long hclusterId;
	
	private HclusterModel hcluster;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getUsed() {
		return used;
	}
	public void setUsed(Integer used) {
		this.used = used;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public ZookeeperStatus getStatus() {
		return status;
	}
	public void setStatus(ZookeeperStatus status) {
		this.status = status;
	}
	public Long getHclusterId() {
		return hclusterId;
	}
	public void setHclusterId(Long hclusterId) {
		this.hclusterId = hclusterId;
	}
	public HclusterModel getHcluster() {
		return hcluster;
	}
	public void setHcluster(HclusterModel hcluster) {
		this.hcluster = hcluster;
	}
}
