package com.letv.portal.service.slb;

import java.util.Map;

import com.letv.portal.enumeration.SlbStatus;
import com.letv.portal.model.slb.SlbServer;
import com.letv.portal.service.IBaseService;

public interface ISlbServerService extends IBaseService<SlbServer> {

	Map<String, Object> save(SlbServer slbServer);
	void updateSlbStatusById(Long id, SlbStatus status);

	Integer selectCountByStatus(Integer value);

	void deleteByClusterId(Long id);
}
