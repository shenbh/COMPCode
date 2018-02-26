package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 我的绩效data2
 * 
 * @author H81
 *
 */
public class EffictData2 implements Serializable {
	private String class_indicator_id;// 值班长指标id
	private String class_indicator_name;// 值班长指标名称
	private String class_indicator_value;// 值班长基础数据
	private String class_indicator_score;// 值班长基础得分

	public String getClass_indicator_id() {
		return class_indicator_id;
	}

	public void setClass_indicator_id(String class_indicator_id) {
		this.class_indicator_id = class_indicator_id;
	}

	public String getClass_indicator_name() {
		return class_indicator_name;
	}

	public void setClass_indicator_name(String class_indicator_name) {
		this.class_indicator_name = class_indicator_name;
	}

	public String getClass_indicator_value() {
		return class_indicator_value;
	}

	public void setClass_indicator_value(String class_indicator_value) {
		this.class_indicator_value = class_indicator_value;
	}

	public String getClass_indicator_score() {
		return class_indicator_score;
	}

	public void setClass_indicator_score(String class_indicator_score) {
		this.class_indicator_score = class_indicator_score;
	}

}
