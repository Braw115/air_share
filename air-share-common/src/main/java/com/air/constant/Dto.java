package com.air.constant;

public class Dto <T>{

	private Integer status; // 返回成功状态
	private String errorCode;// �ô�����Ϊ�Զ��壬һ��0��ʾ�޴�
	private String msg;// 消息
	private T data;// 数据
	
	
	public Dto() {
		super();
	}

	public Dto(Integer status,  String msg, T data) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public Dto(Integer status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
	
}
