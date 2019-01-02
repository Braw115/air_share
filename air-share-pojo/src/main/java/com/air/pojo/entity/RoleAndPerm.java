package com.air.pojo.entity;

import java.util.Date;

public class RoleAndPerm {
	private Integer rolePermId;
	private Integer permId;
	private Integer roleId;
	private Date created;
	private Date modified;
	public Integer getRolePermId() {
		return rolePermId;
	}
	public void setRolePermId(Integer rolePermId) {
		this.rolePermId = rolePermId;
	}
	public Integer getPermId() {
		return permId;
	}
	public void setPermId(Integer permId) {
		this.permId = permId;
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
