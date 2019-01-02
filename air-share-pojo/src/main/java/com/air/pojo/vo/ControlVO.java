package com.air.pojo.vo;

import java.util.Map;

public class ControlVO {
	private String mac; // 空调MAC
	private String order; // 指令
	private Integer value; // 指令对应的值
	private String model;
	private Long useTime;
	private Integer userId;
	private Map<String, Object> orderMap;	//指令详情
	private Float longitude; // 经度
	private Float latitude; // 纬度
	private String seq; //随机码,确定唯一指令
	private String status;
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
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

	public Map<String, Object> getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map<String, Object> orderMap) {
		this.orderMap = orderMap;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getUseTime() {
		return useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
