package com.newland.comp.bean.hr;

import java.io.Serializable;

/**请假审批详情（用于界面显示）
 * @author Administrator
 *
 */
public class LeaveInfo implements Serializable{
	public String mis;// 员工编号（界面显示）
	public String username;// 员工姓名（界面显示）
	public String organName;// 部门名称（界面显示）
	public String begineTime;// 开始时间（界面显示）
	public String endTime;// 结束时间（界面显示）
	public String lengthUnit;// 请假时长（界面显示）
	public String type;// 请假类型（界面显示）
	public String outOf;// 是否离开省市（界面显示）
	public String fillStaffOrg;// 代办人部门（界面显示）
	public String fillStaffName;// 代办人姓名（界面显示）
	public String fillStaffMis;// 代办人编号（界面显示）
	public String description;// 请假原因（界面显示）
	public String flowId;// 流程ID
	public String taskId;// 任务ID
	public String flowCode;// 流程标识
	public String bizId;// 请假单ID
	public String formPage;// 流程页面
	public String getMis() {
		return mis;
	}
	public void setMis(String mis) {
		this.mis = mis;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getBegineTime() {
		return begineTime;
	}
	public void setBegineTime(String begineTime) {
		this.begineTime = begineTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLengthUnit() {
		return lengthUnit;
	}
	public void setLengthUnit(String lengthUnit) {
		this.lengthUnit = lengthUnit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOutOf() {
		return outOf;
	}
	public void setOutOf(String outOf) {
		this.outOf = outOf;
	}
	public String getFillStaffOrg() {
		return fillStaffOrg;
	}
	public void setFillStaffOrg(String fillStaffOrg) {
		this.fillStaffOrg = fillStaffOrg;
	}
	public String getFillStaffName() {
		return fillStaffName;
	}
	public void setFillStaffName(String fillStaffName) {
		this.fillStaffName = fillStaffName;
	}
	public String getFillStaffMis() {
		return fillStaffMis;
	}
	public void setFillStaffMis(String fillStaffMis) {
		this.fillStaffMis = fillStaffMis;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getFormPage() {
		return formPage;
	}
	public void setFormPage(String formPage) {
		this.formPage = formPage;
	}

}
