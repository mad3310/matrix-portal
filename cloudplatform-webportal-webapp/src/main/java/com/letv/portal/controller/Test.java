package com.letv.portal.controller;

public class Test {

	private String mclusterName;
	private String ipName;
	public String getMclusterName() {
		return mclusterName;
	}
	public void setMclusterName(String mclusterName) {
		this.mclusterName = mclusterName;
	}
	public String getIpName() {
		return ipName;
	}
	public void setIpName(String ipName) {
		this.ipName = ipName;
	}
	@Override
	public String toString() {
		return "Test [mclusterName=" + mclusterName + ", ipName=" + ipName
				+ "]";
	}
	
	
}
