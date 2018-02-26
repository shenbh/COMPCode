package com.newland.comp.bean.my.minitest;

import java.io.Serializable;

/**
 * 我的微测试-列表
 * 
 * @author Administrator
 * 
 */
public class MiniTest implements Serializable {
	public String paperId;// 试卷ID
	public String paperName;// 试卷名称
	public String answerTime;// 时长
	public String updateTime;// 创建时间
	public String expireTime;// 过期时间
	public String score;// 总分
	public String passPoint;// 合格分数
	public String joinTimes;// 考试次数 0：不限 1,2,3…相应次数
	public String checkTimes;// 已考次数
	public String checkScore;// 最后成绩
	public String passFlag;// 最后是否通过标识

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getPassPoint() {
		return passPoint;
	}

	public void setPassPoint(String passPoint) {
		this.passPoint = passPoint;
	}

	public String getJoinTimes() {
		return joinTimes;
	}

	public void setJoinTimes(String joinTimes) {
		this.joinTimes = joinTimes;
	}

	public String getCheckTimes() {
		return checkTimes;
	}

	public void setCheckTimes(String checkTimes) {
		this.checkTimes = checkTimes;
	}

	public String getCheckScore() {
		return checkScore;
	}

	public void setCheckScore(String checkScore) {
		this.checkScore = checkScore;
	}

	public String getPassFlag() {
		return passFlag;
	}

	public void setPassFlag(String passFlag) {
		this.passFlag = passFlag;
	}

}
