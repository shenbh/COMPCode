package com.newland.comp.bean.my;

import java.io.Serializable;


/**
 * 考勤统计
 * 
 * @author H81
 * 
 */
public class FilingTotalExp implements Serializable{
	public String char_x;//	图表x值  ,分隔
	public String char_y;//	图表y值
	public String late_num	;//迟点次数
	public String level_num	;//早退次数
	public String absent_num	;//旷工天数
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
	public String getLate_num() {
		return late_num;
	}
	public void setLate_num(String late_num) {
		this.late_num = late_num;
	}
	public String getLevel_num() {
		return level_num;
	}
	public void setLevel_num(String level_num) {
		this.level_num = level_num;
	}
	public String getAbsent_num() {
		return absent_num;
	}
	public void setAbsent_num(String absent_num) {
		this.absent_num = absent_num;
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
