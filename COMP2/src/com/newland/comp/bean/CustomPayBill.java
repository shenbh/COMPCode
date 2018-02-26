package com.newland.comp.bean;

import java.io.Serializable;
import java.util.List;

import com.newland.comp.bean.my.PayBill;

public class CustomPayBill implements Serializable {
	private PayBill payBill;
	private List<PayBill> childPayBills;

	public PayBill getPayBill() {
		return payBill;
	}

	public void setPayBill(PayBill payBill) {
		this.payBill = payBill;
	}

	public List<PayBill> getChildPayBills() {
		return childPayBills;
	}

	public void setChildPayBills(List<PayBill> childPayBills) {
		this.childPayBills = childPayBills;
	}

}
