package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 我的绩效data1
 * 
 * @author H81
 *
 */
public class EffictData implements Serializable {
	private String person_indicator_id;// 话务员指标id
	private String person_indicator_name;// 话务员指标名称
	private String person_indicator_value;// 话务员基础数据
	private String person_indicator_score;// 话务员得分

	public String getPerson_indicator_id() {
		return person_indicator_id;
	}

	public void setPerson_indicator_id(String person_indicator_id) {
		this.person_indicator_id = person_indicator_id;
	}

	public String getPerson_indicator_name() {
		return person_indicator_name;
	}

	public void setPerson_indicator_name(String person_indicator_name) {
		this.person_indicator_name = person_indicator_name;
	}

	public String getPerson_indicator_value() {
		return person_indicator_value;
	}

	public void setPerson_indicator_value(String person_indicator_value) {
		this.person_indicator_value = person_indicator_value;
	}

	public String getPerson_indicator_score() {
		return person_indicator_score;
	}

	public void setPerson_indicator_score(String person_indicator_score) {
		this.person_indicator_score = person_indicator_score;
	}

}
