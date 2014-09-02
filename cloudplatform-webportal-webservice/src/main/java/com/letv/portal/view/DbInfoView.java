package com.letv.portal.view;

import java.util.List;

import com.letv.portal.model.ContainerModel;
import com.letv.portal.model.DbApplyStandardModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.DbUserModel;

public class DbInfoView {

	private DbModel dbModel;
	private DbApplyStandardModel dbApplyStandard;
	private List<DbUserModel> dbUsers;
	private List<ContainerModel> containers;
	
	public DbInfoView(){};
	public DbInfoView(DbModel dbModel,DbApplyStandardModel dbApplyStandard,List<DbUserModel> dbUsers,List<ContainerModel> containers){
		this.dbModel = dbModel;
		this.dbApplyStandard = dbApplyStandard;
		this.dbUsers = dbUsers;
		this.containers = containers;
	}
	
	public DbModel getDbModel() {
		return dbModel;
	}
	public void setDbModel(DbModel dbModel) {
		this.dbModel = dbModel;
	}
	
	public DbApplyStandardModel getDbApplyStandard() {
		return dbApplyStandard;
	}
	public void setDbApplyStandard(DbApplyStandardModel dbApplyStandard) {
		this.dbApplyStandard = dbApplyStandard;
	}
	public List<DbUserModel> getDbUsers() {
		return dbUsers;
	}
	public void setDbUsers(List<DbUserModel> dbUsers) {
		this.dbUsers = dbUsers;
	}
	public List<ContainerModel> getContainers() {
		return containers;
	}
	public void setContainers(List<ContainerModel> containers) {
		this.containers = containers;
	}
	
	
}
