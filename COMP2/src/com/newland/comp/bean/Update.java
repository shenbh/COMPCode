package com.newland.comp.bean;

import java.io.Serializable;

/**
 * 检查更新参数
 * 
 * @author H81
 *
 */
public class Update implements Serializable {
	private String vsersion_id; // 服务端保存的apk版本id
	private String install_url; // 下载地址url

	public String getVsersion_id() {
		return vsersion_id;
	}

	public void setVsersion_id(String vsersion_id) {
		this.vsersion_id = vsersion_id;
	}

	public String getInstall_url() {
		return install_url;
	}

	public void setInstall_url(String install_url) {
		this.install_url = install_url;
	}

}
