package com.letv.common.dao;

import java.util.HashMap;
import java.util.Map;

import com.letv.common.paging.impl.Page;

public class QueryParam {
	
	private Limit limit;
	private Page page;
	
	private Map<String,Object> params = new HashMap<String,Object>();
	
	
	public QueryParam(){};
	
	public QueryParam(Map<String,Object> params,Page page){
		this.page = page;
		this.params = params;
	};
	
	public Map<String,Object> getParams() {
		return params;
	}

	public void setParams(Map<String,Object> params) {
		this.params = params;
	}

	public Limit getLimit() {
		return limit;
	}

	public void setLimit(Limit limit) {
		this.limit = limit;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

}
