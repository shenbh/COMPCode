package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 加班统计
 * 
 * @author H81
 * 
 */
@SuppressWarnings("serial")
public class OverTimeCountData implements Serializable {

	public String kq_time;// 考勤日期
	public String work_time;// 加班时长
	public String work_type;// 加班类型

	public String getKq_time() {
		return kq_time;
	}

	public void setKq_time(String kq_time) {
		this.kq_time = kq_time;
	}

	public String getWork_time() {
		return work_time;
	}

	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}

	public String getWork_type() {
		return work_type;
	}

	public void setWork_type(String work_type) {
		this.work_type = work_type;
	}

}
