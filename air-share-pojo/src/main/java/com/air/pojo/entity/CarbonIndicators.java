package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * <p>
 * Title: CarbonIndicators
 * </p>
 * <p>
 * Description: 碳指标
 * </p>
 * 
 * @author czg
 * @date 2018年6月25日
 */
public class CarbonIndicators {
	private Integer indicatorsId;
	private String mac;
	private Long useTime; // 使用时长
	private BigDecimal electricity; // 用电量
	private BigDecimal catbon; // 碳指标量
	private Integer appusersId; // 使用人ID
	private Date openTime;
	private Date closeTime;
	private Date created;
	private String date;

	public Integer getIndicatorsId() {
		return indicatorsId;
	}
	public void setIndicatorsId(Integer indicatorsId) {
		this.indicatorsId = indicatorsId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Long getUseTime() {
		return useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	public BigDecimal getElectricity() {
		return electricity;
	}

	public void setElectricity(BigDecimal electricity) {
		this.electricity = electricity;
	}

	public BigDecimal getCatbon() {
		return catbon;
	}

	public void setCatbon(BigDecimal catbon) {
		this.catbon = catbon;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	
	
}
