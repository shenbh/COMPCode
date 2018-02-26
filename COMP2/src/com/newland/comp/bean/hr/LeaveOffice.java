package com.newland.comp.bean.hr;

import java.io.Serializable;

/**人资-离职
 * @author H81
 *
 * 2015年4月27日 下午2:14:39
 * @version 1.0.0
 */
public class LeaveOffice implements Serializable{

	public String guid	;//离职移交指南
	public String username	;//员工姓名
	public String userno;//	员工编号
	public String male;//	性别
	public String edu;//	学历
	public String Identity_card	;//身份证
	public String dep;//	所属部门
	public String userType;//	用工类型
	public String jobName;//职位
	public String joinUnitTime;//入司时间
	public String Leave_type_id;//	离职类型选项框id ，分隔
	public String Leave_type_val;//	离职类型选项框val，分隔
	public String reason_first_id;//	离职主要原因选项框id ，分隔
	public String reason_first_val	;//离职主要原因选项框val，分隔
	public String reason_sce_id	;//离职次要原因选项框id ，分隔
	public String reason_sce_val;//	离职次要原因选项框val，分隔
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserno() {
		return userno;
	}
	public void setUserno(String userno) {
		this.userno = userno;
	}
	public String getMale() {
		return male;
	}
	public void setMale(String male) {
		this.male = male;
	}
	public String getEdu() {
		return edu;
	}
	public void setEdu(String edu) {
		this.edu = edu;
	}
	public String getIdentity_card() {
		return Identity_card;
	}
	public void setIdentity_card(String identity_card) {
		Identity_card = identity_card;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getLeave_type_id() {
		return Leave_type_id;
	}
	public void setLeave_type_id(String leave_type_id) {
		Leave_type_id = leave_type_id;
	}
	public String getLeave_type_val() {
		return Leave_type_val;
	}
	public void setLeave_type_val(String leave_type_val) {
		Leave_type_val = leave_type_val;
	}
	public String getReason_first_id() {
		return reason_first_id;
	}
	public void setReason_first_id(String reason_first_id) {
		this.reason_first_id = reason_first_id;
	}
	public String getReason_first_val() {
		return reason_first_val;
	}
	public void setReason_first_val(String reason_first_val) {
		this.reason_first_val = reason_first_val;
	}
	public String getReason_sce_id() {
		return reason_sce_id;
	}
	public void setReason_sce_id(String reason_sce_id) {
		this.reason_sce_id = reason_sce_id;
	}
	public String getReason_sce_val() {
		return reason_sce_val;
	}
	public void setReason_sce_val(String reason_sce_val) {
		this.reason_sce_val = reason_sce_val;
	}
	
	
	
}
