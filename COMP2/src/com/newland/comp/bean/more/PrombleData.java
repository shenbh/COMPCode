package com.newland.comp.bean.more;


import java.io.Serializable;

/**
 * 我的问题列表
 * @author H81
 *
 */
public class PrombleData implements Serializable{
	public String pro_id;//	问题id
	public String title;//	问题标题
	public String statue;//	问题状态
	public String current ;//	当前环节
	public String time	;//提问时间
	public String getPro_id() {
		return pro_id;
	}
	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatue() {
		return statue;
	}
	public void setStatue(String statue) {
		this.statue = statue;
	}
	public String getCurrent() {
		return current;
	}
	public void setCurrent(String current) {
		this.current = current;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
