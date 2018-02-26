package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 服务质量
 * @author H81
 *
 */
public class PayrollQos implements Serializable{
	private String  examine_id;//	考核项名称id
	private String examine_pid;//	考核项名称上级父节点id  如果是第一级就是0

	private String examine_level;//	考核项名称所在level层级，0,1,2,3

	private String examine_value;//	考核项的名称
	private String examine_num;//	考核项服务质量系数
	private String origin_examine;//	原始考核项
	private String cal_explain;//	计算说明examine_pid	考核项名称上级父节点id  如果是第一级就是0
	public String getExamine_id() {
		return examine_id;
	}
	public void setExamine_id(String examine_id) {
		this.examine_id = examine_id;
	}
	public String getExamine_pid() {
		return examine_pid;
	}
	public void setExamine_pid(String examine_pid) {
		this.examine_pid = examine_pid;
	}
	public String getExamine_level() {
		return examine_level;
	}
	public void setExamine_level(String examine_level) {
		this.examine_level = examine_level;
	}
	public String getExamine_value() {
		return examine_value;
	}
	public void setExamine_value(String examine_value) {
		this.examine_value = examine_value;
	}
	public String getExamine_num() {
		return examine_num;
	}
	public void setExamine_num(String examine_num) {
		this.examine_num = examine_num;
	}
	public String getOrigin_examine() {
		return origin_examine;
	}
	public void setOrigin_examine(String origin_examine) {
		this.origin_examine = origin_examine;
	}
	public String getCal_explain() {
		return cal_explain;
	}
	public void setCal_explain(String cal_explain) {
		this.cal_explain = cal_explain;
	}

	
	
}
