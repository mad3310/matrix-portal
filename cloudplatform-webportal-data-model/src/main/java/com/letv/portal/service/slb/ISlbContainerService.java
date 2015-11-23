package com.letv.portal.service.slb;

import java.util.List;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.gce.GceContainer;
import com.letv.portal.model.slb.SlbContainer;
import com.letv.portal.service.IBaseService;

public interface ISlbContainerService extends IBaseService<SlbContainer> {

	public List<SlbContainer> selectBySlbClusterId(Long slbClusterId);

	public SlbContainer selectByName(String containerName);

	void deleteByClusterId(Long id);
}
