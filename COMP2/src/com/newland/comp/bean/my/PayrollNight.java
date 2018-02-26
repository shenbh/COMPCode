package com.newland.comp.bean.my;

/**
 * 夜班时长
 * 
 * @author H81
 *
 */
public class PayrollNight {
	private String night_id;// 夜班时长id
	private String night_name;// 夜班时长名称
	private String night_value;// 夜班时长值

	public String getNight_id() {
		return night_id;
	}

	public void setNight_id(String night_id) {
		this.night_id = night_id;
	}

	public String getNight_name() {
		return night_name;
	}

	public void setNight_name(String night_name) {
		this.night_name = night_name;
	}

	public String getNight_value() {
		return night_value;
	}

	public void setNight_value(String night_value) {
		this.night_value = night_value;
	}

}
