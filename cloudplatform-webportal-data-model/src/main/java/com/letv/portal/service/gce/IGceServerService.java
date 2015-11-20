package com.letv.portal.service.gce;

import java.util.List;
import java.util.Map;

import com.letv.portal.model.gce.GceServer;
import com.letv.portal.model.gce.GceServerExt;
import com.letv.portal.service.IBaseService;

public interface IGceServerService extends IBaseService<GceServer> {

	Map<String, Object> save(GceServer gceServer);

	Boolean isExistByName(String gceName);

	List<GceServer> selectByName(String gceName);

	GceServer selectProxyServerByGce(GceServer gce);

	GceServer selectGceAndProxyById(Long id);

	void saveGceExt(GceServerExt gse);

	GceServer selectByClusterId(Long clusterId);
	
	GceServerExt selectByGceServerId(Long gceServerId);

	Integer selectCountByStatus(Integer value);

	void deleteByClusterId(Long id);
}
