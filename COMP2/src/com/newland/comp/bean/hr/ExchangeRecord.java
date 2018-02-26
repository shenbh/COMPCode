package com.newland.comp.bean.hr;

import java.io.Serializable;
/**
 * 兑换记录
 * @author H81
 *
 */
public class ExchangeRecord implements Serializable{

	public String prize_name;//	奖品名称
	public String exchange_num	;//	、兑换数量、
	public String all_integral;//		本次兑换总积分、
	public String exchange_time;//		消费时间、
	public String type	;//	兑换方式
	
	public String premiumId	;//奖品ID
	public String activityId	;//活动ID
	public String evaluateStaus	;//评价状态（Y\N）
	public String consumeId	;//兑换记录ID
	
	
	public String getPremiumId() {
		return premiumId;
	}
	public void setPremiumId(String premiumId) {
		this.premiumId = premiumId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getEvaluateStaus() {
		return evaluateStaus;
	}
	public void setEvaluateStaus(String evaluateStaus) {
		this.evaluateStaus = evaluateStaus;
	}
	public String getConsumeId() {
		return consumeId;
	}
	public void setConsumeId(String consumeId) {
		this.consumeId = consumeId;
	}
	public String getPrize_name() {
		return prize_name;
	}
	public void setPrize_name(String prize_name) {
		this.prize_name = prize_name;
	}
	public String getExchange_num() {
		return exchange_num;
	}
	public void setExchange_num(String exchange_num) {
		this.exchange_num = exchange_num;
	}
	public String getAll_integral() {
		return all_integral;
	}
	public void setAll_integral(String all_integral) {
		this.all_integral = all_integral;
	}
	public String getExchange_time() {
		return exchange_time;
	}
	public void setExchange_time(String exchange_time) {
		this.exchange_time = exchange_time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
