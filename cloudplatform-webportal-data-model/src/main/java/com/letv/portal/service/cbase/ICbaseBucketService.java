package com.letv.portal.service.cbase;

import java.util.List;
import java.util.Map;

import com.letv.portal.model.DbModel;
import com.letv.portal.model.cbase.CbaseBucketModel;
import com.letv.portal.service.IBaseService;

public interface ICbaseBucketService extends IBaseService<CbaseBucketModel> {

	Map<String, Object> save(CbaseBucketModel cbaseBucketModel);

	public List<CbaseBucketModel> selectByBucketNameForValidate(
			String bucketName, Long createUser);

	public CbaseBucketModel bucketList(Long cacheId);
	
	public Map<String, Object> getMoxiConfig(Long cacheId);

	Integer selectCountByStatus(Integer value);
}
