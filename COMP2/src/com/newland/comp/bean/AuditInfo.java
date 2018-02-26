package com.newland.comp.bean;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AuditInfo {

	private Object flowTaskList; // 返回回来的数据
	private String resultCode; // 响应代码
	private String resultDesc; // 响应描述resultDesc

	public AuditInfo() {

	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public Object getFlowTaskList() {
		return flowTaskList;
	}

	public void setFlowTaskList(Object flowTaskList) {
		this.flowTaskList = flowTaskList;
	}

	public AuditInfo(String result_code, String result_desc, Object flowTaskList) {
		super();
		this.resultCode = result_code;
		this.resultDesc = result_desc;
		this.flowTaskList = flowTaskList;
	}

	@Override
	public String toString() {
		return "JsonInfo [resultCode=" + resultCode + ", resultCode=" + resultDesc + ", flowTaskList=" + flowTaskList + "]";
	}

	public <T> T getFlowTaskListToClass(Class<T> cla) {
		String str2 = JSON.toJSONString(flowTaskList);

		return JSON.parseObject(str2, cla);

	}

	public <T> List<T> getDataList(Class<T> cla) {
		String str2 = JSON.toJSONString(getFlowTaskList());
		List<T> list = JSON.parseArray(str2, cla);
		if (list == null) {
			list = new ArrayList<T>();
		}
		return list;

	}

}
