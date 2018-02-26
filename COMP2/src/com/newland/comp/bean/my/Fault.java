package com.newland.comp.bean.my;

/**
 * 过失扣款记录
 * 
 * @author H81
 *
 */
public class Fault {

	private String fault_id;// 过失扣款记录id 放在data数组里
	private String fault_type;// 过失类型 放在data数组里
	private String fault_level;// 过失等级 放在data数组里
	private String fault_num; // 例数

	public String getFault_id() {
		return fault_id;
	}

	public void setFault_id(String fault_id) {
		this.fault_id = fault_id;
	}

	public String getFault_type() {
		return fault_type;
	}

	public void setFault_type(String fault_type) {
		this.fault_type = fault_type;
	}

	public String getFault_level() {
		return fault_level;
	}

	public void setFault_level(String fault_level) {
		this.fault_level = fault_level;
	}

	public String getFault_num() {
		return fault_num;
	}

	public void setFault_num(String fault_num) {
		this.fault_num = fault_num;
	}

}
