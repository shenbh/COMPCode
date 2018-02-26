package com.newland.comp.bean.indicator;

import java.io.Serializable;

/***
 * 预警信息
 * @author Administrator
 *
 */
public class Warning implements Serializable{

	public String hw_warn;//	话务预警数
	public String  fault_warn	;//故障情况数
	public String  complaint;//	投诉情况数
	public String getHw_warn() {
		return hw_warn;
	}
	public void setHw_warn(String hw_warn) {
		this.hw_warn = hw_warn;
	}
	public String getFault_warn() {
		return fault_warn;
	}
	public void setFault_warn(String fault_warn) {
		this.fault_warn = fault_warn;
	}
	public String getComplaint() {
		return complaint;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	
	
	
}
