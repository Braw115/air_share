package com.air.pojo.entity;

import java.util.Date;
/**
 * 
 * <p>Title: Series</p>
 * <p>Description: 空调系列</p>
 * @author czg
 * @date 2018年6月12日
 */
public class Series {
	private Integer seriesId; // 编号
	private String name; // 系列名字
	private String specification; // 规格
	private String type; // 类别
	private Integer num; // 库存
	private String catena; // 系类
	private Float price; // 单价
	private Date created;
	private Date modified;
	private String order_date;
	

	public String getOrder_date() {
		return order_date;
	}

	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Integer getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Integer seriesId) {
		this.seriesId = seriesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCatena() {
		return catena;
	}

	public void setCatena(String catena) {
		this.catena = catena;
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
