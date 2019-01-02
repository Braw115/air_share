package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class VoucherActive {

	private Integer voucherActiveId;
	private Integer totalCount;//发放的代金券和折扣券总数
	private Integer discount;//折扣券的折扣
	private Integer validTime;//有效天数
	private String activeName;//活动名称
	private Boolean status;//活动状态
	private String endTime;//结束时间
	private String beginTime;//开始时间
	private String imgUrl;
	private BigDecimal voucherMinLevel;//代金券和折扣券的最低使用金额
	private BigDecimal faceValue;//代金券面值
	private BigDecimal conformLevel;//最低消费金额金额（满足可领取）
	private Date created;
	private Date modified;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getVoucherActiveId() {
		return voucherActiveId;
	}

	public void setVoucherActiveId(Integer voucherActiveId) {
		this.voucherActiveId = voucherActiveId;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public BigDecimal getVoucherMinLevel() {
		return voucherMinLevel;
	}

	public void setVoucherMinLevel(BigDecimal voucherMinLevel) {
		this.voucherMinLevel = voucherMinLevel;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}

	public BigDecimal getConformLevel() {
		return conformLevel;
	}

	public void setConformLevel(BigDecimal conformLevel) {
		this.conformLevel = conformLevel;
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

	public Integer getValidTime() {
		return validTime;
	}

	public void setValidTime(Integer validTime) {
		this.validTime = validTime;
	}
	
}
