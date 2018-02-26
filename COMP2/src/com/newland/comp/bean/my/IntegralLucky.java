package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 积分抽奖
 * @author H81
 *
 */
public class IntegralLucky implements Serializable{

	public String activity_name	;//活动名称
	public String less_integral	;//可兑换积分余额
	public String line	;//达标线
	public String activity_time	;//活动时间
	public String activityId;//	主活动ID
	public String lotteryId;//		抽奖活动ID
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getLess_integral() {
		return less_integral;
	}
	public void setLess_integral(String less_integral) {
		this.less_integral = less_integral;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getActivity_time() {
		return activity_time;
	}
	public void setActivity_time(String activity_time) {
		this.activity_time = activity_time;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getLotteryId() {
		return lotteryId;
	}
	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	
}
