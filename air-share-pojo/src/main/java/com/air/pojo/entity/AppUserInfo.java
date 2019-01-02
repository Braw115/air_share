package com.air.pojo.entity;

import java.util.Date;

public class AppUserInfo {
	
	//会员姓名、手机号、微信昵称、头像、首次消费时间、消费次数、消费金额、优惠金额、累计领取代金券张数 
	private Integer appusersId;
	private String nickname;
	private String telephone;
	private String wxnickname;//微信昵称
	private String headimg;
	private Integer number;//消费次数
	private Date firsttime;//首次消费时间
	private Float amountpay;//消费金额
	private Float amountfree;//优惠金额
	private Integer vounumber;//累积领取代金券张数
	private String career;
	private Integer age; //年龄段
	private String sex;//性别
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getWxnickname() {
		return wxnickname;
	}
	public void setWxnickname(String wxnickname) {
		this.wxnickname = wxnickname;
	}
	public String getHeadimg() {
		return headimg;
	}
	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Date getFirsttime() {
		return firsttime;
	}
	public void setFirsttime(Date firsttime) {
		this.firsttime = firsttime;
	}
	public Float getAmountpay() {
		return amountpay;
	}
	public void setAmountpay(Float amountpay) {
		this.amountpay = amountpay;
	}
	public Float getAmountfree() {
		return amountfree;
	}
	public void setAmountfree(Float amountfree) {
		this.amountfree = amountfree;
	}
	public Integer getVounumber() {
		return vounumber;
	}
	public void setVounumber(Integer vounumber) {
		this.vounumber = vounumber;
	}
	
	
	
}
