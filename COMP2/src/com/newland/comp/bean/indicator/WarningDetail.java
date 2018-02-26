package com.newland.comp.bean.indicator;

import java.io.Serializable;

/***
 * 预警信息 详细
 * @author Administrator
 *
 */
public class WarningDetail implements Serializable{

	public String warn_time;//	日期
	public String warn_type;//		事件类型
	public String warn_content;//		内容
	public String getWarn_time() {
		return warn_time;
	}
	public void setWarn_time(String warn_time) {
		this.warn_time = warn_time;
	}
	public String getWarn_type() {
		return warn_type;
	}
	public void setWarn_type(String warn_type) {
		this.warn_type = warn_type;
	}
	public String getWarn_content() {
		return warn_content;
	}
	public void setWarn_content(String warn_content) {
		this.warn_content = warn_content;
	}
	
	
	
}
