package com.air.pojo.vo;

public class BoxAndVoucherVo {
	
	private Integer id;//通用id 
	private String activeName;//标题
	private String imgUrl;//图片地址
	private String type;//类型
	private String imgTurn;//弹出图片
	private String imgDetail;//详情图片
	
	public String getImgTurn() {
		return imgTurn;
	}
	public void setImgTurn(String imgTurn) {
		this.imgTurn = imgTurn;
	}
	public String getImgDetail() {
		return imgDetail;
	}
	public void setImgDetail(String imgDetail) {
		this.imgDetail = imgDetail;
	}
	public String getActiveName() {
		return activeName;
	}
	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
