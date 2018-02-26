package com.newland.comp.bean.my;


import java.io.Serializable;
/**
 * 积分秒杀data
 * @author H81
 *
 */
public class IntegralSeckillData implements Serializable{

	
	public String prize_name;//	奖品名称
	public String integral_num;//		所需扣分
	public String prize_id;//		奖品id
	public String prize_img	;//	奖品图片
	public String getPrize_name() {
		return prize_name;
	}
	public void setPrize_name(String prize_name) {
		this.prize_name = prize_name;
	}
	public String getIntegral_num() {
		return integral_num;
	}
	public void setIntegral_num(String integral_num) {
		this.integral_num = integral_num;
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
