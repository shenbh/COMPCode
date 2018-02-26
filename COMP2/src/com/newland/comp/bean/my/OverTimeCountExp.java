package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 加班统计
 * 
 * @author H81
 * 
 */
public class OverTimeCountExp implements Serializable {

	public String jb_sum;// 加班总计
	public String char_x;// 图表x值 ,分隔
	public String char_y;// 图表y值

	public String getJb_sum() {
		return jb_sum;
	}

	public void setJb_sum(String jb_sum) {
		this.jb_sum = jb_sum;
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
