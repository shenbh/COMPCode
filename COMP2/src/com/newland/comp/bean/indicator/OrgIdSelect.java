package com.newland.comp.bean.indicator;
/**
 * 机构id选择
 * @author H81
 *
 */
public class OrgIdSelect {
	public String org_id;//	机构id

	public String  org_type;//	机构所在类别 2-中心 3-科室 4-区域 5-小组


	public String  org_pid;//	pid的数值就是 上一级菜单的id，如果是顶级菜单就是null
	public String  org_value;//	机构名称
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getOrg_type() {
		return org_type;
	}
	public void setOrg_type(String org_type) {
		this.org_type = org_type;
	}
	public String getOrg_pid() {
		return org_pid;
	}
	public void setOrg_pid(String org_pid) {
		this.org_pid = org_pid;
	}
	public String getOrg_value() {
		return org_value;
	}
	public void setOrg_value(String org_value) {
		this.org_value = org_value;
	}
	
	
}
