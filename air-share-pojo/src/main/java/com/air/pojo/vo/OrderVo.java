package com.air.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

public class OrderVo {
	

	private Integer ordersId;//订单id
	private Integer appusersId;//用户id
	private String telephone;//用户手机号
	private Integer rechargeId;//购买类型id（包时，包年）
	private Integer activeId;//促销活动id
	private Integer voucherId;//代金券折扣券id
	private BigDecimal theoprice;//理论价格
	private BigDecimal realfee;//实付价格
	private Integer num;//充值时间（小时） 
	private String airmac;//空调物理地址
	private String paystatus;//支付状态
	private String orderno;//订单号
	private String account;//支付账号
	private String paymethod;//支付方式
	private String paytype;//消费类型（充值到余额:value，直接消费购买时间：cost）
	private Integer curPage;//页码
	private Integer pageSize;//每页数据
	private Date created;
	private Date modified;
	private Date paytime;
	private String typeName;
	private String model;
	private Boolean isRead;
	private BigDecimal price;
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(Integer ordersId) {
		this.ordersId = ordersId;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public Integer getRechargeId() {
		return rechargeId;
	}
	public void setRechargeId(Integer rechargeId) {
		this.rechargeId = rechargeId;
	}
	public Integer getActiveId() {
		return activeId;
	}
	public void setActiveId(Integer activeId) {
		this.activeId = activeId;
	}
	public Integer getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}
	public BigDecimal getTheoprice() {
		return theoprice;
	}
	public void setTheoprice(BigDecimal theoprice) {
		this.theoprice = theoprice;
	}
	public BigDecimal getRealfee() {
		return realfee;
	}
	public void setRealfee(BigDecimal realfee) {
		this.realfee = realfee;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getAirmac() {
		return airmac;
	}
	public void setAirmac(String airmac) {
		this.airmac = airmac;
	}
	public String getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
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
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public String getPaymethod() {
		return paymethod;
	}
	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
