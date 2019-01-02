package com.air.pojo.entity;

import java.util.Date;

public class OperationRecord {
	private Integer operationId;
	private String mac;
	private String order; // 指令
	private Date time;
	private Integer appusersId;
	private Boolean execute;
	private Date created;

	public Boolean getExecute() {
		return execute;
	}

	public void setExecute(Boolean execute) {
		this.execute = execute;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
