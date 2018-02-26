package com.newland.comp.bean.my;


import java.io.Serializable;

/**
 * 获取班表查询接口  spanner
 * @author H81
 *
 */
public class ClassSearchData implements Serializable{

	public String id;//	单位id
	public String pid;// 	父id 像科室的pid 为null
	public String id_name;//	Id的中文名 话务一室话务二室
	public String type;// 	2-中心  3-科室 4-区域ں 
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
	public String getId_name() {
		return id_name;
	}
	public void setId_name(String id_name) {
		this.id_name = id_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
