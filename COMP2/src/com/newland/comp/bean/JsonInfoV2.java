package com.newland.comp.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.newland.comp.utils.JsonInfo;
/**
 * 兼容新的接口文档解析
 * @author H81
 *
 */
public class JsonInfoV2 implements Serializable{

	public String resultCode;//返回状态码
	public String resultDesc;//返回状态码描述
	public Object resultData;//兼容旧接口属性
	public static String SUCCESS_CODE = "000000"; //接口返回成功状态码
	
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
	 * @param cla
	 * @return
	 */
	public <T> T getResultDataToClass(Class<T> cla)
	{
		String str2 = JSON.toJSONString(resultData);
		
		return JSON.parseObject(str2, cla);
		
	}
	public <T> T getResultDataToClass(Class<T> cla,String str)
	{
		//String str2 = JSON.toJSONString(resultData);
		
		return JSON.parseObject(str, cla);
		
	}
	
	
}
