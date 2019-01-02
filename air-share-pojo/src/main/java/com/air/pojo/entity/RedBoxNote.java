package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat.Value;

public class RedBoxNote {
	
	private Integer redBoxNoteId;
	private Integer redBoxActiveId;
	private Integer appusersId;
	private BigDecimal value;
	private Date created;
	public Integer getRedBoxNoteId() {
		return redBoxNoteId;
	}
	public void setRedBoxNoteId(Integer redBoxNoteId) {
		this.redBoxNoteId = redBoxNoteId;
	}
	public Integer getRedBoxActiveId() {
		return redBoxActiveId;
	}
	public void setRedBoxActiveId(Integer redBoxActiveId) {
		this.redBoxActiveId = redBoxActiveId;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
