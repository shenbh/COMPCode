package com.newland.comp.bean.my;

/**
 * 考勤报备
 * 
 * @author H81
 * 
 */
public class FilingData {
	public String start_time;// 考勤报备开始时间
	public String end_time;// 考勤报备结束时间
	public String type;// 考勤报备申请类型
	public String reason;// 考勤报备原因
	public String sta;// 考勤报备审批状态

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSta() {
		return sta;
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

}
