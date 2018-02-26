package com.newland.comp.bean.more;


/**更多之问题反馈实体类
 *
 * 2015年5月5日 下午4:27:57
 * @version 1.0.0
 */
public class TreeBean {
	public String id;//	单位id
	public String pid ;//		父id  如果没有 为null
	public String id_name;//		Id的中文名 咨询、建议
	public String type ;//		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getId_name() {
		return id_name;
	}
	public void setId_name(String id_name) {
		this.id_name = id_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
