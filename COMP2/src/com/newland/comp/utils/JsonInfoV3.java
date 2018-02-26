package com.newland.comp.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * json新接口解析方法
 * 
 * @author H81
 *
 */
public class JsonInfoV3 {
	public String resultCode;// 返回状态码
	public String resultDesc;// 返回状态码描述
	public static String SUCCESS_CODE = "000000"; // 接口返回成功状态码
	public Object resultData; // 数据对象字段

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

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	/**
	 * 转换ResultData成对应的对象
	 * 
	 * @param cla
	 * @return
	 */
	public <T> T getResultDataToClass(Class<T> cla) {
		if (StringUtil.isNotEmpty(resultData.toString())) {
			String str2 = JSON.toJSONString(resultData);
			return JSON.parseObject(str2, cla);
		} else {
			return null;
		}

	}


	/**
	 * 把ResultData对象转换成 JsonObject
	 * 
	 * @return
	 */
	public JSONObject getResultDataToJsonObject() {
		String str2 = JSON.toJSONString(resultData);

		return JSON.parseObject(str2);

	}

	/**
	 * 把ResultData对象中某个key 获取array数组
	 * 
	 * @return
	 */
	public <T> List<T> getResultDataKeyToClass(String key, Class<T> cla) {
		String str2 = JSON.toJSONString(resultData);
		JSONObject jsonObject = JSON.parseObject(str2);
		JSONArray jsonArray = jsonObject.getJSONArray(key);
		String  str3 = JSON.toJSONString(jsonArray);
		return JSON.parseArray(str3, cla);
	}

	
	public <T> List<T> getResultDataKeyToClass(String str, String key, Class<T> cla) {

		JSONObject jsonObject = JSON.parseObject(str);
		JSONArray jsonArray = jsonObject.getJSONArray(key);
		String str3 = JSON.toJSONString(jsonArray);
		return JSON.parseArray(str3, cla);
	}
	
	
	public <T> List<T> getResultDataKeyToClass(String str,String key,Class<T> cla,String s )
	{
		String str2 = JSON.toJSONString(resultData);
		JSONObject jsonObject = JSON.parseObject(str2);
		JSONArray jsonArray = jsonObject.getJSONArray(key);
		String  str3 = JSON.toJSONString(jsonArray);
		return JSON.parseArray(str3, cla);
	}
	
	public <T> List<T> getDataExpToClass(String key,String key1,Class<T> cla){
		String str2=JSON.toJSONString(resultData);
		JSONObject jsonObject=JSON.parseObject(str2);
		JSONObject jsonObject1=jsonObject.getJSONObject(key);
		JSONArray jsonArray=jsonObject1.getJSONArray(key1);
		String str4=JSON.toJSONString(jsonArray);
		return JSON.parseArray(str4,cla);
	}
}
