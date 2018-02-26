package com.newland.comp.bean.my;

import java.io.Serializable;
import java.util.List;


import net.tsz.afinal.db.table.KeyValue;

/**
 * 我的资料实体bean
 * 
 * @author H81
 *
 */
public class MyInfo implements Serializable {

	private String user_no; // 员工编号
	private String name; // 姓名
	private String male; // 性别
	private String phyle; // 民族
	private String birth; // 出生日期
	private String person_type; // 人员性质
	private String department; // 科室:
	private String district; // 区域
	private String group; // 班组
	private String join_time; // 参加本单位时间:
	private String person_type2; // 员工类型
	private String belong_to; // 归属
	private String native_place; // 籍贯
	private String health; // 健康状况
	private String marriage; // 婚姻状况
	private String office_dis; // 办公地点
	private String main_skill; // 主技能线
	private String full_time_job; // 是否全职
	private String identification; // 一线标识
	private String job_name; // 职位名称
	private String job_grade; // 标准职级
	private String now_job_grade; // 现职等
	private String q_identification; // 量化标识
	private String positions_time; // 新工定岗时间
	private String phone; // 移动电话
	private String email; // 邮箱
	private String school; // 毕业学校
	private String profession; // 所学专业
	private String edu; // 学历
	private String edu_type; // 学习形式
	private String internship_start; // 实习开始时间
	private String internship_end; // 实习结束时间
	private String time_limit; //   新工期数
	private String level_time; // 离职时间
	private String political; // 政治面貌
	private String specialty; // 特长
	private String in_area_time; // 入区域时间
	private String standard_working; // 工时制
//	private String bop_number;//BOP工号
//	private String csp_number;//CSP工号
//	private String csr_number;//CSR工号
	private List<MyInfoMationKeyValue> noList;

	public String getUser_no() {
		return user_no;
	}

	public void setUser_no(String user_no) {
		this.user_no = user_no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMale() {
		return male;
	}

	public void setMale(String male) {
		this.male = male;
	}

	public String getPhyle() {
		return phyle;
	}

	public void setPhyle(String phyle) {
		this.phyle = phyle;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getPerson_type() {
		return person_type;
	}

	public void setPerson_type(String person_type) {
		this.person_type = person_type;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getJoin_time() {
		return join_time;
	}

	public void setJoin_time(String join_time) {
		this.join_time = join_time;
	}

	public String getPerson_type2() {
		return person_type2;
	}

	public void setPerson_type2(String person_type2) {
		this.person_type2 = person_type2;
	}

	public String getBelong_to() {
		return belong_to;
	}

	public void setBelong_to(String belong_to) {
		this.belong_to = belong_to;
	}

	public String getNative_place() {
		return native_place;
	}

	public void setNative_place(String native_place) {
		this.native_place = native_place;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getOffice_dis() {
		return office_dis;
	}

	public void setOffice_dis(String office_dis) {
		this.office_dis = office_dis;
	}

	public String getMain_skill() {
		return main_skill;
	}

	public void setMain_skill(String main_skill) {
		this.main_skill = main_skill;
	}

	public String getFull_time_job() {
		return full_time_job;
	}

	public void setFull_time_job(String full_time_job) {
		this.full_time_job = full_time_job;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public String getJob_grade() {
		return job_grade;
	}

	public void setJob_grade(String job_grade) {
		this.job_grade = job_grade;
	}

	public String getNow_job_grade() {
		return now_job_grade;
	}

	public void setNow_job_grade(String now_job_grade) {
		this.now_job_grade = now_job_grade;
	}

	public String getQ_identification() {
		return q_identification;
	}

	public void setQ_identification(String q_identification) {
		this.q_identification = q_identification;
	}

	public String getPositions_time() {
		return positions_time;
	}

	public void setPositions_time(String positions_time) {
		this.positions_time = positions_time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	public String getEdu_type() {
		return edu_type;
	}

	public void setEdu_type(String edu_type) {
		this.edu_type = edu_type;
	}

	public String getInternship_start() {
		return internship_start;
	}

	public void setInternship_start(String internship_start) {
		this.internship_start = internship_start;
	}

	public String getInternship_end() {
		return internship_end;
	}

	public void setInternship_end(String internship_end) {
		this.internship_end = internship_end;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getLevel_time() {
		return level_time;
	}

	public void setLevel_time(String level_time) {
		this.level_time = level_time;
	}

	public String getPolitical() {
		return political;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getIn_area_time() {
		return in_area_time;
	}

	public void setIn_area_time(String in_area_time) {
		this.in_area_time = in_area_time;
	}

	public String getStandard_working() {
		return standard_working;
	}

	public void setStandard_working(String standard_working) {
		this.standard_working = standard_working;
	}
//	public void setBop_number(String bop_number){
//		this.bop_number=bop_number;
//	}
//	public String getBop_number(){
//		return bop_number;
//	}
//    public void setCsr_number(String csr_number){
//    	this.csr_number=csr_number;
//    }
//    public String getCsr_number(){
//    	return csr_number;
//    }
//    public void setCsp_number(String csp_number){
//    	this.csp_number=csp_number;
//    }
//    public String getCsp_number(){
//    	return csp_number;
//    }

	public List<MyInfoMationKeyValue> getNoList() {
		return noList;
	}

	@Override
	public String toString() {
		return "MyInfo [user_no=" + user_no + ", name=" + name + ", male="
				+ male + ", phyle=" + phyle + ", birth=" + birth
				+ ", person_type=" + person_type + ", department=" + department
				+ ", district=" + district + ", group=" + group
				+ ", join_time=" + join_time + ", person_type2=" + person_type2
				+ ", belong_to=" + belong_to + ", native_place=" + native_place
				+ ", health=" + health + ", marriage=" + marriage
				+ ", office_dis=" + office_dis + ", main_skill=" + main_skill
				+ ", full_time_job=" + full_time_job + ", identification="
				+ identification + ", job_name=" + job_name + ", job_grade="
				+ job_grade + ", now_job_grade=" + now_job_grade
				+ ", q_identification=" + q_identification
				+ ", positions_time=" + positions_time + ", phone=" + phone
				+ ", email=" + email + ", school=" + school + ", profession="
				+ profession + ", edu=" + edu + ", edu_type=" + edu_type
				+ ", internship_start=" + internship_start
				+ ", internship_end=" + internship_end + ", time_limit="
				+ time_limit + ", level_time=" + level_time + ", political="
				+ political + ", specialty=" + specialty + ", in_area_time="
				+ in_area_time + ", standard_working=" + standard_working
				+ ", noList=" + noList + "]";
	}

	public void setNoList(List<MyInfoMationKeyValue> noList) {
		this.noList = noList;
	}
	
	
}
