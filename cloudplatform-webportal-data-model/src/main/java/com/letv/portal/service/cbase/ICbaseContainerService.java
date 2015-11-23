package com.letv.portal.service.cbase;

import java.util.List;

import com.letv.portal.model.cbase.CbaseContainerModel;
import com.letv.portal.service.IBaseService;

public interface ICbaseContainerService extends
		IBaseService<CbaseContainerModel> {

	public List<CbaseContainerModel> selectByCbaseClusterId(Long cbaseClusterId);

	public CbaseContainerModel selectByName(String string);

	void deleteByClusterId(Long id);
}
