package com.letv.portal.model.gce;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.GceType;
import com.letv.portal.model.HclusterModel;
import com.letv.portal.model.UserModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

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

    @NotEmpty
    @Length(max = 20,min = 2)
	public String getGceName() {
		return gceName;
	}
    @NotNull
	public Long getHclusterId() {
		return hclusterId;
	}
    @Length(max = 50)
    public String getDescn() {
        return descn;
    }
    @NotNull
    public GceType getType() {
        return type;
    }
    @NotNull
    public Long getMemorySize() {
        return memorySize;
    }
    @NotNull
    public boolean isCreateNginx() {
        return createNginx;
    }
    @NotNull
    public int getBuyNum() {
        return buyNum;
    }

    public Long getGceClusterId() {
        return gceClusterId;
    }

	public Long getLogId() {
		return logId;
	}

	public String getGceImageName() {
		return gceImageName;
	}

	public String getIp() {
		return ip;
	}

	public String getPortForward() {
		return portForward;
	}

	public Integer getStatus() {
		return status;
	}

	public HclusterModel getHcluster() {
		return hcluster;
	}

	public GceCluster getGceCluster() {
		return gceCluster;
	}

	public UserModel getCreateUserModel() {
		return createUserModel;
	}

	public List<GceContainer> getGceContainers() {
		return gceContainers;
	}

	public GceServer getGceServerProxy() {
		return gceServerProxy;
	}

    public void setGceName(String gceName) {
        this.gceName = gceName;
    }

    public void setGceClusterId(Long gceClusterId) {
        this.gceClusterId = gceClusterId;
    }

    public void setHclusterId(Long hclusterId) {
        this.hclusterId = hclusterId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void setGceImageName(String gceImageName) {
        this.gceImageName = gceImageName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPortForward(String portForward) {
        this.portForward = portForward;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public void setType(GceType type) {
        this.type = type;
    }

    public void setMemorySize(Long memorySize) {
        this.memorySize = memorySize;
    }

    public void setCreateNginx(boolean createNginx) {
        this.createNginx = createNginx;
    }

    public void setHcluster(HclusterModel hcluster) {
        this.hcluster = hcluster;
    }

    public void setGceCluster(GceCluster gceCluster) {
        this.gceCluster = gceCluster;
    }

    public void setCreateUserModel(UserModel createUserModel) {
        this.createUserModel = createUserModel;
    }

    public void setGceContainers(List<GceContainer> gceContainers) {
        this.gceContainers = gceContainers;
    }

    public void setGceServerProxy(GceServer gceServerProxy) {
        this.gceServerProxy = gceServerProxy;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
}
