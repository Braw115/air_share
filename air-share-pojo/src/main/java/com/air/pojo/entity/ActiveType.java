package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ActiveType {
	
	private Integer activeId;//营销促销id
	private Integer seriesId;//设备系列id
	private Integer areaId;//区域id
	private BigDecimal yearPrice;//促销包年价格
//	private BigDecimal hourPrice;//促销包时价格
	private Float discount;//促销折扣
	private String beginTime;//开始时间
	private String activeName;//活动名称
	private String endTime;//结束时间
	private Boolean shelves;//是否发布
	private String img;//首页轮播图
	private String imgTurn;//首页图
	private String imgDetail;//活动详情图
	private String type;//活动类别（dis 折扣促销 pri 价格促销）
	private Date created;
	private Date modified;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public Integer getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public BigDecimal getYearPrice() {
		return yearPrice;
	}
	public void setYearPrice(BigDecimal yearPrice) {
		this.yearPrice = yearPrice;
	}
//	public BigDecimal getHourPrice() {
//		return hourPrice;
//	}
//	public void setHourPrice(BigDecimal hourPrice) {
//		this.hourPrice = hourPrice;
//	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
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
	public Boolean getShelves() {
		return shelves;
	}
	public void setShelves(Boolean shelves) {
		this.shelves = shelves;
	}
	public String getImg() {
		return img;
	}
	public String[] getImgList() {
		return null==img?null:img.split(",");
	}
	public String getImgTurn() {
		return imgTurn;
	}
	public void setImgTurn(String imgTurn) {
		this.imgTurn = imgTurn;
	}
	public String[] getImgTurnList() {
		return null==imgTurn?null:imgTurn.split(",");
	}
	public String getImgDetail() {
		return imgDetail;
	}
	public void setImgDetail(String imgDetail) {
		this.imgDetail = imgDetail;
	}
	public String[] getImgDetailList() {
		return null==imgDetail?null:imgDetail.split(",");
	}
	public void setImg(String img) {
		this.img = img;
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
	
	
}
