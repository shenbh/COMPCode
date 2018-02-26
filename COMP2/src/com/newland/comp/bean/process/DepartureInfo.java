package com.newland.comp.bean.process;

import java.io.Serializable;

/**离职审批详情（用于显示）
 * @author Administrator
 *
 */
public class DepartureInfo implements Serializable {
	public String staffName;// 员工姓名（界面显示）
	public String staffId;// 员工编号（界面显示）
	public String identitycardNumber;// 身份证（界面显示）
	public String gender;// 性别（界面显示）
	public String educational;// 学历（界面显示）
	public String organName;// 部门（界面显示）
	public String userType;// 用工类型（界面显示）
	public String jobName;// 职位（界面显示）
	public String joinUnitTime;// 入司时间（界面显示）
	public String departureDate;// 拟离职时间（界面显示）
	public String departureType;// 离职类型（界面显示）
	public String description;// 离职备注（界面显示）
	public String flowId;// 流程ID
	public String taskId;// 任务ID
	public String flowCode;// 流程标识
	public String bizId;// 请假单ID
	public String formPage;// 流程页面
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getIdentitycardNumber() {
		return identitycardNumber;
	}
	public void setIdentitycardNumber(String identitycardNumber) {
		this.identitycardNumber = identitycardNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEducational() {
		return educational;
	}
	public void setEducational(String educational) {
		this.educational = educational;
	}
	public String getOrganName() {
		return organName;
	}
	public void setOrganName(String organName) {
		this.organName = organName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJoinUnitTime() {
		return joinUnitTime;
	}
	public void setJoinUnitTime(String joinUnitTime) {
		this.joinUnitTime = joinUnitTime;
	}
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	public String getDepartureType() {
		return departureType;
	}
	public void setDepartureType(String departureType) {
		this.departureType = departureType;
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
