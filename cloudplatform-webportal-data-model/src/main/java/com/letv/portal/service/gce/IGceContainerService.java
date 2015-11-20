package com.letv.portal.service.gce;

import java.util.List;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.service.IBaseService;

public interface IGceContainerService extends IBaseService<GceContainer> {
	
	public List<GceContainer> selectByGceClusterId(Long gceClusterId);

	public GceContainer selectByName(String containerName);

	void deleteByClusterId(Long id);
}
