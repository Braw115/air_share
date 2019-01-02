package com.air.pojo.entity;

import java.util.Date;

/**
 * 
 * APP用户空调表
 * @author czg
 *
 */
public class AppUserAir {
	private Integer appuserAirId;
	private Integer appusersId;		//APP用户id
	private AirCondition conditions;
	private String mac;				//空调MAC
	private String note;			//备注
	private Date created;
	private Date modified;
	private Long time;
	private String model;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getAppuserAirId() {
		return appuserAirId;
	}

	public void setAppuserAirId(Integer appuserAirId) {
		this.appuserAirId = appuserAirId;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public AirCondition getConditions() {
		return conditions;
	}

	public void setConditions(AirCondition conditions) {
		this.conditions = conditions;
	}

	
	
	
}
