package com.newland.comp.bean.indicator;

import java.io.Serializable;

/**
 * 运营指标列表界面 item实体类
 * 
 * @author H81
 *
 */
public class IndicatorOperationItem implements Serializable {

	private String indicator_id;
	private String indicator_name;// 指标名称
	private String indicator_value;// 指标当前值
	private String indicator_rang; // 指标合理区间
	private String tb; // 同比
	private String hb; // 环比
	private String series_data; // 折线数据值逗号分隔，没有用0填充。7.0, 6, 9.5, 14.5, 18.4

	public String getIndicator_name() {
		return indicator_name;
	}

	public void setIndicator_name(String indicator_name) {
		this.indicator_name = indicator_name;
	}

	public String getIndicator_value() {
		return indicator_value;
	}

	public void setIndicator_value(String indicator_value) {
		this.indicator_value = indicator_value;
	}

	public String getIndicator_rang() {
		return indicator_rang;
	}

	public void setIndicator_rang(String indicator_rang) {
		this.indicator_rang = indicator_rang;
	}

	public String getTb() {
		return tb;
	}

	public void setTb(String tb) {
		this.tb = tb;
	}

	public String getHb() {
		return hb;
	}

	public void setHb(String hb) {
		this.hb = hb;
	}

	public String getSeries_data() {
		return series_data;
	}

	public void setSeries_data(String series_data) {
		this.series_data = series_data;
	}

	public String getIndicator_id() {
		return indicator_id;
	}

	public void setIndicator_id(String indicator_id) {
		this.indicator_id = indicator_id;
	}

}
