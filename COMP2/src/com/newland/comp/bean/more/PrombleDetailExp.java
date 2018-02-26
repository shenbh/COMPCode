package com.newland.comp.bean.more;

import java.io.Serializable;
import java.util.List;

/**更多--问题详情
 * @author H81
 *
 * 2015年5月6日 下午2:38:27
 * @version 1.0.0
 */
public class PrombleDetailExp implements Serializable{
	public String pro_id;//	问题id
	public String title;//	问题标题
	public String name;//	员工姓名
	public String userid;//	员工编号
	public String phone	;//联系方式
	public String dep	;//所在科室
	public String pro_type;//	问题类型
	public String pro_content;//	问题内容
	public String pro_reply;//问题回复
	public String keywords;//关键字
	public List<PrombleDetailData>assessData;  //评价集合
//	public List<PrombleDetailData2>handleData;  //流程集合
	public List<ProbleDetailAttachmentData>attachmentData;//附件集合
	
	public List<PrombleDetailData> getAssessData() {
		return assessData;
	}
	public void setAssessData(List<PrombleDetailData> assessData) {
		this.assessData = assessData;
	}
//	public List<PrombleDetailData2> getHandleData() {
//		return handleData;
//	}
//	public void setHandleData(List<PrombleDetailData2> handleData) {
//		this.handleData = handleData;
//	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getPro_id() {
		return pro_id;
	}
	public void setPro_id(String pro_id) {
		this.pro_id = pro_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getPro_type() {
		return pro_type;
	}
	public void setPro_type(String pro_type) {
		this.pro_type = pro_type;
	}
	public String getPro_content() {
		return pro_content;
	}
	public void setPro_content(String pro_content) {
		this.pro_content = pro_content;
	}
	public String getPro_reply() {
		return pro_reply;
	}
	public void setPro_reply(String pro_reply) {
		this.pro_reply = pro_reply;
	}
	public List<ProbleDetailAttachmentData> getAttachmentData() {
		return attachmentData;
	}
	public void setAttachmentData(List<ProbleDetailAttachmentData> attachmentData) {
		this.attachmentData = attachmentData;
	}

	
}
