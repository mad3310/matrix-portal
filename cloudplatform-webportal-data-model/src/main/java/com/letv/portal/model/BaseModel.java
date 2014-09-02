package com.letv.portal.model;

public class BaseModel implements IEntity,ISoftDelete{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5822755615614280336L;
	
	private String id;
	
	private boolean deleted;

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;		
	}

	@Override
	public String getId() {
		return id;
	}

	@Deprecated
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
}
