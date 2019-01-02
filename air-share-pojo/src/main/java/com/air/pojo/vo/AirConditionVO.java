package com.air.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.air.pojo.entity.Repair;

public class AirConditionVO {
	private String mac; // 空调MAC地址
	private Integer electricity; // 用电量
	private Date useTime; // 开始使用时间
	private Date operationTime; // 运营时间
	private Float longitude; // 经度
	private Float latitude; // 纬度
	private Date newUseTime; // 最近充值时间
	private Date newRepairTime; // 最近维修时间
	private Integer balance; // 剩余时间
	private Date lastUpdate; //
	private Boolean fault; // 是否故障
	private Integer seriesId; // 空调系列号id
	private Integer enterpriseId; // 加盟企业id
	private String username; // 加盟企业名字
	private Integer areaId; // 区域id
	private String location; // 位置信息
	private Integer useStatus; // 使用状态
	private String name; // 型号
	private String specification; // 规格
	private String type; // 系列
	private Integer num; // 库存
	private String catena; // 系类
	private List<Repair> repairs;
	private String areaName; // 区域名
	private Date created;
	private Date modified;
	private String beginDate;
	private String endDate;
	private Integer pageNum;
	private Integer pageSize;
	private Integer partnerId;
	
	private BigDecimal year;
	private BigDecimal hour;
    private Integer power;//功率
    private Double powerType;//空调功率类型：1p 1.5p
    private Long time;
    private BigDecimal voltage;	// 标准电压
    
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getElectricity() {
		return electricity;
	}

	public void setElectricity(Integer electricity) {
		this.electricity = electricity;
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

	public Date getNewUseTime() {
		return newUseTime;
	}

	public void setNewUseTime(Date newUseTime) {
		this.newUseTime = newUseTime;
	}

	public Date getNewRepairTime() {
		return newRepairTime;
	}

	public void setNewRepairTime(Date newRepairTime) {
		this.newRepairTime = newRepairTime;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCatena() {
		return catena;
	}

	public void setCatena(String catena) {
		this.catena = catena;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Repair> getRepairs() {
		return repairs;
	}

	public void setRepairs(List<Repair> repairs) {
		this.repairs = repairs;
	}
	
	
}
