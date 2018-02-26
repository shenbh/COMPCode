package com.newland.comp.bean.my;


import java.io.Serializable;

/**
 * 我的-请假统计
 * @author H81
 *
 */
public class LeaveCountExp implements Serializable {
	public String qj_sum;//	请假统计
	public String char_x;//		图表x值  ,分隔
	public String char_y;//		图表y值
	public String getQj_sum() {
		return qj_sum;
	}
	public void setQj_sum(String qj_sum) {
		this.qj_sum = qj_sum;
	}
	public String getChar_x() {
		return char_x;
	}
	public void setChar_x(String char_x) {
		this.char_x = char_x;
	}
	public String getChar_y() {
		return char_y;
	}
	public void setChar_y(String char_y) {
		this.char_y = char_y;
	}
	
	
//	新增月份字段
	public String date;
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date=date;
	}
	
	
	
}
