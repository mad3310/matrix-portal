package com.letv.portal.model.gce;

import java.util.List;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.GceType;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.model.UserModel;

public class GceServer extends BaseModel {
	
	private static final long serialVersionUID = -7999485658204466572L;

	private String gceName;
	
	private Long gceClusterId;
	private Long hclusterId;
	private Long logId;

	private String gceImageName;//gce镜像名称
	private String ip;
	private String portForward;//端口转发规则
	private Integer status;
	private String descn;
	private GceType type;//container类型：nginx、jetty
	private Long memorySize;
	private boolean createNginx;
	
	private HclusterModel hcluster;
	private GceCluster gceCluster;
	private UserModel createUserModel;
	private List<GceContainer> gceContainers;
	private GceServer gceServerProxy;
	
	private int buyNum;

	public GceType getType() {
		return type;
	}
	public void setType(GceType type) {
		this.type = type;
	}
	public String getGceName() {
		return gceName;
	}
	public void setGceName(String gceName) {
		this.gceName = gceName;
	}
	public Long getGceClusterId() {
		return gceClusterId;
	}
	public void setGceClusterId(Long gceClusterId) {
		this.gceClusterId = gceClusterId;
	}
	public Long getHclusterId() {
		return hclusterId;
	}
	public void setHclusterId(Long hclusterId) {
		this.hclusterId = hclusterId;
	}
	public String getGceImageName() {
		return gceImageName;
	}
	public void setGceImageName(String gceImageName) {
		this.gceImageName = gceImageName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPortForward() {
		return portForward;
	}
	public void setPortForward(String portForward) {
		this.portForward = portForward;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public HclusterModel getHcluster() {
		return hcluster;
	}
	public void setHcluster(HclusterModel hcluster) {
		this.hcluster = hcluster;
	}
	public GceCluster getGceCluster() {
		return gceCluster;
	}
	public void setGceCluster(GceCluster gceCluster) {
		this.gceCluster = gceCluster;
	}
	public UserModel getCreateUserModel() {
		return createUserModel;
	}
	public void setCreateUserModel(UserModel createUserModel) {
		this.createUserModel = createUserModel;
	}
	public List<GceContainer> getGceContainers() {
		return gceContainers;
	}
	public void setGceContainers(List<GceContainer> gceContainers) {
		this.gceContainers = gceContainers;
	}
	public GceServer getGceServerProxy() {
		return gceServerProxy;
	}
	public void setGceServerProxy(GceServer gceServerProxy) {
		this.gceServerProxy = gceServerProxy;
	}
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Long getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(Long memorySize) {
		this.memorySize = memorySize;
	}
	public boolean isCreateNginx() {
		return createNginx;
	}
	public void setCreateNginx(boolean createNginx) {
		this.createNginx = createNginx;
	}
	public int getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(int buyNum) {
		this.buyNum = buyNum;
	}
	
}
