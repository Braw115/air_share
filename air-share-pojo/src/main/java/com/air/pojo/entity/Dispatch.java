package com.air.pojo.entity;
/**
 * 派单表
 * @author lxl
 *
 */
public class Dispatch {
	private Integer dispatchId;
	private Integer repairId;
	private Integer userId;
	
	public Integer getDispathId() {
		return dispatchId;
	}
	public void setDispathId(Integer dispathId) {
		this.dispatchId = dispathId;
	}
	public Integer getRepairId() {
		return repairId;
	}
	public void setRepairId(Integer repairId) {
		this.repairId = repairId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
