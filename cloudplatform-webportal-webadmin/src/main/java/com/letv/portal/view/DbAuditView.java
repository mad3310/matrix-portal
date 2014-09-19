package com.letv.portal.view;


public class DbAuditView {


	private String auditType;
	private String auditInfo;
	private String mclusterId;
	private String mclusterName;
	private String dbId;
	private String applyCode;
	private String dbApplyStandardId;
	private String hostIds;
	private String createUser;
	
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}
	public String getMclusterId() {
		return mclusterId;
	}
	public void setMclusterId(String mclusterId) {
		this.mclusterId = mclusterId;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	
	public String getApplyCode() {
		return applyCode;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public String getDbApplyStandardId() {
		return dbApplyStandardId;
	}
	public void setDbApplyStandardId(String dbApplyStandardId) {
		this.dbApplyStandardId = dbApplyStandardId;
	}
	public String getHostIds() {
		return hostIds;
	}
	public void setHostIds(String hostIds) {
		this.hostIds = hostIds;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getMclusterName() {
		return mclusterName;
	}
	public void setMclusterName(String mclusterName) {
		this.mclusterName = mclusterName;
	}
	
}
