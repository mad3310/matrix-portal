package com.letv.portal.dao.gce;

import java.util.List;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.gce.GceServer;

public interface IGceServerDao extends IBaseDao<GceServer> {

	List<GceServer> selectByName(String gceName);

	GceServer selectByClusterId(Long clusterId);

	Integer selectCountByStatus(Integer _parameter);

	void deleteByClusterId(Long _parameter);
}
