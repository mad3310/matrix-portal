/*
 * @Title: DictionaryModel.java
 * @Package com.letv.mms.model
 * @Description: 字典信息数据模型
 * @author 陈光
 * @date 2012-12-5 下午5:53:27
 * @version V1.0
 *
 * Modification History:  
 * Date         Author      Version     Description  
 * -------------------------------------------------------------- 
 * 2012-12-5                          
 */
package com.letv.portal.model;

public class DictionaryModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2424522277805333097L;
	private Integer valueId;// 字典信息表id
	private String value;// 字典信息表名称
	private Integer typeId;// 字典信息分类ID
	private Integer parentValueId;// 字典信息表父ID
	private Integer level;// 树中等级
	private Integer status;// 状态

	public Integer getValueId() {
		return this.valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getParentValueId() {
		return this.parentValueId;
	}

	public void setParentValueId(Integer parentValueId) {
		this.parentValueId = parentValueId;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String toString() {
		return "valueId:" + this.valueId + " value:" + this.value + " typeId:"
				+ this.typeId + " parentValueId:" + this.parentValueId;
	}
}
