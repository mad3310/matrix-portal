package com.letv.portal.task.gce.service.impl;

import com.letv.portal.enumeration.ByteEnum;

public enum ManageType implements ByteEnum{

	MANAGER(8888),LOGS(9999),MOXI(7777),GBALANCER(9888);
	
	private final Integer value;
	
	private ManageType(Integer value){
		this.value = value;
	}
	
	@Override
	public Integer getValue() {
		return this.value;
	}
}


