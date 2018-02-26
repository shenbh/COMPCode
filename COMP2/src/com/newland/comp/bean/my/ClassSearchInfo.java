package com.newland.comp.bean.my;


import java.io.Serializable;
/**
 * 班表查询 数据
 * @author H81
 *
 */
public class ClassSearchInfo implements Serializable{

	public String userid;//	员工编号
	public String username;//	员工名字
	public String sb_date;//	上班日期
	public String sb_time;//	上班时间
	public String xb_time	;//下班时间
	public String ks;//	科室
	public String zq;//	专区
	public String bz;//	所属班组
	public String seat;//	座位号
	
	
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
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
	public String getSb_date() {
		return sb_date;
	}
	public void setSb_date(String sb_date) {
		this.sb_date = sb_date;
	}
	public String getSb_time() {
		return sb_time;
	}
	public void setSb_time(String sb_time) {
		this.sb_time = sb_time;
	}
	public String getXb_time() {
		return xb_time;
	}
	public void setXb_time(String xb_time) {
		this.xb_time = xb_time;
	}
	public String getKs() {
		return ks;
	}
	public void setKs(String ks) {
		this.ks = ks;
	}
	public String getZq() {
		return zq;
	}
	public void setZq(String zq) {
		this.zq = zq;
	}
	
	
	
}
