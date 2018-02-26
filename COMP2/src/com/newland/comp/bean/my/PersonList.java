package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 人员列表
 * @author H81
 *
 */
public class PersonList implements Serializable{

	public String misid;//	Misid(用于展示)
	public String userid;//	用户id
	public String username;//	用户名
	public String getMisid() {
		return misid;
	}
	public void setMisid(String misid) {
		this.misid = misid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
