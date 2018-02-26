package com.newland.comp.bean.indicator;

import java.io.Serializable;
import java.util.List;
/**
 *  指标详情 新接口 图表集合
 * @author H81
 *
 */
public class IndicatorAssResultDataChart implements Serializable{

	public String XData	;//X轴数据
	public String YData	;//Y轴数据
	public String key;//	关联指标分析id  也就是日期
	public String hoverText;//	说明
	public String data_type;//	数据类型（1-小数 2-百分比 3-整数） 
	
	public List<IndicatorAssResultDataChart> childList;//	子节点数据
	
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHoverText() {
		return hoverText;
	}
	public void setHoverText(String hoverText) {
		this.hoverText = hoverText;
	}

	public String getXData() {
		return XData;
	}
	public void setXData(String xData) {
		XData = xData;
	}
	public String getYData() {
		return YData;
	}
	public void setYData(String yData) {
		YData = yData;
	}
	
	
	
}
