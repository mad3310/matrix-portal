package com.letv.portal.service.gce.impl;

import javax.annotation.Resource;

import com.letv.portal.enumeration.GceImageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.dao.gce.IGceImageDao;
import com.letv.portal.model.gce.GceImage;
import com.letv.portal.service.gce.IGceImageService;
import com.letv.portal.service.impl.BaseServiceImpl;

@Service("gceImageService")
public class GceImageServiceImpl extends BaseServiceImpl<GceImage> implements IGceImageService{
	
	private final static Logger logger = LoggerFactory.getLogger(GceImageServiceImpl.class);
	
	@Resource
	private IGceImageDao gceImageDao;

	public GceImageServiceImpl() {
		super(GceImage.class);
	}

	@Override
	public IBaseDao<GceImage> getDao() {
		return this.gceImageDao;
	}

	@Override
	public GceImage selectByUrl(String url) {
		return this.gceImageDao.selectByUrl(url);
	}

	@Override
	public void insert(GceImage gceImage) {
		if(null == gceImage.getStatus()) {
			gceImage.setStatus(GceImageStatus.AVAILABLE);
		}
		super.insert(gceImage);
	}
}
