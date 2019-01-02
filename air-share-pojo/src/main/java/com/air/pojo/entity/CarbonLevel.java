package com.air.pojo.entity;

import java.util.Date;

public class CarbonLevel {
	private Integer levelId;
	private Integer carbonLevel;
	private Integer min;
	private Integer max;
	private Date created;
	private Date modified;

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getCarbonLevel() {
		return carbonLevel;
	}

	public void setCarbonLevel(Integer carbonLevel) {
		this.carbonLevel = carbonLevel;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
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
