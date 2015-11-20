package com.letv.portal.proxy;

import com.letv.portal.model.gce.GceServer;

public interface IGceProxy extends IBaseProxy<GceServer> {
	
	public void saveAndBuild(GceServer gceServer,Long rdsId,Long ocsId);
	public void start(Long id);
	public void stop(Long id);
	public void restart(Long id);
	public void capacity(Long clusterId, int multiple);
	public void checkStatus();
}
