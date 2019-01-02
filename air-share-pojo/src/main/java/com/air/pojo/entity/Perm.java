package com.air.pojo.entity;

import java.util.Date;

public class Perm {
	
	private Integer permId;//权限id
	private String permName;//权限名称
	private String description;//权限描述
	private Date created;
	private Date modified;
	public Integer getPermId() {
		return permId;
	}
	public void setPermId(Integer permId) {
		this.permId = permId;
	}
	public String getPermName() {
		return permName;
	}
	public void setPermName(String permName) {
		this.permName = permName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
