package com.air.pojo.vo;

import java.util.Date;

public class OperationRecordVO {
	private Integer operationId;
	private String mac;
	private String order; // 指令
	private Date time;
	private Integer appusersId;
	private Date created;
	private String beginDate;
	private String endDate;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
