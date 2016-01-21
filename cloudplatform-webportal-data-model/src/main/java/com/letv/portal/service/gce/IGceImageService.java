package com.letv.portal.service.gce;

import com.letv.portal.model.gce.GceImage;
import com.letv.portal.service.IBaseService;

public interface IGceImageService extends IBaseService<GceImage> {

	public GceImage selectByUrl(String url);

	void pushImage(Long id, String hclusterIds);
}
