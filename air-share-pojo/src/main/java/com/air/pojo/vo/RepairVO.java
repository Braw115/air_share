package com.air.pojo.vo;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RepairVO {
	private Integer repairId;
	private Integer appusersId;
	private String airmac; // 空调mac
	private Integer userId; // 维修用户ID
	private String status; // 状态
	private String failPicture; // 故障图片
	private String repairPicture;// 维修图片
	private Date dispatchTime; // 派单时间
	private Date repairTime; // 维修时间
	private String type; // 故障原因
	private String detail; // 清理描述
	private String content; // 故障描述
	private String result; // 处理结果
	private String comment; // 评价
	private Integer stars;	// 星数量
	private Boolean authorization;	// 是否已授权过
	private Integer priority; // 优先级
	private Date created;
	private Date modified;
	private String beginDate; // 开始时间
	private String endDate; // 结束时间
	private String password; // 密码
	List<MultipartFile> file;
	private Integer pageNum;
	private Integer pageSize;
	private Integer partnerId; //合作商id
	private Integer enterpriseId; //业主id
	private String  phone;
	private Integer areaId; //区域id
	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	
	

	
	
	public Integer getAppusersId() {
		return appusersId;
	}

	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public Boolean getAuthorization() {
		return authorization;
	}

	public void setAuthorization(Boolean authorization) {
		this.authorization = authorization;
	}

	public Integer getStart() {
		return stars;
	}

	public void setStart(Integer start) {
		this.stars = start;
	}

	public String[] getFailPictureList() {
		return failPicture != null ?failPicture.split(",") : null;// 故障图片列表
	}

	public String[] getRepairPictureList() {
		return repairPicture != null ?repairPicture.split(",") : null;// 维修图片列表
	}
	
	public Integer getRepairId() {
		return repairId;
	}

	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public List<MultipartFile> getFile() {
		return file;
	}

	public void setFile(List<MultipartFile> file) {
		this.file = file;
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

}
