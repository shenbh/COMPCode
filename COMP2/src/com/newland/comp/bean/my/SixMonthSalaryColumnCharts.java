package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 工资单界面 item实体类
 * 
 * @author H81
 *
 */
public class SixMonthSalaryColumnCharts implements Serializable {
	private String month_series_data; // 数据值逗号分隔，没有用0填充。2015/01,
										// 2015/02,2015/03, 2015/04,
										// 2015/05,2015/06
	private String money_series_data; // 数据值逗号分隔，没有用0填充。2355, 2355, 2355, 2355,
										// 2355,2355

	public String getMonth_series_data() {
		return month_series_data;
	}

	public void setMonth_series_data(String month_series_data) {
		this.month_series_data = month_series_data;
	}

	public String getMoney_series_data() {
		return money_series_data;
	}

	public void setMoney_series_data(String money_series_data) {
		this.money_series_data = money_series_data;
	}

}
