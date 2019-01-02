package com.air.pojo.vo;

import java.util.Date;

public class BindingVO {
	private String mac; // 空调MAC地址
	private Date useTime; // 开始使用时间
	private Date operationTime; // 运营时间
	private Float longitude; // 经度
	private Float latitude; // 纬度
	private Date newUseTime; // 最近使用时间
	private Integer seriesId; // 空调系列号id
	private Integer enterpriseId; // 加盟企业id
	private Integer areaId; // 区域id
	private String username; // 业主名
	private String appNickName; //维修人员的名字
	private String password;	//确认密码
	private String location;	//空调位置
	private Date bindingDate;	//绑定时间
	private Integer partnerId; //合作商ID
	private String type; // 型号
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	public Date getBindingDate() {
		return bindingDate;
	}
	public void setBindingDate(Date bindingDate) {
		this.bindingDate = bindingDate;
	}
	public String getAppNickName() {
		return appNickName;
	}
	public void setAppNickName(String appNickName) {
		this.appNickName = appNickName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
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
	public Date getNewUseTime() {
		return newUseTime;
	}
	public void setNewUseTime(Date newUseTime) {
		this.newUseTime = newUseTime;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
