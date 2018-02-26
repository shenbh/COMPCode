package com.newland.comp.bean.indicator;

import java.io.Serializable;
import java.util.List;
/**
 * 指标详情 新接口
 * @author H81
 *
 */
public class IndicatorAssResultData implements Serializable{

	public String value	;//当前值
	public String loop	;//环
	public String rang	;//合理区间
	public String rang_explain	;//合理区间比较
	public String tb	;//同
	public String from	;//数据来源
	public String custom_indicator;// 	指标定义
	public String division	;//制定口径科室
	public String rule	;//取数规则
	
	
	public String data_type;
	public String  zb_id; 

	public List<IndicatorAssResultDataChart> trendDataList	;//指标趋势图数据
	public List<IndicatorAssResultDataChart> childDataList;//	分科室区域数据
	public List<IndicatorAssResultDataRelation> relationDataList;//	关联数据
	
	//用于webview字段，非接口返回
	public String zb_name;  //指标名
	
	
	
	public String getZb_name() {
		return zb_name;
	}
	public void setZb_name(String zb_name) {
		this.zb_name = zb_name;
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
	public String getRang_explain() {
		return rang_explain;
	}
	public void setRang_explain(String rang_explain) {
		this.rang_explain = rang_explain;
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
	public List<IndicatorAssResultDataChart> getTrendDataList() {
		return trendDataList;
	}
	public void setTrendDataList(List<IndicatorAssResultDataChart> trendDataList) {
		this.trendDataList = trendDataList;
	}
	public List<IndicatorAssResultDataChart> getChildDataList() {
		return childDataList;
	}
	public void setChildDataList(List<IndicatorAssResultDataChart> childDataList) {
		this.childDataList = childDataList;
	}
	public List<IndicatorAssResultDataRelation> getRelationDataList() {
		return relationDataList;
	}
	public void setRelationDataList(
			List<IndicatorAssResultDataRelation> relationDataList) {
		this.relationDataList = relationDataList;
	}
	
	
	
	
	
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getZb_id() {
		return zb_id;
	}
	public void setZb_id(String zb_id) {
		this.zb_id = zb_id;
	}
}
