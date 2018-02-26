package com.newland.comp.bean.process;

import java.io.Serializable;

/**
 * 待办流程+已办流程+发起的流程
 * 
 * @author Administrator
 *
 */
public class ProcessDataComplete implements Serializable {

	public String flowId; // 流程id
	public String flowTitle; // 流程标题
	public String proPerson; // 流程创建人
	public String statue; // 状态
	public String createTime; // 流程创建时间
	public String arrivePro; // 当前处理环节
	public String arriveTime; // 到达本人时间
	public String proType; // 请假：vacate
							// 离职：leave
							// 考勤报备：check_on
							// 其他：others
	public String taskId;
	public String nodeId;
	public String bizId;
	public String flowCode; // 流程编码
	public String defId;
	public String formPage; // 流程页面
	public String flowType; // 流程类型 0：程序定制 1：客户自定义


	public String getProPerson() {
		return proPerson;
	}

	public void setProPerson(String proPerson) {
		this.proPerson = proPerson;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowTitle() {
		return flowTitle;
	}

	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}

	public String getStatue() {
		return statue;
	}

	public void setStatue(String statue) {
		this.statue = statue;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getArrivePro() {
		return arrivePro;
	}

	public void setArrivePro(String arrivePro) {
		this.arrivePro = arrivePro;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getFormPage() {
		return formPage;
	}

	public void setFormPage(String formPage) {
		this.formPage = formPage;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

}
