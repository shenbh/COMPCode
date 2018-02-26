package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 工资单界面 item实体类
 * 
 * @author H81
 *
 */
public class SixMonthSalaryPieCharts implements Serializable {
	private String pie_name; // 当月薪酬结构图，饼状图名称（以，分隔）基本工资,年资,绩效资金....
	private String pie_value;// 当月薪酬结构图，饼状图数值（以，分隔）1100,60,34.5.......

	public String getPie_name() {
		return pie_name;
	}

	public void setPie_name(String pie_name) {
		this.pie_name = pie_name;
	}

	public String getPie_value() {
		return pie_value;
	}

	public void setPie_value(String pie_value) {
		this.pie_value = pie_value;
	}

}
