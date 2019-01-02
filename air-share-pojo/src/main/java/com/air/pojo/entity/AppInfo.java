package com.air.pojo.entity;

import java.util.Date;


public class AppInfo {
	
	private Integer appInfoId;//app信息id
	private String type;//App信息类型
	private String content;//App信息内容
	private Date created;
	private Date modified;
	public Integer getAppInfoId() {
		return appInfoId;
	}
	public void setAppInfoId(Integer appInfoId) {
		this.appInfoId = appInfoId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}

	

}
