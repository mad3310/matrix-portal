package com.letv.portal.enumeration;

public enum GceType implements ByteEnum{
	JETTY(0),
	NGINX_PROXY(1),
	NGINX(2),
	TOMCAT(3),
	RESIN(4);
	
	private final Integer value;
	
	private GceType(Integer value) {
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return this.value;
	}

}
