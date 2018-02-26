package com.newland.comp.bean.my;

/**我的积分
 * @author H81
 *
 * 2015年4月24日 下午3:02:29
 * @version 1.0.0
 */
public class IntegralExp {

	public String com_integral_sum; // 综合评价积分总计
	public String work_age_sum; // 工龄加分总计
	public String exchange_integral; // 可兑换的结余积分
	public String quarte_indicator_score; // 本季度获取的综合评价积分
	public String histogram_x; // 柱状图x轴数据 2014第一季度，2014第二季度
	public String histogram_y; // 柱状图y轴数据 200,400.。。
	public String line_x; // 折线图x轴数据 ,分隔
	public String line_y; // 折线图y轴数据 ,分隔
	public String activity;// 当前活动
	private String  is_save;//	已归档/未归档
	
	
	public String getIs_save() {
		return is_save;
	}
	public void setIs_save(String is_save) {
		this.is_save = is_save;
	}
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getCom_integral_sum() {
		return com_integral_sum;
	}

	public void setCom_integral_sum(String com_integral_sum) {
		this.com_integral_sum = com_integral_sum;
	}

	public String getWork_age_sum() {
		return work_age_sum;
	}

	public void setWork_age_sum(String work_age_sum) {
		this.work_age_sum = work_age_sum;
	}

	public String getExchange_integral() {
		return exchange_integral;
	}

	public void setExchange_integral(String exchange_integral) {
		this.exchange_integral = exchange_integral;
	}

	public String getQuarte_indicator_score() {
		return quarte_indicator_score;
	}

	public void setQuarte_indicator_score(String quarte_indicator_score) {
		this.quarte_indicator_score = quarte_indicator_score;
	}

	public String getHistogram_x() {
		return histogram_x;
	}

	public void setHistogram_x(String histogram_x) {
		this.histogram_x = histogram_x;
	}

	public String getHistogram_y() {
		return histogram_y;
	}

	public void setHistogram_y(String histogram_y) {
		this.histogram_y = histogram_y;
	}

	public String getLine_x() {
		return line_x;
	}

	public void setLine_x(String line_x) {
		this.line_x = line_x;
	}

	public String getLine_y() {
		return line_y;
	}

	public void setLine_y(String line_y) {
		this.line_y = line_y;
	}

}
