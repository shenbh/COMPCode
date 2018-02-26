package com.newland.comp.bean.hr;

import java.io.Serializable;

/**
 * 人资-请假数据
 * 
 * @author H81
 *
 */
public class LeaveData implements Serializable {
	public String commission_id;// 是否代办选项框列表id 以，分隔
	public String commission_val;// 是否代办选项框列表中文值 以，分隔
	public String leave_type_id;// 请假类型id ;//以，分隔
	public String leave_type_val;// 请假类型框列表中文值 以，分隔
	public String outside_id;// 是否离开本省id，分隔
	public String outside_val;// 是否离开本省值 以，分隔
	public String userno;// 员工编号
	public String username;// 员工姓名
	public String dep;// 员工部门
	public String reason_id;// 请假事由id
	public String reason_val;// 请假事由val
	public String year_hol;// 年假总额天数
	public String used_hol;// 已申请天数
	public String ava_hol;// 可用天数

	public String getCommission_id() {
		return commission_id;
	}

	public void setCommission_id(String commission_id) {
		this.commission_id = commission_id;
	}

	public String getCommission_val() {
		return commission_val;
	}

	public void setCommission_val(String commission_val) {
		this.commission_val = commission_val;
	}

	public String getLeave_type_id() {
		return leave_type_id;
	}

	public void setLeave_type_id(String leave_type_id) {
		this.leave_type_id = leave_type_id;
	}

	public String getLeave_type_val() {
		return leave_type_val;
	}

	public void setLeave_type_val(String leave_type_val) {
		this.leave_type_val = leave_type_val;
	}

	public String getOutside_id() {
		return outside_id;
	}

	public void setOutside_id(String outside_id) {
		this.outside_id = outside_id;
	}

	public String getOutside_val() {
		return outside_val;
	}

	public void setOutside_val(String outside_val) {
		this.outside_val = outside_val;
	}

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getReason_id() {
		return reason_id;
	}

	public void setReason_id(String reason_id) {
		this.reason_id = reason_id;
	}

	public String getReason_val() {
		return reason_val;
	}

	public void setReason_val(String reason_val) {
		this.reason_val = reason_val;
	}

	public String getYear_hol() {
		return year_hol;
	}

	public void setYear_hol(String year_hol) {
		this.year_hol = year_hol;
	}

	public String getUsed_hol() {
		return used_hol;
	}

	public void setUsed_hol(String used_hol) {
		this.used_hol = used_hol;
	}

	public String getAva_hol() {
		return ava_hol;
	}

	public void setAva_hol(String ava_hol) {
		this.ava_hol = ava_hol;
	}

}
