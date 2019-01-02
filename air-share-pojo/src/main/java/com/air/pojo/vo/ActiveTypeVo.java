package com.air.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ActiveTypeVo {
	
	private Integer activeId;//营销促销id
	private String activeName;//促销活动名称
	private Integer seriesId;//设备系列id
	private Integer areaId;//区域id
//	private BigDecimal hourPrice;//促销价格包时
	private BigDecimal yearPrice;//促销价格包年
	private Float discount;//促销折扣
	private String beginTime;//开始时间
	private String endTime;//结束时间
	private Boolean shelves;//是否发布
	private List<MultipartFile> picture;		//上传图片地址(主页显示的图)
	private List<MultipartFile> imgTurn;		//上传图片地址(登录成功后跳出来的图片)
	private List<MultipartFile> imgDetail;		//上传图片地址(详情页图片)
	private Date created;
	private Date modified;
	private String airmac;
	private String nowTime;
	private Integer curPage;
	private Integer pageSize;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<MultipartFile> getImgTurn() {
		return imgTurn;
	}
	public void setImgTurn(List<MultipartFile> imgTurn) {
		this.imgTurn = imgTurn;
	}
	public List<MultipartFile> getImgDetail() {
		return imgDetail;
	}
	public void setImgDetail(List<MultipartFile> imgDetail) {
		this.imgDetail = imgDetail;
	}
	public String getActiveName() {
		return activeName;
	}
	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
	public String getNowTime() {
		return nowTime;
	}
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	public String getAirmac() {
		return airmac;
	}
	public void setAirmac(String airmac) {
		this.airmac = airmac;
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
//	public BigDecimal getHourPrice() {
//		return hourPrice;
//	}
//	public void setHourPrice(BigDecimal hourPrice) {
//		this.hourPrice = hourPrice;
//	}
	public BigDecimal getYearPrice() {
		return yearPrice;
	}
	public void setYearPrice(BigDecimal yearPrice) {
		this.yearPrice = yearPrice;
	}
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
	public List<MultipartFile> getPicture() {
		return picture;
	}
	public void setPicture(List<MultipartFile> picture) {
		this.picture = picture;
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
