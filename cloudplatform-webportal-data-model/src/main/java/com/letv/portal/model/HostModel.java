package com.letv.portal.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.letv.common.model.BaseModel;


/**Program Name: HostModel <br>
 * Description:  <br>
 * @author name: liuhao1 <br>
 * Written Date: 2014年8月12日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class HostModel extends BaseModel {
	
	private static final long serialVersionUID = -4046115868963401983L;
	private String hostName; //主机名称
	private String hostIp; //主机ip
	private Integer nodesNumber; //节点个数
	private String hostModel; //主机型号
	private String cpuModel; //cpu型号
	private Integer coresNumber; //cpu核数
	private Integer memorySize; //内存大小
	private Integer diskSize; //磁盘大小
	private Integer diskUsed; //磁盘使用量
	private String  type;     //主机类型
	private Long  hclusterId;     //主机类型
	private String  descn;     //主机描述
	private HclusterModel hcluster; //物理机集群
	private String name; //用户名
	private String password; //密码
	private String hostNameAlias; //别名
	

	private Integer status; //状态:
	
	
	
	
	public String getHostNameAlias() {
		return hostNameAlias;
	}
	public void setHostNameAlias(String hostNameAlias) {
		this.hostNameAlias = hostNameAlias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getHclusterId() {
		return hclusterId;
	}
	public void setHclusterId(Long hclusterId) {
		this.hclusterId = hclusterId;
	}
	public String getDescn() {
		return descn;
	}
	public void setDescn(String descn) {
		this.descn = descn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public HclusterModel getHcluster() {
		return hcluster;
	}
	public void setHcluster(HclusterModel hcluster) {
		this.hcluster = hcluster;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public Integer getNodesNumber() {
		return nodesNumber;
	}
	public void setNodesNumber(Integer nodesNumber) {
		this.nodesNumber = nodesNumber;
	}
	public String getHostModel() {
		return hostModel;
	}
	public void setHostModel(String hostModel) {
		this.hostModel = hostModel;
	}
	
	public String getCpuModel() {
		return cpuModel;
	}
	public void setCpuModel(String cpuModel) {
		this.cpuModel = cpuModel;
	}
	public Integer getCoresNumber() {
		return coresNumber;
	}
	public void setCoresNumber(Integer coresNumber) {
		this.coresNumber = coresNumber;
	}
	public Integer getMemorySize() {
		return memorySize;
	}
	public void setMemorySize(Integer memorySize) {
		this.memorySize = memorySize;
	}
	public Integer getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(Integer diskSize) {
		this.diskSize = diskSize;
	}
	public Integer getDiskUsed() {
		return diskUsed;
	}
	public void setDiskUsed(Integer diskUsed) {
		this.diskUsed = diskUsed;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
