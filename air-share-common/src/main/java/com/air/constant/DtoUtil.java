package com.air.constant;

public class DtoUtil{

	public static Integer success = 200;

	public static Integer fail = 400;

	public static String errorCode = "0";

	public static Dto returnSuccess() {
		Dto dto = new Dto();
		dto.setStatus(success);
		return dto;
	}

	public static Dto returnSuccess(String message, Object data) {
		Dto dto = new Dto();
		dto.setStatus(success);
		dto.setMsg(message);
		dto.setErrorCode(errorCode);
		dto.setData(data);
		return dto;
	}

	public static Dto returnSuccess(String message) {
		Dto dto = new Dto();
		dto.setStatus(success);
		dto.setMsg(message);
		dto.setErrorCode(errorCode);
		return dto;
	}

	public static Dto returnDataSuccess(Object data) {
		Dto dto = new Dto();
		dto.setStatus(success);
		dto.setErrorCode(errorCode);
		dto.setData(data);
		return dto;
	}

	public static Dto returnFail(String message, String errorCode) {
		Dto dto = new Dto();
		dto.setStatus(fail);
		dto.setMsg(message);
		dto.setErrorCode(errorCode);
		return dto;
	}
	public static Dto returnFail(String message, String errorCode, Object data) {
		Dto dto = new Dto();
		dto.setStatus(fail);
		dto.setMsg(message);
		dto.setErrorCode(errorCode);
		dto.setData(data);
		return dto;
	}

	public static Integer getSuccess() {
		return success;
	}

	public static void setSuccess(Integer success) {
		DtoUtil.success = success;
	}

	public static Integer getFail() {
		return fail;
	}

	public static void setFail(Integer fail) {
		DtoUtil.fail = fail;
	}

	public static String getErrorCode() {
		return errorCode;
	}

	public static void setErrorCode(String errorCode) {
		DtoUtil.errorCode = errorCode;
	}
		
}
