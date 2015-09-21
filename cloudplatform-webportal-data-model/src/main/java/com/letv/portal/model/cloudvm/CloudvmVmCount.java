package com.letv.portal.model.cloudvm;

import com.letv.common.model.BaseModel;

/**
 * Created by zhouxianguang on 2015/8/19.
 */
public class CloudvmVmCount extends BaseModel {

    private static final long serialVersionUID = -7021105235327076566L;

    private Integer vmCount;

    public CloudvmVmCount(){
    }

    public CloudvmVmCount(Long userId, Integer vmCount){
        setCreateUser(userId);
        this.vmCount = vmCount;
    }

    public Integer getVmCount() {
        return vmCount;
    }

    public void setVmCount(Integer vmCount) {
        this.vmCount = vmCount;
    }
}
