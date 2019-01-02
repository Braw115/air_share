package com.air.pojo.entity;

import java.util.Date;

public class Logger {
	private Integer loggerId;
	private String ip;
	private Integer userId;
	private String username;
	private String content;
	private Date created;
	public Integer getLoggerId() {
		return loggerId;
	}
	public void setLoggerId(Integer loggerId) {
		this.loggerId = loggerId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
}
