package com.air.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class AppUserVo {
	
	private Integer appusersId;//app用户id
	private Integer partnerId;//合作商id
	private String wxappopenid;//微信openid
	private String aliUserId;//支付宝Id
	private String telephone;//手机号
	private String password;//
	private List<MultipartFile> headimg;//头像
	private String nickname;//昵称
	private String perm;//用户权限（用户和技术人员）
	private String reviewStatus;//技术人员审核状态
	private String sex;//性别
	private String address;//地址
	private Integer vip;//VIP等级
	private String signature;//个性签名
	private String email;//邮箱
	private String wxunionid;
	private String wxnickname;//微信昵称
	private BigDecimal balance;//余额
	private List<MultipartFile> authUrl;		//上传图片地址资质证书
	private List<MultipartFile> perCardUrl;		//上传图片地址手持身份证
	private List<MultipartFile> personUrl;//本人照片
	private List<MultipartFile> idCardUrl;//身份证
	private String aliname;//zfb相关
	private String aliImg;
	private Date created;				
	private Date modified;
	private String phone;//手机号
	private String msg;//验证码
	private String token;
	private Long totalTime;
	private Integer curPage ;
	private Integer pageSize;
	private String isDoZmrz;
	private String career;
	private Integer age; //年龄段 0:18以下 1:18-30 2:31-45 3:45以上
	
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
	public String getIsDoZmrz() {
		return isDoZmrz;
	}
	public void setIsDoZmrz(String isDoZmrz) {
		this.isDoZmrz = isDoZmrz;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public String getAliUserId() {
		return aliUserId;
	}
	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Integer getAppusersId() {
		return appusersId;
	}
	public void setAppusersId(Integer appusersId) {
		this.appusersId = appusersId;
	}
	public String getWxappopenid() {
		return wxappopenid;
	}
	public void setWxappopenid(String wxappopenid) {
		this.wxappopenid = wxappopenid;
	}
	public String getAliopenId() {
		return aliUserId;
	}
	public void setAliopenId(String aliopenId) {
		this.aliUserId = aliopenId;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<MultipartFile> getHeadimg() {
		return headimg;
	}
	public void setHeadimg(List<MultipartFile> headimg) {
		this.headimg = headimg;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPerm() {
		return perm;
	}
	public void setPerm(String perm) {
		this.perm = perm;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getVip() {
		return vip;
	}
	public void setVip(Integer vip) {
		this.vip = vip;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWxunionid() {
		return wxunionid;
	}
	public void setWxunionid(String wxunionid) {
		this.wxunionid = wxunionid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getWxnickname() {
		return wxnickname;
	}
	public void setWxnickname(String wxnickname) {
		this.wxnickname = wxnickname;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<MultipartFile> getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(List<MultipartFile> authUrl) {
		this.authUrl = authUrl;
	}
	public List<MultipartFile> getPerCardUrl() {
		return perCardUrl;
	}
	public void setPerCardUrl(List<MultipartFile> perCardUrl) {
		this.perCardUrl = perCardUrl;
	}
	public List<MultipartFile> getPersonUrl() {
		return personUrl;
	}
	public void setPersonUrl(List<MultipartFile> personUrl) {
		this.personUrl = personUrl;
	}
	public List<MultipartFile> getIdCardUrl() {
		return idCardUrl;
	}
	public void setIdCardUrl(List<MultipartFile> idCardUrl) {
		this.idCardUrl = idCardUrl;
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
	public String getAliname() {
		return aliname;
	}
	public void setAliname(String aliname) {
		this.aliname = aliname;
	}
	public String getAliImg() {
		return aliImg;
	}
	public void setAliImg(String aliImg) {
		this.aliImg = aliImg;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}


}
