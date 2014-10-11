package com.letv.portal.model;


/**Program Name: DbModel <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月12日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class DbModel extends BaseModel {
	
	
	private static final long serialVersionUID = -5881585751828246693L;
	
	private String id;   //主键ID
	private String dbName; //db名称
	private String clusterId; //所属cluster
	private MclusterModel cluster; //所属cluster

	private String status; //状态
	private String auditInfo; //状态
	private String isDeleted; //是否删除   0:无效 1:有效
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
	
	public DbModel(){}
	public DbModel(String id,String status){
		this.id = id;
		this.status = status;
	}
	public DbModel(String id,String status,String clusterId,String auditInfo){
		this.id = id;
		this.status = status;
		this.clusterId = clusterId;
		this.auditInfo = auditInfo;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public MclusterModel getCluster() {
		return cluster;
	}
	public void setCluster(MclusterModel cluster) {
		this.cluster = cluster;
	}
	
	public String getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}
	@Override
	public String toString() {
		return "DbModel [id=" + id + ", dbName=" + dbName + ", clusterId="
				+ clusterId + ", cluster=" + cluster + ", status=" + status
				+ ", isDeleted=" + isDeleted + ", createTime=" + createTime
				+ ", createUser=" + createUser + ", updateTime=" + updateTime
				+ ", updateUser=" + updateUser + "]";
	}
	
	
}
