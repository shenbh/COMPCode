package com.newland.comp.bean.process;

import java.io.Serializable;

/**
 * 办理流程item
 * @author H81
 *
 */
public class ProcessFlow implements Serializable{

	public String step	;//处理步骤
	public String statue	;//状态
	public String arrive_time;//	任务到达时间
	public String handle_time	;//处理时间
	public String receive_person;//	接收人
	public String handle_person;//	处理人
	public String handle_suggest;//	处理意见
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getStatue() {
		return statue;
	}
	public void setStatue(String statue) {
		this.statue = statue;
	}
	public String getArrive_time() {
		return arrive_time;
	}
	public void setArrive_time(String arrive_time) {
		this.arrive_time = arrive_time;
	}
	public String getHandle_time() {
		return handle_time;
	}
	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}
	public String getReceive_person() {
		return receive_person;
	}
	public void setReceive_person(String receive_person) {
		this.receive_person = receive_person;
	}
	public String getHandle_person() {
		return handle_person;
	}
	public void setHandle_person(String handle_person) {
		this.handle_person = handle_person;
	}
	public String getHandle_suggest() {
		return handle_suggest;
	}
	public void setHandle_suggest(String handle_suggest) {
		this.handle_suggest = handle_suggest;
	}
	
	
	
}
