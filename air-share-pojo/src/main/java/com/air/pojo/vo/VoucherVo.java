package com.air.pojo.vo;

import java.math.BigDecimal;

public class VoucherVo {

	private Integer voucherId;
	private Integer appusersId;
	private Integer voucherActiveId;
	private BigDecimal facevalue;//代金券面额
	private BigDecimal minimum;//最低额度
	private String type;//券类型(voucher:代金券 , discount:折扣券)
	private Integer discountRatio;//折扣比例 需除以100
	private String status;//券的状态
	private String beginTime;
	private String endTime;
	public Integer getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public Integer getVoucherActiveId() {
		return voucherActiveId;
	}
	public void setVoucherActiveId(Integer voucherActiveId) {
		this.voucherActiveId = voucherActiveId;
	}
	public BigDecimal getFacevalue() {
		return facevalue;
	}
	public void setFacevalue(BigDecimal facevalue) {
		this.facevalue = facevalue;
	}
	public BigDecimal getMinimum() {
		return minimum;
	}
	public void setMinimum(BigDecimal minimum) {
		this.minimum = minimum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getDiscountRatio() {
		return discountRatio;
	}
	public void setDiscountRatio(Integer discountRatio) {
		this.discountRatio = discountRatio;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
