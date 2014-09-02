package com.letv.portal.model;

/**Program Name: MclusterModel <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月12日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class MclusterModel extends BaseModel {
	
	
	private static final long serialVersionUID = 8873122802478974943L;
	
	private String id;   //主键ID
	private String mclusterName; //名称
	
	private String status; //状态：
	private String isDeleted; //是否删除   0:无效 1:有效
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
	
	public MclusterModel(){};
	public MclusterModel(String id,String mclusterName,String status,String createUser){
		this.id = id;
		this.mclusterName = mclusterName;
		this.status = status;
		this.createUser = createUser;
	};
	public MclusterModel(String id,String status){
		this.id = id;
		this.status = status;
	};
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMclusterName() {
		return mclusterName;
	}
	public void setMclusterName(String mclusterName) {
		this.mclusterName = mclusterName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 
	 * 
	 * rest api
	 * 
	 * 
	 * func:在提供ip的host机器上，创建相应container：1个vip 3个normal
	 * 
	 * 传入参数：
	 * 
	 * 返回结果：  返回4个container信息：  ip 及标识 vip/normal
	 * 
	 * 
	 * 
	 */
	@Override
	public String toString() {
		return "MclusterModel [id=" + id + ", mclusterName=" + mclusterName
				+ ", status=" + status + ", isDeleted=" + isDeleted
				+ ", createTime=" + createTime + ", createUser=" + createUser
				+ ", updateTime=" + updateTime + ", updateUser=" + updateUser
				+ "]";
	}
	
}
