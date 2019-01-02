package com.air.pojo.vo;


import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * @author
 *
 */
public class RespVo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean result;

	private String msg;

	private T data;
	
	protected Map<String, Object> properties = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public RespVo(boolean result, String msg, T data) {
		this.result = result;
		this.msg = msg;
		this.data = data;
	}
	
	public RespVo(boolean result, String msg) {
		this.result = result;
		this.msg = msg;
	}
	
	public RespVo(boolean result, T data) {
		this.result = result;
		this.data = data;
	}
	
	public RespVo() {
		
	}
	
	@JsonAnyGetter
	public Map<String, Object> getProperties() {
		return properties;
	}

	@JsonAnySetter
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

}
