package com.air.pojo.entity;

import java.util.Date;

/**
 * 
 * <p>Title: Evaluate</p>
 * <p>Description: 技术人员评价</p>
 * @author czg
 * @date 2018年6月12日
 */
public class Evaluate {
	private Integer evaluateId; // ID
	private Integer appusersId; // 普通用户ID
	private Integer technicianId; // 技术员ID
	private Integer stars; // 星的数量
	private String content; // 评价内容
	private Integer repairId; // 维修记录ID
	private Boolean isNews;	// 新评价
	private String nickName;	// 昵称
	private String headimg;	//头像
	private String technicianName;
	private Repair repair;
	private Date created;
	private Date modified;

	
	public String getTechnicianName() {
		return technicianName;
	}

	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public Repair getRepair() {
		return repair;
	}

	public void setRepair(Repair repair) {
		this.repair = repair;
	}

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

	public Integer getRepairId() {
		return repairId;
	}

	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
	}

	public Boolean getIsNews() {
		return isNews;
	}

	public void setIsNews(Boolean isNews) {
		this.isNews = isNews;
	}
	
}
