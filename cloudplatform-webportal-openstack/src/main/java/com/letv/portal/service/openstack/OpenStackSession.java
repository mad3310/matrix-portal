package com.letv.portal.service.openstack;

import com.letv.portal.service.openstack.exception.OpenStackException;
import com.letv.portal.service.openstack.resource.manager.ImageManager;
import com.letv.portal.service.openstack.resource.manager.NetworkManager;
import com.letv.portal.service.openstack.resource.manager.VMManager;
import com.letv.portal.service.openstack.resource.manager.VolumeManager;

import java.io.Closeable;
import java.io.Serializable;

/**
 * Created by zhouxianguang on 2015/6/8.
 */
public interface OpenStackSession extends Closeable, Serializable {
    ImageManager getImageManager() throws OpenStackException;
    
    NetworkManager getNetworkManager() throws OpenStackException;
    
    VMManager getVMManager() throws OpenStackException;
    
    VolumeManager getVolumeManager() throws OpenStackException;

    boolean isClosed();

    boolean isAuthority();

    void init() throws OpenStackException;

//    Set<String> listRegions();
//
//    VM createVM(String region, VMConf conf);
//
//    void deleteVM(String region, VM vm);
//
//    List<VM> listVM(String region);
//
//    void startVM(String region, VM vm);
//
//    void stopVM(String region, VM vm);
    
    // vm image network identify
    
}
