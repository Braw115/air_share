package com.air.pojo.entity;

import java.util.Date;

public class CrmUserAndRole {
	
	private Integer userRoleId;
	private Integer crmuserId;
	private Integer roleId;
	private Date created;
	private Date modified;
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getCrmuserId() {
		return crmuserId;
	}
	public void setCrmuserId(Integer crmuserId) {
		this.crmuserId = crmuserId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
