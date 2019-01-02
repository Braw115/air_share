package com.air.pojo.entity;

import java.util.Date;

public class Notice {
	private Integer noticeId;
	private Integer appusersId;
	private String telephone;
	private String title;	// 标题
	private String content;	// 内容
	private Date created;	// 创建时间
	private Boolean send;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public Boolean getSend() {
		return send;
	}

	public void setSend(Boolean send) {
		this.send = send;
	}

	
}
