package com.newland.comp.bean;

import java.io.Serializable;

/**
 * 确认扣款记录
 * 
 * @author H81
 *
 */
public class Confirm implements Serializable {
	private String confirm_id;// 确认差扣款记录id 放在data2数组里
	private String confirm_type;// 差错类型 放在data2数组里
	private String origin_num;// 原始倒数 放在data2数组里
	private String redun_num;// 冗余倒数 放在data2数组里
	private String check_num;// 考核倒数 放在data2数组里

	public String getConfirm_id() {
		return confirm_id;
	}

	public void setConfirm_id(String confirm_id) {
		this.confirm_id = confirm_id;
	}

	public String getConfirm_type() {
		return confirm_type;
	}

	public void setConfirm_type(String confirm_type) {
		this.confirm_type = confirm_type;
	}

	public String getOrigin_num() {
		return origin_num;
	}

	public void setOrigin_num(String origin_num) {
		this.origin_num = origin_num;
	}

	public String getRedun_num() {
		return redun_num;
	}

	public void setRedun_num(String redun_num) {
		this.redun_num = redun_num;
	}

	public String getCheck_num() {
		return check_num;
	}

	public void setCheck_num(String check_num) {
		this.check_num = check_num;
	}

}
