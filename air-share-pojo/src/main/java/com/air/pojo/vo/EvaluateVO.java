package com.air.pojo.vo;

import java.util.Date;

public class EvaluateVO {
	private Integer evaluateId; // ID
	private Integer appusersId; // 普通用户ID
	private Integer technicianId; // 技术员ID
	private Integer stars; // 星的数量
	private String content; // 评价内容
	private Integer repairId; // 维修记录ID
	private Date created;
	private Date modified;
	private String beginDate;
	private String endDate;
	private Integer pageNum;
	private Integer pageSize;

	public Integer getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(Integer evaluateId) {
		this.evaluateId = evaluateId;
	}

	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getRepairId() {
		return repairId;
	}

	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
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

}
