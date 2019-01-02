package com.air.pojo.entity;

import java.util.Date;

public class TimingTasks {
	private Integer taskId;		
	private String order;		// 指令
	private Integer userId;		// 用户ID
	private Boolean execute;	// 任务是否执行
	private Date exeTime;		// 执行时间
	private String mac;
	private Date created;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getExecute() {
		return execute;
	}

	public void setExecute(Boolean execute) {
		this.execute = execute;
	}

	public Date getExeTime() {
		return exeTime;
	}

	public void setExeTime(Date exeTime) {
		this.exeTime = exeTime;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
