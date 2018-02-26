package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 工资单 图表bean
 * 
 * @author H81
 *
 */
public class PayBillExp implements Serializable {

	private String histogram_value;// 近6个月薪酬分析图金额（以，分隔，默认月份顺序从前到后）3000,3600,5000....

	private String histogram_name;// 近6个月薪酬分析图月份（以，分隔，默认月份顺序从前到后）2014-6,2014-7,2014-8....

	private String pie_name;// 当月薪酬结构图，饼状图名称（以，分隔）基本工资,年资,绩效资金....
	private String pie_value;// 当月薪酬结构图，饼状图数值（以，分隔) 1100,60,34.5.......

	public String getHistogram_value() {
		return histogram_value;
	}

	public void setHistogram_value(String histogram_value) {
		this.histogram_value = histogram_value;
	}

	public String getHistogram_name() {
		return histogram_name;
	}

	public void setHistogram_name(String histogram_name) {
		this.histogram_name = histogram_name;
	}

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
