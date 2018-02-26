package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 我的绩效data_exp
 * 
 * @author H81
 *
 */
public class EffictDataExp implements Serializable {
	private String kpi_score;// 个人绩效总得分
	private String kpi_com;// 个人绩效综合考评
	private String kpi_level;// 个人绩效等级
	private String kpi_comment;// 个人绩效评价
	private String kpi_x;// 近6个月个人绩效分析图月份(x轴)2014-8,2014-09,2014-10......
	private String kpi_y;// 近6个月个人绩效分析图绩效分(y轴)50,70,90....

	public String getKpi_score() {
		return kpi_score;
	}

	public void setKpi_score(String kpi_score) {
		this.kpi_score = kpi_score;
	}

	public String getKpi_com() {
		return kpi_com;
	}

	public void setKpi_com(String kpi_com) {
		this.kpi_com = kpi_com;
	}

	public String getKpi_level() {
		return kpi_level;
	}

	public void setKpi_level(String kpi_level) {
		this.kpi_level = kpi_level;
	}

	public String getKpi_comment() {
		return kpi_comment;
	}

	public void setKpi_comment(String kpi_comment) {
		this.kpi_comment = kpi_comment;
	}

	public String getKpi_x() {
		return kpi_x;
	}

	public void setKpi_x(String kpi_x) {
		this.kpi_x = kpi_x;
	}

	public String getKpi_y() {
		return kpi_y;
	}

	public void setKpi_y(String kpi_y) {
		this.kpi_y = kpi_y;
	}

	
//	新增月份字段
	public String date;
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date=date;
	}
}
