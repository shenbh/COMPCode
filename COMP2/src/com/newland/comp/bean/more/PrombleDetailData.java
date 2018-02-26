package com.newland.comp.bean.more;

import java.io.Serializable;

/**更多--问题详情之评价回复
 * @author H81
 *
 * 2015年5月6日 下午2:35:25
 * @version 1.0.0
 */
public class PrombleDetailData implements Serializable{
	public String assess_type;//	评价类型
	public String assess_time;//	评价时间
	public String 	reason;//	不满意理由
	public String getAssess_type() {
		return assess_type;
	}
	public void setAssess_type(String assess_type) {
		this.assess_type = assess_type;
	}
	public String getAssess_time() {
		return assess_time;
	}
	public void setAssess_time(String assess_time) {
		this.assess_time = assess_time;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}
