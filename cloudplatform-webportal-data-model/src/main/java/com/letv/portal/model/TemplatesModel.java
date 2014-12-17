package com.letv.portal.model;

public class TemplatesModel {
  private String templateid;
  public TemplatesModel(){
	  this.templateid ="10108"; 
  }
  //添加构造函数，传入模板id 20141217 by liuhao
  public TemplatesModel(String templateId){
	  this.templateid = templateId; 
  }
public String getTemplateid() {
	return templateid;
}

public void setTemplateid(String templateid) {
	this.templateid = templateid;
}
  
}
