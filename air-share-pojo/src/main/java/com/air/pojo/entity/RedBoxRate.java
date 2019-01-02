package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RedBoxRate {

	private Integer redBoxRateId;
	private Integer redBoxActiveId;
	private Integer redBoxRate;
	private BigDecimal redBoxMax;
	private BigDecimal redBoxMin;
	private Date created;
	private Date modified;
	
	public Integer getRedBoxRateId() {
		return redBoxRateId;
	}
	public void setRedBoxRateId(Integer redBoxRateId) {
		this.redBoxRateId = redBoxRateId;
	}
	public Integer getRedBoxActiveId() {
		return redBoxActiveId;
	}
	public void setRedBoxActiveId(Integer redBoxActiveId) {
		this.redBoxActiveId = redBoxActiveId;
	}
	public Integer getRedBoxRate() {
		return redBoxRate;
	}
	public void setRedBoxRate(Integer redBoxRate) {
		this.redBoxRate = redBoxRate;
	}
	public BigDecimal getRedBoxMax() {
		return redBoxMax;
	}
	public void setRedBoxMax(BigDecimal redBoxMax) {
		this.redBoxMax = redBoxMax;
	}
	public BigDecimal getRedBoxMin() {
		return redBoxMin;
	}
	public void setRedBoxMin(BigDecimal redBoxMin) {
		this.redBoxMin = redBoxMin;
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
