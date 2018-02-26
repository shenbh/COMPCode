package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 考勤
 * 
 * @author Administrator
 *
 */
public class CheckIn implements Serializable {

	private String clocking_id;// 考勤项id
	private String clocking_exp;// 考勤项说明
	private String clocking_result;// 考勤项结果

	public String getClocking_id() {
		return clocking_id;
	}

	public void setClocking_id(String clocking_id) {
		this.clocking_id = clocking_id;
	}

	public String getClocking_exp() {
		return clocking_exp;
	}

	public void setClocking_exp(String clocking_exp) {
		this.clocking_exp = clocking_exp;
	}

	public String getClocking_result() {
		return clocking_result;
	}

	public void setClocking_result(String clocking_result) {
		this.clocking_result = clocking_result;
	}

}
