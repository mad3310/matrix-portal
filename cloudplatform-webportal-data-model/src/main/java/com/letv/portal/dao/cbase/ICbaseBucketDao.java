package com.letv.portal.dao.cbase;

import java.util.HashMap;
import java.util.List;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.cbase.CbaseBucketModel;

public interface ICbaseBucketDao extends IBaseDao<CbaseBucketModel> {

	public List<CbaseBucketModel> selectByBucketNameForValidate(
			HashMap<String, Object> params);

	Integer selectCountByStatus(Integer _parameter);
}
