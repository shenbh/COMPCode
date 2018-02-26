package com.newland.comp.bean.more;

import java.io.Serializable;

/**员工心声问题详情附件
 * @author Administrator
 *
 */
public class ProbleDetailAttachmentData implements Serializable {
	public String attachment_img;
	public String attachment_name;

	public String getAttachment_img() {
		return attachment_img;
	}

	public void setAttachment_img(String attachment_img) {
		this.attachment_img = attachment_img;
	}

	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

}
