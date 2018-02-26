package com.newland.comp.bean.indicator;

import java.io.Serializable;
import java.util.List;

/**trendDataList实体类
 * @author ab
 *
 * 2015年7月24日下午2:52:32
 */
public class IndicatorTrendData implements Serializable{
   /* "XData": "2015-07-17",
    "YData": "0.0",
    "childList": "",
    "hoverText": "",
    "key": ""*/
	public String XData;
	public String YData;
	public String hoverText;
	public String key;
	public String childList;
	
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
	public String getHoverText() {
		return hoverText;
	}
	public void setHoverText(String hoverText) {
		this.hoverText = hoverText;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getChildList() {
		return childList;
	}
	public void setChildList(String childList) {
		this.childList = childList;
	}
	
	
}
