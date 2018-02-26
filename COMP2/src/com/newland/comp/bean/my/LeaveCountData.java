package com.newland.comp.bean.my;


import java.io.Serializable;

/**
 *我的- 请假统计
 * @author H81
 *
 */
public class LeaveCountData implements Serializable {
	
	public String start_time;//	开始时间
	public String end_time;//	结束时间
	public String qj_type;//	请假类型
	public String dur_time;//	请假时长
	public String length_unit;//	时长单位
	
	
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getQj_type() {
		return qj_type;
	}
	public void setQj_type(String qj_type) {
		this.qj_type = qj_type;
	}
	public String getDur_time() {
		return dur_time;
	}
	public void setDur_time(String dur_time) {
		this.dur_time = dur_time;
	}
	public String getLength_unit() {
		return length_unit;
	}
	public void setLength_unit(String length_unit) {
		this.length_unit = length_unit;
	}
	
	
}
