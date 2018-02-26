package com.newland.comp.bean.process;


import java.io.Serializable;

/**
 * 已办/我发起的流程 列表数据
 * @author H81
 *
 */
public class ProcessStateData implements Serializable{

	public String work_id	;//工单编号
	public String work_title;//	工单标题
	public String pro_person	;//流程创建人
	public String statue;//	状态
	public String create_time	;//流程创建时间
	public String arrive_pro	;//当前处理流程
	
	public String pro_type	;//流程类型（目前手机端只支持3种流程、请假、离职申请、考勤报备） 
								/*
								 * 	请假：vacate
									离职：leave
									考勤报备：check_on
									其他手机端不支持的流程统一是：others
								 */
	
	public String getWork_id() {
		return work_id;
	}
	public void setWork_id(String work_id) {
		this.work_id = work_id;
	}
	public String getWork_title() {
		return work_title;
	}
	public void setWork_title(String work_title) {
		this.work_title = work_title;
	}
	public String getPro_person() {
		return pro_person;
	}
	public void setPro_person(String pro_person) {
		this.pro_person = pro_person;
	}
	public String getStatue() {
		return statue;
	}
	public void setStatue(String statue) {
		this.statue = statue;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getArrive_pro() {
		return arrive_pro;
	}
	public void setArrive_pro(String arrive_pro) {
		this.arrive_pro = arrive_pro;
	}
	public String getPro_type() {
		return pro_type;
	}
	public void setPro_type(String pro_type) {
		this.pro_type = pro_type;
	}
	
	
	
}
