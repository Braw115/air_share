package com.air.pojo.entity;

import java.util.Date;

public class UseRecords {
	private Integer recordsId; // 记录ID
	private Integer appusersId; // APP用户ID
	private String mac; // 空调MAC
	private Integer useTime; // 使用时间
	private Integer electricity; // 用电量
	private Date created;
	private Date modified;

	public Integer getRecordsId() {
		return recordsId;
	}

	public void setRecordsId(Integer recordsId) {
		this.recordsId = recordsId;
	}

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getUseTime() {
		return useTime;
	}

	public void setUseTime(Integer useTime) {
		this.useTime = useTime;
	}

	public Integer getElectricity() {
		return electricity;
	}

	public void setElectricity(Integer electricity) {
		this.electricity = electricity;
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
