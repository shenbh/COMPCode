package com.newland.comp.bean.indicator;

import java.io.Serializable;
/**
 *  指标详情 新接口  指标关联
 * @author H81
 *
 */
public class IndicatorAssResultDataRelation implements Serializable{
	public String zb_id	;//指标id
	public String zb_name;//	指标名
	public String value	;//当前值
	public String rang;//	合理区间

	public String rang_explain;//	合理区间比较

	public String getZb_id() {
		return zb_id;
	}

	public void setZb_id(String zb_id) {
		this.zb_id = zb_id;
	}

	public String getZb_name() {
		return zb_name;
	}

	public void setZb_name(String zb_name) {
		this.zb_name = zb_name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRang() {
		return rang;
	}

	public void setRang(String rang) {
		this.rang = rang;
	}

	public String getRang_explain() {
		return rang_explain;
	}

	public void setRang_explain(String rang_explain) {
		this.rang_explain = rang_explain;
	}

	
	
}
