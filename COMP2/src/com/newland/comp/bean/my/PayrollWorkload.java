package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 我的薪酬 --工作量
 * 
 * @author H81
 *
 */
public class PayrollWorkload implements Serializable {

	private String skill_id;// 技能名称id
	private String skill_pid;// 技能名称 上一级的技能名称id 如果第一级pid就是0

	private String skill_level;// 技能名称所在level层级，1,2,3

	private String skill_value;// 技能名称的值
	private String workload;// 工作折合量
	private String origin_work;// 原始工作量
	private String person_work;// 个人实际量
	private String max_value;// 最大允许量

	public String getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(String skill_id) {
		this.skill_id = skill_id;
	}

	public String getSkill_pid() {
		return skill_pid;
	}

	public void setSkill_pid(String skill_pid) {
		this.skill_pid = skill_pid;
	}

	public String getSkill_level() {
		return skill_level;
	}

	public void setSkill_level(String skill_level) {
		this.skill_level = skill_level;
	}

	public String getSkill_value() {
		return skill_value;
	}

	public void setSkill_value(String skill_value) {
		this.skill_value = skill_value;
	}

	public String getWorkload() {
		return workload;
	}

	public void setWorkload(String workload) {
		this.workload = workload;
	}

	public String getOrigin_work() {
		return origin_work;
	}

	public void setOrigin_work(String origin_work) {
		this.origin_work = origin_work;
	}

	public String getPerson_work() {
		return person_work;
	}

	public void setPerson_work(String person_work) {
		this.person_work = person_work;
	}

	public String getMax_value() {
		return max_value;
	}

	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}

}
