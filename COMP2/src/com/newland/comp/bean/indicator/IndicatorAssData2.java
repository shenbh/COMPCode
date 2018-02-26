package com.newland.comp.bean.indicator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * 指标关联  科室对比bean
 * @author H81
 *
 */
public class IndicatorAssData2 implements Serializable{

	public String id ;//    单位id
	public String pid   ;//    顶级pid 为空
	public String id_key  ;//     中心，话务一室,
	public String id_value ;//     0%， 2%
	public String type ;//    2-中心  3-科室 4-区ں
	
	public Set<IndicatorAssData2> childList = new HashSet<IndicatorAssData2>();  //用于保存科室下的区域
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getId_key() {
		return id_key;
	}
	public void setId_key(String id_key) {
		this.id_key = id_key;
	}
	public String getId_value() {
		return id_value;
	}
	public void setId_value(String id_value) {
		this.id_value = id_value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
