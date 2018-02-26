package com.newland.comp.bean.my;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;
/**
 * 系统消息
 * @author H81
 *
 */
@Table(name="sysmsg")
public class SysMsg implements Serializable{

	@Id
	public String msgid;//系统消息id
	public String msg_time;//	系统消息时间
	public String senderName;//	发送人
	public String content;//	消息内容
	public String statue;//	已读状态/ 0未读， 1已读
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getMsg_time() {
		return msg_time;
	}
	public void setMsg_time(String msg_time) {
		this.msg_time = msg_time;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatue() {
		return statue;
	}
	public void setStatue(String statue) {
		this.statue = statue;
	}
	
	
	
}
