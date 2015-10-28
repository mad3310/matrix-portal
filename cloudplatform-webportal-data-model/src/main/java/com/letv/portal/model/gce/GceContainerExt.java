package com.letv.portal.model.gce;

import com.letv.common.model.BaseModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GceContainerExt extends BaseModel {
	
	private static final long serialVersionUID = 6663970534108172228L;
	
	private Long containerId;
	private GceContainer container;
	private String bindPort;//绑定端口，供外部调用用
	private String innerPort;//内部端口，容器内使用
	private String type;//类型，例如：moxi、gbalancer
	private String descn;//描述
	
	public Long getContainerId() {
		return containerId;
	}
	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}
	public GceContainer getContainer() {
		return container;
	}
	public void setContainer(GceContainer container) {
		this.container = container;
	}
	public String getBindPort() {
		return bindPort;
	}
	public void setBindPort(String bindPort) {
		this.bindPort = bindPort;
	}
	public String getInnerPort() {
		return innerPort;
	}
	public void setInnerPort(String innerPort) {
		this.innerPort = innerPort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	
}
