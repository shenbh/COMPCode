package com.newland.comp.bean.my;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 积分兑换  exp
 * @author H81
 *
 */
public class IntegralExchangeExp implements Serializable{

	public String activity_name	;//活动名称
	public String activity_time	;//活动时间
	public String exchange_period	;//每天兑换时间
	public String integral_line	;//活动积分达标线
	public String less_integral	;//可用于兑换的积分余额
	public ArrayList<IntegralExchangeData> premiumList; //兑换产品集合
	public String activityId;//		活动ID
	
	
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	public ArrayList<IntegralExchangeData> getPremiumList() {
		return premiumList;
	}
	public void setPremiumList(ArrayList<IntegralExchangeData> premiumList) {
		this.premiumList = premiumList;
	}
	
	
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(String activity_time) {
		this.activity_time = activity_time;
	}
	public String getExchange_period() {
		return exchange_period;
	}
	public void setExchange_period(String exchange_period) {
		this.exchange_period = exchange_period;
	}
	public String getIntegral_line() {
		return integral_line;
	}
	public void setIntegral_line(String integral_line) {
		this.integral_line = integral_line;
	}
	public String getLess_integral() {
		return less_integral;
	}
	public void setLess_integral(String less_integral) {
		this.less_integral = less_integral;
	}
	
	
}
