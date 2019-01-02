package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;

/**
 * 空调表
 * 
 * @author czg
 *
 */
public class AirCondition {
	private String mac; // 空调MAC地址
	private Date useTime; // 开始使用时间
	private Date operationTime; // 运营时间
	private Float longitude; // 经度
	private Float latitude; // 纬度
	private Date newUseTime; // 最近使用时间
	private Date newRepairTime; // 最近维修时间
	private Boolean fault; // 是否故障
	private Integer seriesId; // 空调系列号id
	private Integer enterpriseId; // 业主id
	private String enterprise; // 业主
	private Integer areaId; // 区域id
	private Integer rechargeId;//购买类型id（包时，包年）
	private String location; // 位置信息
	private Integer useStatus; // 使用状态 -1 离线 0在线  1 使用中
	private Integer windSpeed; // 风速
	private String model; // 模式
	private Integer partnerId;//合作商ID
	private boolean flag ;//标识是否可以使用 true可用 false不可用
	private Date created;
	private Date modified;
	
	private BigDecimal year;
	private BigDecimal hour;
    private Integer power;
    private Double powerType;//空调功率类型：1p 1.5p
    private Long time;
    private BigDecimal voltage;	// 标准电压
    private String authorization;//开启是否通过授权 n没授权 y授权
    
    
    
    public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
    
	public Double getPowerType() {
		return powerType;
	}

	public void setPowerType(Double powerType) {
		this.powerType = powerType;
	}

	public BigDecimal getVoltage() {
		return voltage;
	}

	public void setVoltage(BigDecimal voltage) {
		this.voltage = voltage;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public BigDecimal getYear() {
		return year;
	}

	public void setYear(BigDecimal year) {
		this.year = year;
	}

	public BigDecimal getHour() {
		return hour;
	}

	public void setHour(BigDecimal hour) {
		this.hour = hour;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Date getUseTime() {
		return useTime;
	}

	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Date getNewRepairTime() {
		return newRepairTime;
	}

	public void setNewRepairTime(Date newRepairTime) {
		this.newRepairTime = newRepairTime;
	}

	public Boolean getFault() {
		return fault;
	}

	public void setFault(Boolean fault) {
		this.fault = fault;
	}

	public Integer getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}

	public Integer getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Integer windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}

	public Date getNewUseTime() {
		return newUseTime;
	}

	public void setNewUseTime(Date newUseTime) {
		this.newUseTime = newUseTime;
	}	

	/*
	 * @Override public boolean equals(Object obj) { boolean eq = true; if (obj
	 * instanceof AirCondition) { AirCondition air = (AirCondition) obj; if
	 * (StringUtils.isEmpty(mac) || !mac.equals(air.getMac())) { return false; } if
	 * (electricity != air.getElectricity() || useTime != air.getUseTime()) { return
	 * false; } if (eq) {
	 * 
	 * } System.out.println("equal"+ name.id); return eq; } return
	 * super.equals(obj); }
	 */

}
