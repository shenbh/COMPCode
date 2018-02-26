package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 我的星级 data1
 * @author H81
 *
 */
public class MyStartData2 implements Serializable{

	public String xj_key ;// 1、2···
	public String xj_name ;// 评选前、起评···
	public String xj_value  ;// 一星级、二星级···
	public String xj_remark ;// 星级备注
	public String getXj_key() {
		return xj_key;
	}
	public void setXj_key(String xj_key) {
		this.xj_key = xj_key;
	}
	public String getXj_name() {
		return xj_name;
	}
	public void setXj_name(String xj_name) {
		this.xj_name = xj_name;
	}
	public String getXj_value() {
		return xj_value;
	}
	public void setXj_value(String xj_value) {
		this.xj_value = xj_value;
	}
	public String getXj_remark() {
		return xj_remark;
	}
	public void setXj_remark(String xj_remark) {
		this.xj_remark = xj_remark;
	}
	
	
	
}
