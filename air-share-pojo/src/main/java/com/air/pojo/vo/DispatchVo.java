package com.air.pojo.vo;

import java.util.Date;

public class DispatchVo {
	private Integer repairId;//工单ID
	private Integer appusersId;
	private String airmac; // 空调mac
	private Integer userId; // 维修用户ID
	private String type; // 故障原因
	private String detail; // 清理描述
	private String content; // 故障描述
	private String comment; // 评价
	private String status; // 状态
	private Boolean service;
	private Boolean authorization;	// 是否已授权过
	private Integer priority; // 优先级
	private String failPicture; // 故障图片
	private String repairPicture;// 维修图片
	private Date dispatchTime; // 派单时间
	private Date repairTime; // 维修时间
	private String result; // 处理结果
	private Date created;
	private Date modified;
	private String phone;
	private  Integer partnerId;
	
	public Integer getRepairId() {
		return repairId;
	}
	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public String getAirmac() {
		return airmac;
	}
	public void setAirmac(String airmac) {
		this.airmac = airmac;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getService() {
		return service;
	}
	public void setService(Boolean service) {
		this.service = service;
	}
	public Boolean getAuthorization() {
		return authorization;
	}
	public void setAuthorization(Boolean authorization) {
		this.authorization = authorization;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getFailPicture() {
		return failPicture;
	}
	public void setFailPicture(String failPicture) {
		this.failPicture = failPicture;
	}
	public String getRepairPicture() {
		return repairPicture;
	}
	public void setRepairPicture(String repairPicture) {
		this.repairPicture = repairPicture;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public Date getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	

}
