package com.newland.comp.bean.my;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 积分秒杀exp
 * @author H81
 *
 */
public class IntegralSeckillExp implements Serializable{

	
	public String activity_name	;//秒杀活动名称
	public String activity_explain;//	活动说明
	public String seckill_time;//	秒杀时间
	public String less_integral	;//积分余额
	public String next_time;//	下一场开始时间
	
	public String seckillBoutId;// 秒杀场次ID
	public String seckillId;//	            秒杀活动ID 
	public String activityId;//	主活动ID


	public ArrayList<IntegralSeckillData> premiumList; //奖品列表（数组）
	
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	public String getActivity_explain() {
		return activity_explain;
	}
	public void setActivity_explain(String activity_explain) {
		this.activity_explain = activity_explain;
	}
	public String getSeckill_time() {
		return seckill_time;
	}
	public void setSeckill_time(String seckill_time) {
		this.seckill_time = seckill_time;
	}
	public String getLess_integral() {
		return less_integral;
	}
	public void setLess_integral(String less_integral) {
		this.less_integral = less_integral;
	}
	public String getNext_time() {
		return next_time;
	}
	public void setNext_time(String next_time) {
		this.next_time = next_time;
	}
	public ArrayList<IntegralSeckillData> getPremiumList() {
		if (premiumList == null) {
			return new ArrayList<IntegralSeckillData>();
		}
		return premiumList;
	}
	public void setPremiumList(ArrayList<IntegralSeckillData> premiumList) {
		this.premiumList = premiumList;
	}
	public String getSeckillBoutId() {
		return seckillBoutId;
	}
	public void setSeckillBoutId(String seckillBoutId) {
		this.seckillBoutId = seckillBoutId;
	}
	public String getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(String seckillId) {
		this.seckillId = seckillId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	
}
