package com.air.constant;


public class AppException extends RuntimeException {
	private String code;
	private String message;

	public AppException(String code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}
}