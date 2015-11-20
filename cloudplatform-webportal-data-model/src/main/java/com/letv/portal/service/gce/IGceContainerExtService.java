package com.letv.portal.service.gce;

import com.letv.portal.model.gce.GceContainerExt;
import com.letv.portal.service.IBaseService;

public interface IGceContainerExtService extends IBaseService<GceContainerExt> {

    void deleteByClusterId(Long id);
}
