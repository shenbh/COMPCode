package com.newland.comp.bean.my.miniknowledge;

import java.io.Serializable;

/**
 * 微知识列表(搜索列表)
 * 
 * @author Administrator
 * 
 */
public class MiniKnowledgeList implements Serializable {
	public String infoId;// 知识ID
	public String title;// 标题
	public String keyWords;// 关键字
	public String summary;// 知识简介
	public String typePath;// 知识目录路径A->B->C
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getTypePath() {
		return typePath;
	}
	public void setTypePath(String typePath) {
		this.typePath = typePath;
	}
	
	

}
