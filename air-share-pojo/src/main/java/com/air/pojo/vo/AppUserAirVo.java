package com.air.pojo.vo;

import java.util.Date;

import javax.management.loading.PrivateClassLoader;

import org.aspectj.weaver.AjAttribute.PrivilegedAttribute;

import com.air.pojo.entity.AirCondition;

public class AppUserAirVo {
	
	private Integer appuserAirId;
	private Integer appusersId;		//APP用户id
	private AirCondition conditions;
	private String mac;				//空调MAC
	private String note;			//备注
	private String telephone;			//绑定用户的手机号
	private Date created;
	private Date modified;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
