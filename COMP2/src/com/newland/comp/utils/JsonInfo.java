package com.newland.comp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.provider.Settings.System;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.comp.bean.UserInfo;
/**
 * 封装服务器返回的实体类
 * @author H81
 *
 */
public class JsonInfo {
	private String resultCode; //响应代码
	private String resultDesc; //响应描述resultDesc
	private Object data; //返回回来的数据
	private Object data2; //返回回来的数据
	private Object data_exp; //返回回来的数据  额外
	public static String SUCCESS_CODE = "000000"; //接口返回成功状态码
	
	public JsonInfo()
	{
		
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



	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
	
	public Object getData_exp() {
		return data_exp;
	}

	public void setData_exp(Object data_exp) {
		this.data_exp = data_exp;
	}

	public JsonInfo(String result_code, String result_desc, Object data) {
		super();
		this.resultCode = result_code;
		this.resultDesc = result_desc;
		this.data = data;
	}

	public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}

	@Override
	public String toString() {
		return "JsonInfo [resultCode=" + resultCode + ", resultCode="
				+ resultDesc + ", data=" + data + "]";
	}
	
	
	public <T> List<T> getDataList(Class<T> cla)
	{
		String str2 = JSON.toJSONString(getData());
		List<T> list = JSON.parseArray(str2, cla);
		if (list == null) {
			list = new ArrayList<T>();
		}
		return list;
		
	}
	
	
	public <T> List<T> getDataList2(Class<T> cla)
	{
		String str2 = JSON.toJSONString(getData2());
		List<T> list = JSON.parseArray(str2, cla);
		if (list == null) {
			list = new ArrayList<T>();
		}
		return list;
		
	}

	
	public <T> T getData_exp(Class<T> cla)
	{
		String str2 = JSON.toJSONString(getData_exp());
		
		return JSON.parseObject(str2, cla);
		
	}
	
	/**
	 * 把exp对象转换成  JsonObject
	 * @return
	 */
	public JSONObject  getData_expToJsonObject()
	{
		String str2 = JSON.toJSONString(getData_exp());
		
		return JSON.parseObject(str2);
		
	}
}
