package com.newland.comp.bean.indicator;
/**
 * 指标关联明细data
 * @author H81
 *
 */
public class IndicatorAssExp {

	public String  zb_name;//	指标名
	public String zb_id;//	指标id
	public String value;//	当前值
	public String time;//	时间
	public String loop;//	环
	public String rang;//	合理区间
	public String rang_explain;	//合理区间比较
	public String tb;//同
	public String from	;//数据来源
	public String line_x;//	图表x值（默认7天数据）1,2,3,4,5

	public String line_y;//	图表y值（默认7天数据）13,25,43,43,55

	public String custom_indicator;// 	指标定义
	public String division;//	制定口径科室
	public String rule	;//取数规则
		
	public String hw1_x	;//话务1室x值信息
	public String hw1_y	;//话务1室y值信息
	public String hw2_x	;//话务2室x值信息
	public String hw2_y	;//话务2室y值信息
	public String center_value;//	中心百分比值
	public String hw1_value;//	话务1室百分比值
	public String hw2_value;//	话务2室百分比值
	public String gh_value	;//工会百分比值
	
	
	
	/**
	 * 用于圖表顯示的字段
	 * @return
	 */
	public String new_x	;//x值信息  组装后
	public String hw1_new_y	;//话务1室x值信息  组装后
	public String hw2_new_y	;//话务2室y值信息  组装后
	
	public String getRang_explain() {
		return rang_explain;
	}
	public void setRang_explain(String rang_explain) {
		this.rang_explain = rang_explain;
	}
	public String getZb_name() {
		return zb_name;
	}
	public void setZb_name(String zb_name) {
		this.zb_name = zb_name;
	}
	public String getZb_id() {
		return zb_id;
	}
	public void setZb_id(String zb_id) {
		this.zb_id = zb_id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLoop() {
		return loop;
	}
	public void setLoop(String loop) {
		this.loop = loop;
	}
	public String getRang() {
		return rang;
	}
	public void setRang(String rang) {
		this.rang = rang;
	}
	public String getTb() {
		return tb;
	}
	public void setTb(String tb) {
		this.tb = tb;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getLine_x() {
		return line_x;
	}
	public void setLine_x(String line_x) {
		this.line_x = line_x;
	}
	public String getLine_y() {
		return line_y;
	}
	public void setLine_y(String line_y) {
		this.line_y = line_y;
	}
	public String getCustom_indicator() {
		return custom_indicator;
	}
	public void setCustom_indicator(String custom_indicator) {
		this.custom_indicator = custom_indicator;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getHw1_x() {
		return hw1_x;
	}
	public void setHw1_x(String hw1_x) {
		this.hw1_x = hw1_x;
	}
	public String getHw1_y() {
		return hw1_y;
	}
	public void setHw1_y(String hw1_y) {
		this.hw1_y = hw1_y;
	}
	public String getHw2_x() {
		return hw2_x;
	}
	public void setHw2_x(String hw2_x) {
		this.hw2_x = hw2_x;
	}
	public String getHw2_y() {
		return hw2_y;
	}
	public void setHw2_y(String hw2_y) {
		this.hw2_y = hw2_y;
	}
	public String getCenter_value() {
		return center_value;
	}
	public void setCenter_value(String center_value) {
		this.center_value = center_value;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getHw1_value() {
		return hw1_value;
	}
	public void setHw1_value(String hw1_value) {
		this.hw1_value = hw1_value;
	}
	public String getHw2_value() {
		return hw2_value;
	}
	public void setHw2_value(String hw2_value) {
		this.hw2_value = hw2_value;
	}
	public String getGh_value() {
		return gh_value;
	}
	public void setGh_value(String gh_value) {
		this.gh_value = gh_value;
	}
	
}
