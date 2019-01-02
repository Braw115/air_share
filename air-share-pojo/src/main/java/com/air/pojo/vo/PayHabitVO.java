package com.air.pojo.vo;

public class PayHabitVO {
	private Integer payHour; // 时间
	private Integer num; // 支付数量
	private String payMethod;	//支付方式
	private String beginDate;
	private String endDate;

	public Integer getPayHour() {
		return payHour;
	}

	public void setPayHour(Integer payHour) {
		this.payHour = payHour;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
}
