package com.air.pojo.vo;

import java.math.BigDecimal;

public class PersonalRecordVO {
	private BigDecimal carbon;
	private Long useTime;
	private BigDecimal electricity;
	private BigDecimal saveElect;
	private Integer carbonLevel;
	private Long curTime;
	private String mac; 
	private String note;
	private Integer enterpriseId;
	private Boolean useStatus;
	private String name;
	private Boolean service; // 是否维修中
	private Boolean binding; //是否绑定
	private Integer repairId;//工单ID
	private Long beginTime; //开始使用时间
	
	
	
	
	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Boolean getService() {
		return service;
	}

	public void setService(Boolean service) {
		this.service = service;
	}

	public Integer getRepairId() {
		return repairId;
	}

	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
	}

	public Boolean getBinding() {
		return binding;
	}

	public void setBinding(Boolean binding) {
		this.binding = binding;
	}

	public Boolean getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Boolean useStatus) {
		this.useStatus = useStatus;
	}

	public BigDecimal getCarbon() {
		return carbon;
	}

	public void setCarbon(BigDecimal carbon) {
		this.carbon = carbon;
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

	public BigDecimal getSaveElect() {
		return saveElect;
	}

	public void setSaveElect(BigDecimal saveElect) {
		this.saveElect = saveElect;
	}

	public Integer getCarbonLevel() {
		return carbonLevel;
	}

	public void setCarbonLevel(Integer carbonLevel) {
		this.carbonLevel = carbonLevel;
	}

	public Long getCurTime() {
		return curTime;
	}

	public void setCurTime(Long curTime) {
		this.curTime = curTime;
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

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
