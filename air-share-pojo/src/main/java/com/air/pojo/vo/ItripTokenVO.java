package com.air.pojo.vo;

import java.io.Serializable;

/**
 *返回前端-Token相关VO
 */
public class ItripTokenVO implements Serializable{

	/**
	 * 用户认证凭据
	 */
	private String token;
	/**
	 * 过期时间
	 */
	private long expTime;
	/**
	 * 生成时间
	 */
	private long genTime;
	
	private Integer accountId;
	private String identity;
	private Integer beadHouseid;
	private Integer comId;
	private String userName;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpTime() {
		return expTime;
	}
	public void setExpTime(long expTime) {
		this.expTime = expTime;
	}
	public long getGenTime() {
		return genTime;
	}
	public void setGenTime(long genTime) {
		this.genTime = genTime;
	}
	
	public ItripTokenVO() {
		super();
	}
	public ItripTokenVO(String token, long expTime, long genTime) {
		super();
		this.token = token;
		this.expTime = expTime;
		this.genTime = genTime;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public Integer getBeadHouseid() {
		return beadHouseid;
	}
	public void setBeadHouseid(Integer beadHouseid) {
		this.beadHouseid = beadHouseid;
	}
	public Integer getComId() {
		return comId;
	}
	public void setComId(Integer comId) {
		this.comId = comId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
