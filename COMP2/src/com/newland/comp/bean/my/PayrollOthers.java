package com.newland.comp.bean.my;

/**
 * 我的薪酬
 * 
 * @author H81
 *
 */
public class PayrollOthers {

	private String byls;// 表扬例数 放在data_exp jsonobj里

	private String zhkp;//  综合考评 放在data_exp jsonobj里

	public String getByls() {
		return byls;
	}

	public void setByls(String byls) {
		this.byls = byls;
	}

	public String getZhkp() {
		return zhkp;
	}

	public void setZhkp(String zhkp) {
		this.zhkp = zhkp;
	}

}
