package com.newland.comp.bean.process;

import java.io.Serializable;
import java.util.List;

import com.newland.comp.bean.my.PersonList;

/**
 * 
 * @author H81
 *getleaveData 接口封装
 */
public class SubmitLeaveBean implements Serializable{
	public List<PersonList> userList;//	用户列表（数组）
	public String  flow_code;//	流程模板
	public List<PersonList> getUserList() {
		return userList;
	}
	public void setUserList(List<PersonList> userList) {
		this.userList = userList;
	}
	public String getFlow_code() {
		return flow_code;
	}
	public void setFlow_code(String flow_code) {
		this.flow_code = flow_code;
	}
	
	
	
}
