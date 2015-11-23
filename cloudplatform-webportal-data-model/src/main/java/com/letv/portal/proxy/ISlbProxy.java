package com.letv.portal.proxy;

import com.letv.portal.model.slb.SlbServer;

public interface ISlbProxy extends IBaseProxy<SlbServer> {
	
	public void saveAndBuild(SlbServer slbServer);
	public void restart(Long id);
	public void stop(Long id);
	public void start(Long id);

	void checkStatus();
}
