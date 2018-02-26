package com.newland.comp.bean.my;

import java.io.Serializable;

/**
 * 服务质量 系数
 * 
 * @author H81
 *
 */
public class PayrollQosXs implements Serializable {
	private String service_final_num;// 实际服务质量系数
	private String service_adjust_num;// 服务质量调节系数

	public String getService_final_num() {
		return service_final_num;
	}

	public void setService_final_num(String service_final_num) {
		this.service_final_num = service_final_num;
	}

	public String getService_adjust_num() {
		return service_adjust_num;
	}

	public void setService_adjust_num(String service_adjust_num) {
		this.service_adjust_num = service_adjust_num;
	}

}
