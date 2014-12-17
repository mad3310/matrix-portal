package com.letv.portal.model;

import java.util.ArrayList;
import java.util.List;

public class ZabbixParam {
    private String  host;

    private List<Object> groups = new ArrayList<Object>();
    private List<Object> templates = new ArrayList<Object>();
    private List<InterfacesModel> interfaces = new ArrayList<InterfacesModel>();
    private InventoryModel inventory = new InventoryModel();
    
    
    public ZabbixParam(){
    	GroupsModel groupsModel = new GroupsModel();
    	TemplatesModel templatesModel = new TemplatesModel();
    	groups.add(groupsModel);
    	templates.add(templatesModel);
    }
    
    //添加构造函数，传入模板id 20141217 by liuhao
    public ZabbixParam(String templateId){
    	GroupsModel groupsModel = new GroupsModel();
    	TemplatesModel templatesModel = new TemplatesModel(templateId);
    	groups.add(groupsModel);
    	templates.add(templatesModel);
    }
    
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public List<Object> getGroups() {
		return groups;
	}
	public void setGroups(List<Object> groups) {
		this.groups = groups;
	}
	public List<Object> getTemplates() {
		return templates;
	}
	public void setTemplates(List<Object> templates) {
		this.templates = templates;
	}

	public List<InterfacesModel> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<InterfacesModel> list) {
		this.interfaces = list;
	}

	public InventoryModel getInventory() {
		return inventory;
	}

	public void setInventory(InventoryModel inventory) {
		this.inventory = inventory;
	}


    
    
}
