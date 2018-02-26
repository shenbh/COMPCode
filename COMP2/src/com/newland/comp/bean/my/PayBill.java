package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 工资单 bean
 * 
 * @author H81
 *
 */
public class PayBill implements Serializable {

	private String pay_id;// 金额明细id

	private String pay_level;// 金额明细所在级别 顶层菜单就level=0 展开项下的level=1 以此类推

	private String pid;// 金额明细子节点在那个展开项下，pid的数值就是 上一级菜单的id，如果是顶级菜单就是null
	private String pay_value;// 菜单名称
	private String pay_num;// 菜单名金额

	public String getPay_id() {
		return pay_id;
	}

	public void setPay_id(String pay_id) {
		this.pay_id = pay_id;
	}

	public String getPay_level() {
		return pay_level;
	}

	public void setPay_level(String pay_level) {
		this.pay_level = pay_level;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPay_value() {
		return pay_value;
	}

	public void setPay_value(String pay_value) {
		this.pay_value = pay_value;
	}

	public String getPay_num() {
		return pay_num;
	}

	public void setPay_num(String pay_num) {
		this.pay_num = pay_num;
	}

}
