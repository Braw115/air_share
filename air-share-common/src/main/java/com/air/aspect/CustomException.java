package com.air.aspect;

public class CustomException extends Exception {

	private String msg;
	
	public CustomException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
