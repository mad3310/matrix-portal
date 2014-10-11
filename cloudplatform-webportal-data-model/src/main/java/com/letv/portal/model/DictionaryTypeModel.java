/*
 * @Title: DictionaryTypeModel.java
 * @Package com.letv.mms.model
 * @Description: 字典分类数据模型
 * @author 陈光
 * @date 2012-12-5 下午5:55:25
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5                          
 */
package com.letv.portal.model;

public class DictionaryTypeModel extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3986685236553403900L;
	private String name;// 分类名称
	private String code;// 名称的英文表示
	private Integer type;//
	private Integer level;// 使用状态
	private Integer staus;//
	private Integer isReference;//
	private String dataStructure;//

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStaus() {
		return this.staus;
	}

	public void setStaus(Integer staus) {
		this.staus = staus;
	}

	public Integer getIsReference() {
		return this.isReference;
	}

	public void setIsReference(Integer isReference) {
		this.isReference = isReference;
	}

	public String getDataStructure() {
		return this.dataStructure;
	}

	public void setDataStructure(String dataStructure) {
		this.dataStructure = dataStructure;
	}

	public String toString() {
		return "ID:" + super.getId().toString() + " Name:" + this.name + " Code:" + this.code
				+ " Type:" + this.type;
	}

}
