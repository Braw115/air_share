package com.air.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.http.conn.ssl.PrivateKeyStrategy;
import org.springframework.web.multipart.MultipartFile;

import com.air.pojo.entity.RedBoxRate;

public class RedBoxActiveVo {
	
	private Integer redBoxActiveId;
	private BigDecimal totalValue;//活动发放红包总金额
	private String activeName;//活动名称
	private BigDecimal conformLevel;//金钱条件（大于或等于可以领取红包）
	private BigDecimal redBoxMaxLevel;//红包最大限额
	private BigDecimal redBoxMinLevel;//红包最小限额
	private String beginTime;//活动开始时间
	private String endTime;//活动截止时间
	private Boolean status ;//活动状态（已发布：true，未发布或下架：false）
	private List<RedBoxRate> rateList;
	private String rateListJson;
	private List<MultipartFile> imgUrlList;
	private String imgUrl;
	private String type;//活动类别 V 优惠券活动 B 红包活动 
	private Integer curPage;
	private Integer pageSize;
	private Date created;
	private Date modified;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRateListJson() {
		return rateListJson;
	}
	public void setRateListJson(String rateListJson) {
		this.rateListJson = rateListJson;
	}
	public List<MultipartFile> getImgUrlList() {
		return imgUrlList;
	}
	public void setImgUrlList(List<MultipartFile> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getRedBoxActiveId() {
		return redBoxActiveId;
	}
	public void setRedBoxActiveId(Integer redBoxActiveId) {
		this.redBoxActiveId = redBoxActiveId;
	}
	public BigDecimal getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}
	public String getActiveName() {
		return activeName;
	}
	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
	public BigDecimal getConformLevel() {
		return conformLevel;
	}
	public void setConformLevel(BigDecimal conformLevel) {
		this.conformLevel = conformLevel;
	}
	public BigDecimal getRedBoxMaxLevel() {
		return redBoxMaxLevel;
	}
	public void setRedBoxMaxLevel(BigDecimal redBoxMaxLevel) {
		this.redBoxMaxLevel = redBoxMaxLevel;
	}
	public BigDecimal getRedBoxMinLevel() {
		return redBoxMinLevel;
	}
	public void setRedBoxMinLevel(BigDecimal redBoxMinLevel) {
		this.redBoxMinLevel = redBoxMinLevel;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public List<RedBoxRate> getRateList() {
		return rateList;
	}
	public void setRateList(List<RedBoxRate> rateList) {
		this.rateList = rateList;
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
