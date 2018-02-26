package com.newland.comp.bean.my;

import java.io.Serializable;
/**
 * 我的星级
 * @author H81
 *
 */
public class MyStartExp implements Serializable{

	public String px_sum;//	评选总计
	public String zh_score;//		综合积分得分
	public String total;//		总分排名
	public String user_name;//		员工名字
	public String user_num;//		编号
	public String csp;//		Csp工号
	public String dq;//		地区
		
		
	public String main_skill;//		主线技能
	public String service_sum;//		服务营销得分
	public String jx_level;//		绩效等级-半年绩效等级
	public String jx_level_val;//		绩效等级合计值：

	public String jx_level_x;//		绩效等级x轴值 2013-7,2013-8  ,分隔
	public String jx_level_y;//		绩效等级y轴值 10,10  ,分隔
	public String jx_level_y_title;//		绩效等级y轴 标题 职称，职称，

	public String px_val;//		培训得分
	public String px_val_p_x;//		培训得分个人值x轴值 非网络培训市场，a季度网络学习时长 ,分隔
	public String px_val_p_y;//		培训得分个人值y轴值 ,分隔
	public String px_val_j_x;//		培训得分基准值值x轴值 非网络培训市场，a季度网络学习时长 ,分隔
	public String px_val_j_y;//		培训得分基准值y轴值  ,分隔
	public String zscc_score;//		知识传承得分
	public String is_bear;//		是否获奖人员
	public String sugget;//		合理化建议个数
	public String action;//		行动方案个数
	public String course;//		课程开发个数
	public String my_case;//		案例数量个数
	public String training	;//	培训时长
	public String remark;// 备注信息
	public String getPx_sum() {
		return px_sum;
	}
	public void setPx_sum(String px_sum) {
		this.px_sum = px_sum;
	}
	public String getZh_score() {
		return zh_score;
	}
	public void setZh_score(String zh_score) {
		this.zh_score = zh_score;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_num() {
		return user_num;
	}
	public void setUser_num(String user_num) {
		this.user_num = user_num;
	}
	public String getCsp() {
		return csp;
	}
	public void setCsp(String csp) {
		this.csp = csp;
	}
	public String getDq() {
		return dq;
	}
	public void setDq(String dq) {
		this.dq = dq;
	}
	public String getMain_skill() {
		return main_skill;
	}
	public void setMain_skill(String main_skill) {
		this.main_skill = main_skill;
	}
	public String getService_sum() {
		return service_sum;
	}
	public void setService_sum(String service_sum) {
		this.service_sum = service_sum;
	}
	public String getJx_level() {
		return jx_level;
	}
	public void setJx_level(String jx_level) {
		this.jx_level = jx_level;
	}
	public String getJx_level_val() {
		return jx_level_val;
	}
	public void setJx_level_val(String jx_level_val) {
		this.jx_level_val = jx_level_val;
	}
	public String getJx_level_x() {
		return jx_level_x;
	}
	public void setJx_level_x(String jx_level_x) {
		this.jx_level_x = jx_level_x;
	}
	public String getJx_level_y() {
		return jx_level_y;
	}
	public void setJx_level_y(String jx_level_y) {
		this.jx_level_y = jx_level_y;
	}
	public String getJx_level_y_title() {
		return jx_level_y_title;
	}
	public void setJx_level_y_title(String jx_level_y_title) {
		this.jx_level_y_title = jx_level_y_title;
	}
	public String getPx_val() {
		return px_val;
	}
	public void setPx_val(String px_val) {
		this.px_val = px_val;
	}
	public String getPx_val_p_x() {
		return px_val_p_x;
	}
	public void setPx_val_p_x(String px_val_p_x) {
		this.px_val_p_x = px_val_p_x;
	}
	public String getPx_val_p_y() {
		return px_val_p_y;
	}
	public void setPx_val_p_y(String px_val_p_y) {
		this.px_val_p_y = px_val_p_y;
	}
	public String getPx_val_j_x() {
		return px_val_j_x;
	}
	public void setPx_val_j_x(String px_val_j_x) {
		this.px_val_j_x = px_val_j_x;
	}
	public String getPx_val_j_y() {
		return px_val_j_y;
	}
	public void setPx_val_j_y(String px_val_j_y) {
		this.px_val_j_y = px_val_j_y;
	}
	public String getZscc_score() {
		return zscc_score;
	}
	public void setZscc_score(String zscc_score) {
		this.zscc_score = zscc_score;
	}
	public String getIs_bear() {
		return is_bear;
	}
	public void setIs_bear(String is_bear) {
		this.is_bear = is_bear;
	}
	public String getSugget() {
		return sugget;
	}
	public void setSugget(String sugget) {
		this.sugget = sugget;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getMy_case() {
		return my_case;
	}
	public void setMy_case(String my_case) {
		this.my_case = my_case;
	}
	public String getTraining() {
		return training;
	}
	public void setTraining(String training) {
		this.training = training;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
}
