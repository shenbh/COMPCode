package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 我的星级 data1
 * @author H81
 *
 */
public class MyStartData implements Serializable{

	public String skill	;//轮岗技能线
	public String standard;//	标准量（》=650）
	public String xs;//	服务系数（》=1。4）
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getXs() {
		return xs;
	}
	public void setXs(String xs) {
		this.xs = xs;
	}
	
	
	
	
}
