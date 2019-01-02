package com.air.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class AppUser {

	private Integer appusersId;//app用户id
	private Integer partnerId;//合作商id
	private String wxappopenid;//微信openid
	private String aliUserId;//支付宝Id
	private String telephone;//手机号
	private String password;//
	private String headimg;//头像
	private String nickname;//昵称
	private String perm;//用户权限（用户和技术人员）
	private String reviewStatus;//技术员审核状态
	private String sex;//性别
	private String address;//地址
	private Integer vip;//VIP等级(无用)
	private String signature;//个性签名
	private BigDecimal balance;//余额
	private BigDecimal redBoxValue;//红包
	private String email;//邮箱
	private String wxunionid;
	private String wxnickname;// 微信昵称
	private String authUrl;		//上传图片地址资质证书
	private String perCardUrl;		//上传图片地址手持身份证
	private String personUrl;//本人照片
	private String idCardUrl;//身份证
	private String aliname;// zfb相关
	private String aliImg;
	private String model; // 模式 hour/year
	private Long useTime; // 剩余使用时间
	private BigDecimal carbon; //碳排放
	private BigDecimal electricity; //用电量
	private BigDecimal saveElect; //节省电量
	private Long totalTime; //使用总时长
	private Integer zmf;
	private String isDoZmrz;
	private String token;
	private Date created;
	private Date modified;
	private String career;
	private Integer age; //年龄段
	private String partnerName;
	
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
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
	public String getAliUserId() {
		return aliUserId;

	}
	public void setAliUserId(String aliUserId) {
		this.aliUserId = aliUserId;
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

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
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
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public String getPerCardUrl() {
		return perCardUrl;
	}
	public void setPerCardUrl(String perCardUrl) {
		this.perCardUrl = perCardUrl;
	}
	public String getPersonUrl() {
		return personUrl;
	}
	public void setPersonUrl(String personUrl) {
		this.personUrl = personUrl;
	}
	public String getIdCardUrl() {
		return idCardUrl;
	}
	public void setIdCardUrl(String idCardUrl) {
		this.idCardUrl = idCardUrl;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Long getUseTime() {
		return useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}
	public BigDecimal getRedBoxValue() {
		return redBoxValue;
	}
	public void setRedBoxValue(BigDecimal redBoxValue) {
		this.redBoxValue = redBoxValue;
	}
	public BigDecimal getCarbon() {
		return carbon;
	}
	public void setCarbon(BigDecimal carbon) {
		this.carbon = carbon;
	}
	public BigDecimal getElectricity() {
		return electricity;
	}
	public void setElectricity(BigDecimal electricity) {
		this.electricity = electricity;
	}
	public BigDecimal getSaveElect() {
		return saveElect;
	}
	public void setSaveElect(BigDecimal saveElect) {
		this.saveElect = saveElect;
	}
	public Integer getZmf() {
		return zmf;
	}
	public void setZmf(Integer zmf) {
		this.zmf = zmf;
	}
	public String getIdDoZmrz() {
		return isDoZmrz;
	}
	public void setIdDoZmrz(String idDoZmrz) {
		this.isDoZmrz = idDoZmrz;
	}
	public String[] getIdCardUrlList() {
	  return idCardUrl != null ?idCardUrl.split(",") : null;// 身份证图片列表
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	public String getIsDoZmrz() {
		return isDoZmrz;
	}
	public void setIsDoZmrz(String isDoZmrz) {
		this.isDoZmrz = isDoZmrz;
	}
	
}
