package com.air.pojo.entity;

public class ConditionInfo {
	private String mac;
	private String status;// 状态
	private String model; // 模式
	private String windSpeed; // 风速
	private String swing; // 风向
	private String sleep; // 睡眠
	private String strong; // 强力
	private String timing; // 定时
	private String dryHot;	//干燥/辅热
	private Integer temp; // 温度
	private boolean use;
	private Integer appuserId; //使用者id
	

	public Integer getAppuserId() {
		return appuserId;
	}

	public void setAppuserId(Integer appuserId) {
		this.appuserId = appuserId;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}

	public String getDryHot() {
		return dryHot;
	}

	public void setDryHot(String dryHot) {
		this.dryHot = dryHot;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getSwing() {
		return swing;
	}

	public void setSwing(String swing) {
		this.swing = swing;
	}

	public String getSleep() {
		return sleep;
	}

	public void setSleep(String sleep) {
		this.sleep = sleep;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}


	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

}
