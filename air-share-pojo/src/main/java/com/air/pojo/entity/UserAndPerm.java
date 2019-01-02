package com.air.pojo.entity;

import java.util.Date;
import java.util.List;

public class UserAndPerm {
	private Integer userPermId;
	private Integer crmUserId;
	private Integer permId;
	private Date created;
	private Date modified;
	
	public Integer getUserPermId() {
		return userPermId;
	}
	public void setUserPermId(Integer userPermId) {
		this.userPermId = userPermId;
	}
	public Integer getCrmUserId() {
		return crmUserId;
	}
	public void setCrmUserId(Integer crmUserId) {
		this.crmUserId = crmUserId;
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
	public Integer getPermId() {
		return permId;
	}
	public void setPermId(Integer permId) {
		this.permId = permId;
	}
	
}
