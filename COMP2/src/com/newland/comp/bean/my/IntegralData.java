package com.newland.comp.bean.my;

import java.io.Serializable;

/**我的积分
 * @author H81
 *
 * 2015年4月24日 下午3:02:18
 * @version 1.0.0
 */
public class IntegralData implements Serializable{

	public String integral_id;//	积分id
	public String get_time;//		获取时间
	public String pro_name;//		项目名称
	public String pro_level;//		项目级别
	public String pro_num;//		项目分值
	public String multiple;//		倍数
	public String integral;//		积分分值
	public String integral_from;//		积分获取来源
	
	public String getIntegral_id() {
		return integral_id;
	}
	public void setIntegral_id(String integral_id) {
		this.integral_id = integral_id;
	}
	public String getGet_time() {
		return get_time;
	}
	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	public String getPro_level() {
		return pro_level;
	}
	public void setPro_level(String pro_level) {
		this.pro_level = pro_level;
	}
	public String getPro_num() {
		return pro_num;
	}
	public void setPro_num(String pro_num) {
		this.pro_num = pro_num;
	}
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getIntegral_from() {
		return integral_from;
	}
	public void setIntegral_from(String integral_from) {
		this.integral_from = integral_from;
	}
	
	
}
