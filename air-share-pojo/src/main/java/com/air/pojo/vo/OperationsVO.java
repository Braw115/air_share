package com.air.pojo.vo;

import java.math.BigDecimal;

import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.ExcelBean;

public class OperationsVO {
	private String mac; // 空调MAC
	private String series; // 系列名称
	private Integer putDate; // 投放天数
	private Integer num; // 成交笔数
	private BigDecimal price; // 成交金额
	private String paytype; // 支付方式
	private String beginDate; // 开始时间
	private String endDate; // 结束时间
	private Integer pageNum; // 页码
	private Integer pageSize; // 每页数量
	private AppUser appUser;
	private Integer appusersId;
	private String username;
	private String telephone ; 
    private String address;
    private BigDecimal carbon;
	
    public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getCarbon() {
		return carbon;
	}

	public void setCarbon(BigDecimal carbon) {
		this.carbon = carbon;
	}

	

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Integer getPutDate() {
		return putDate;
	}

	public void setPutDate(Integer putDate) {
		this.putDate = putDate;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
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

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}
	
}
