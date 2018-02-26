package com.newland.comp.bean;

import java.io.Serializable;
/**
 * 问题类型
 * @author Tony
 *
 */
public class PrombleType implements Serializable{
	public String typeId;// 类型id
	public String parentType;// 	父类型id 父级id
	public String typeName	;// 类型名称
	public String isLeaf;// 	是否叶子节点 0-否 1-是
	public String id;
	public String remark;// 
	public String topId ;//  顶层父节点	类型说明
	
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTopId() {
		return topId;
	}
	public void setTopId(String topId) {
		this.topId = topId;
	}
}
