package com.newland.comp.bean.my;

/**
 * 考勤明细
 * 
 * @author H81
 * 
 */
public class FilingDetailData {
	public String kq_time;// 考勤日期
	public String dur_time;// 时长
	public String kq_type;// 考勤过失级别

	public String getKq_time() {
		return kq_time;
	}

	public void setKq_time(String kq_time) {
		this.kq_time = kq_time;
	}

	public String getDur_time() {
		return dur_time;
	}

	public void setDur_time(String dur_time) {
		this.dur_time = dur_time;
	}

	public String getKq_type() {
		return kq_type;
	}

	public void setKq_type(String kq_type) {
		this.kq_type = kq_type;
	}

}
