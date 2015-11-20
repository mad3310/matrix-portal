package com.letv.portal.dao.gce;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.gce.GceContainerExt;

public interface IGceContainerExtDao extends IBaseDao<GceContainerExt> {

    void deleteByClusterId(Long _parameter);
}
