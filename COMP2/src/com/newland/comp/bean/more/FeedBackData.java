package com.newland.comp.bean.more;

import java.io.Serializable;

/**
 * 员工心声问题反馈
 * 
 * @author H81
 *
 */
public class FeedBackData implements Serializable {

	public String bizId;// 指标名

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String s) {
		this.bizId = s;
	}

}
