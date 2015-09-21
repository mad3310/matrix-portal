package com.letv.portal.service.cloudvm;

import com.letv.portal.model.cloudvm.CloudvmVmCount;
import com.letv.portal.service.IBaseService;

/**
 * Created by zhouxianguang on 2015/8/19.
 */
public interface ICloudvmVmCountService extends IBaseService<CloudvmVmCount>{
    CloudvmVmCount getVmCountOfCurrentUser();
    void createVmCountOfCurrentUser(int count);
    void updateVmCountOfCurrentUser(int count);
}
