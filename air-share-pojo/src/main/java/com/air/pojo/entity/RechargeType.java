package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RechargeType {
	
	private Integer rechargeId;
	private Integer seriesId;
	private String typeName;
	private BigDecimal price;
	private Date created;
	private Date modified;
	public Integer getRechargeId() {
		return rechargeId;
	}
	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}
	public Integer getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
