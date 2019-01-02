package com.air.pojo.entity;

import java.util.Date;


/**
 * 
 * <p>Title: Repair</p>
 * <p> Description:维修表</p>
 * @author czg
 * @date 2018年6月5日
 */
public class Repair {
	private Integer repairId;//工单ID
	private Integer appusersId;//报修用户
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
	private Integer stars;	//星星个数
	private Integer priority; // 优先级
	private String nickName;	// 维修人员昵称
	private String username;	// 合作商名称
	private Boolean authorization;	// 是否已授权过
	private Boolean service;		//是否维修中
	private AirCondition conditions;
	private Date created;
	private Date modified;
	private String phone;//报修用户电话
	private String repairIds; //派单人员(数组)
	private Date grabOrderTime; //抢单时间
	private String repairPhone; //维修人员电话

	
	

	public String getRepairPhone() {
		return repairPhone;
	}

	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}

	public Date getGrabOrderTime() {
		return grabOrderTime;
	}

	public void setGrabOrderTime(Date grabOrderTime) {
		this.grabOrderTime = grabOrderTime;
	}

	public String getRepairIds() {
		return repairIds;
	}

	public void setRepairIds(String repairIds) {
		this.repairIds = repairIds;
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

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getFailPictureList() {
		return failPicture != null ?failPicture.split(",") : null;// 故障图片列表
	}

	public String[] getRepairPictureList() {
		return repairPicture != null ?repairPicture.split(",") : null;// 维修图片列表
	}

	public AirCondition getConditions() {
		return conditions;
	}

	public void setConditions(AirCondition conditions) {
		this.conditions = conditions;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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
