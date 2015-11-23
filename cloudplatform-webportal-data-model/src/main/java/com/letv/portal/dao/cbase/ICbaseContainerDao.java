package com.letv.portal.dao.cbase;

import java.util.List;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.cbase.CbaseContainerModel;

public interface ICbaseContainerDao extends IBaseDao<CbaseContainerModel> {

	 List<CbaseContainerModel> selectContainerByCbaseClusterId(
			Long clusterId);

	 CbaseContainerModel selectByName(String containerName);

	void deleteByClusterId(Long _parameter);
}
