package com.air.pojo.entity;

public class PromotionInfo {//促销统计信息
	
	private Integer activeId;
	private String activeName;
	private String name;//促销设备名称
	private String protime;//促销时间
	private Integer hours;//时
	private Integer years;//年
	private Float realpay;//实际付的总金额
	private Float totalpay;//折算前金额
	private Float voupay;//使用代金券金额
	private Float dispay;//使用折扣券金额
	
	public String getActiveName() {
		return activeName;
	}
	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
	public Integer getActiveId() {
		return activeId;
	}
	public void setActiveId(Integer activeId) {
		this.activeId = activeId;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public Integer getYears() {
		return years;
	}
	public void setYears(Integer years) {
		this.years = years;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtime() {
		return protime;
	}
	public void setProtime(String protime) {
		this.protime = protime;
	}
	public Float getRealpay() {
		return realpay;
	}
	public void setRealpay(Float realpay) {
		this.realpay = realpay;
	}
	public Float getTotalpay() {
		return totalpay;
	}
	public void setTotalpay(Float totalpay) {
		this.totalpay = totalpay;
	}
	public Float getVoupay() {
		return voupay;
	}
	public void setVoupay(Float voupay) {
		this.voupay = voupay;
	}
	public Float getDispay() {
		return dispay;
	}
	public void setDispay(Float dispay) {
		this.dispay = dispay;
	}
	
	

}
