package com.newland.comp.bean;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author H81
 *
 */
public class UserInfo implements Serializable {

	private String userid;// 工号
	private String menuid;// 该用户所拥有的菜单权限 ，分隔
	private String banner_img; // 首页画廊图片
	private String username;// 用户中文名

	public UserInfo() {
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
}
