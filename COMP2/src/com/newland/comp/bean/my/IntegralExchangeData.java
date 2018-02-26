package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 积分兑换  data
 * @author H81
 *
 */
public class IntegralExchangeData implements Serializable{


	public String prize_name;//	奖品名字
	public String intergral;//	所需积分
	public String less_num;//	剩余数量
	public String prize_id	;//奖品id
	public String prize_img	;//奖品图片	
	public String all_num;//	总兑换量
	public String isLeaveWord;//		是否需要留言（0：否，1：是）
	
	

	public String getIsLeaveWord() {
		return isLeaveWord;
	}
	public void setIsLeaveWord(String isLeaveWord) {
		this.isLeaveWord = isLeaveWord;
	}	
	public String getAll_num() {
		return all_num;
	}
	public void setAll_num(String all_num) {
		this.all_num = all_num;
	}
	public String getPrize_name() {
		return prize_name;
	}
	public void setPrize_name(String prize_name) {
		this.prize_name = prize_name;
	}
	public String getIntergral() {
		return intergral;
	}
	public void setIntergral(String intergral) {
		this.intergral = intergral;
	}
	public String getLess_num() {
		return less_num;
	}
	public void setLess_num(String less_num) {
		this.less_num = less_num;
	}
	public String getPrize_id() {
		return prize_id;
	}
	public void setPrize_id(String prize_id) {
		this.prize_id = prize_id;
	}
	public String getPrize_img() {
		return prize_img;
	}
	public void setPrize_img(String prize_img) {
		this.prize_img = prize_img;
	}
	
	
}
