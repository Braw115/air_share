package com.air.pojo.vo;

public class ZFBUserVo {
	
	public String user_id;//支付宝用户id	String	支付宝用户的Use_id	必填	2088102104794936
	public String imgurl;//用户头像	String	如果没有数据的时候不会返回该数据，请做好容错	必填	https://tfsimg.alipay.com/images/partner/T1k0xiXXRnXXXXXXXX
	public String nickname;//用户昵称	String	如果没有数据的时候不会返回该数据，请做好容错	必填	张三
	public String province;//省份	String	用户注册时填写的省份 如果没有数据的时候不会返回该数据，请做好容错	必填	浙江省
	public String city;//城市	String	用户注册时填写的城市， 如果没有数据的时候不会返回该数据，请做好容错	必填	杭州
	public String gender;//用户性别	String	M为男性，F为女性， 如果没有数据的时候不会返回该数据，请做好容错	可空	M
	public String user_type;//用户类型 	String	1代表公司账户2代表个人账户 	可空 	1 
	public String user_status;//用户状态	String 	Q代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户 	可空 	T 
	public String is_certified;//是否通过实名认证	String 	T是通过 F是没有实名认证 	可空 	T 
	public String is_student_certified;//是否是学生	String 	T是学生 F不是学生 	可空 	T 
	
	private String phone;
	private String smscode;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	public String getIs_certified() {
		return is_certified;
	}
	public void setIs_certified(String is_certified) {
		this.is_certified = is_certified;
	}
	public String getIs_student_certified() {
		return is_student_certified;
	}
	public void setIs_student_certified(String is_student_certified) {
		this.is_student_certified = is_student_certified;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSmscode() {
		return smscode;
	}
	public void setSmscode(String smscode) {
		this.smscode = smscode;
	}

	
}
