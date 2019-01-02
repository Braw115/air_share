package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AirUseInfo {
	private Integer airUseInfoId;
	private BigDecimal eleCurrent;//电流
	private BigDecimal voltage;//电压
	private BigDecimal power;//功率
	private String airMac;//空调mac地址
	private Date created;

	public Integer getAirUseInfoId() {
		return airUseInfoId;
	}

	public void setAirUseInfoId(Integer airUseInfoId) {
		this.airUseInfoId = airUseInfoId;
	}

	public BigDecimal getEleCurrent() {
		return eleCurrent;
	}

	public void setEleCurrent(BigDecimal eleCurrent) {
		this.eleCurrent = eleCurrent;
	}

	public BigDecimal getVoltage() {
		return voltage;
	}

	public void setVoltage(BigDecimal voltage) {
		this.voltage = voltage;
	}

	public BigDecimal getPower() {
		return power;
	}

	public void setPower(BigDecimal power) {
		this.power = power;
	}

	public String getAirMac() {
		return airMac;
	}

	public void setAirMac(String airMac) {
		this.airMac = airMac;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
