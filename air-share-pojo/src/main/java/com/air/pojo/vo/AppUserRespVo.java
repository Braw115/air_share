package com.air.pojo.vo;


public class AppUserRespVo {

	private Integer appusersId;
	
	private String nickname;
	
	private String telephone;
	
	private String token;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String wxappopenid;//微信openid
	private String wxunionid;//微信unionid
	private String zfbId;//支付宝Id
	
	public String getWxappopenid() {
		return wxappopenid;
	}

	public void setWxappopenid(String wxappopenid) {
		this.wxappopenid = wxappopenid;
	}

	public String getWxunionid() {
		return wxunionid;
	}

	public void setWxunionid(String wxunionid) {
		this.wxunionid = wxunionid;
	}

	public String getZfbId() {
		return zfbId;
	}

	public void setZfbId(String zfbId) {
		this.zfbId = zfbId;
	}
	// 用户此次登陆，是否会进行注册
	public boolean isregaction;
	private boolean ispwdempty;

	public boolean isIsregaction() {
		return isregaction;
	}

	public void setIsregaction(boolean isregaction) {
		this.isregaction = isregaction;
	}

	public boolean isIspwdempty() {
		return ispwdempty;
	}

	public void setIspwdempty(boolean ispwdempty) {
		this.ispwdempty = ispwdempty;
	}
	
	
	
}
