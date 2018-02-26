package com.newland.comp.bean.indicator;

import java.io.Serializable;
import java.util.List;

/**
 * 指标监控
 * 
 * @author H81
 *
 */
public class IndicatorData implements Serializable {

	public String zb_name;//	指标名
	public String zb_id;//	指标id
	public String value;//	当前值
	public String loop;//	环
	public String rang	;//合理区间
	public String tb;//	同
	public String remark;
	public String zb_flag;
	public String data_type;
	public String rang_ecplain;
//	public String line_x;//	图表x值（默认7天数据）1,2,3,4,5
//	private String line_y;//图表y值（默认7天数据）13,25,43,43,55

	public List<IndicatorTrendData> trendDataList	;//指标趋势图数据
	
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

//	public String getLine_x() {
//		return line_x;
//	}
//
//	public void setLine_x(String line_x) {
//		this.line_x = line_x;
//	}
//
//	public String getLine_y() {
//		return line_y;
//	}
//
//	public void setLine_y(String line_y) {
//		this.line_y = line_y;
//	}
	
	public List<IndicatorTrendData> getTrendDataList() {
		return trendDataList;
	}
	public void setTrendDataList(List<IndicatorTrendData> trendDataList) {
		this.trendDataList = trendDataList;
	}

	
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getZb_flag() {
		return zb_flag;
	}

	public void setZb_flag(String zb_flag) {
		this.zb_flag = zb_flag;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getRang_ecplain() {
		return rang_ecplain;
	}

	public void setRang_ecplain(String rang_ecplain) {
		this.rang_ecplain = rang_ecplain;
	}
}
