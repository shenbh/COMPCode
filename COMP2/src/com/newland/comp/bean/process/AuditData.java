package com.newland.comp.bean.process;

import java.io.Serializable;

public class AuditData implements Serializable{
	
	private String name;//节点名称
	private String comment;//审批意见
	private String operatorName;//审批人
	private String endDate;//审批时间
	
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	
	public void setComment(String comment){
		this.comment=comment;
	}
	public String getComment(){
		return comment;
	}
	public void setOperatorName(String operatorName){
		this.operatorName=operatorName;
	}
	public String getOperatorName(){
		return operatorName;
	}
	public void setEndDate(String endDate){
		this.endDate=endDate;
	}
	public String getEndDate(){
		return endDate;
	}
}
