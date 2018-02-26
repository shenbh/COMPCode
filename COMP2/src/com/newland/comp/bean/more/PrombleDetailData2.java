package com.newland.comp.bean.more;

import java.io.Serializable;

/**更多--问题详情之处理流程
 * @author H81
 *
 * 2015年5月6日 下午2:37:23
 * @version 1.0.0
 */
public class PrombleDetailData2 implements Serializable {
	public String handle_time;// 处理完成时间
	public String handle_dep;// 处理科室
	public String handle_person;// 处理人
	public String reply_content;// 回复内容
	public String getHandle_time() {
		return handle_time;
	}
	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}
	public String getHandle_dep() {
		return handle_dep;
	}
	public void setHandle_dep(String handle_dep) {
		this.handle_dep = handle_dep;
	}
	public String getHandle_person() {
		return handle_person;
	}
	public void setHandle_person(String handle_person) {
		this.handle_person = handle_person;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

}
