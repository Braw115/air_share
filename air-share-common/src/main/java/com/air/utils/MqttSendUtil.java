package com.air.utils;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class MqttSendUtil {
	
	/**
	 * 
	 * <p>Title: Map2StrAndLen</p>
	 * <p>Description: Map对象转JSON字符串+char值和</p>
	 * @param map
	 * @return
	 */
	public static String Map2StrAndLen(Map map) {
		String str = JSONObject.toJSONString(map);
		Long count = 0l;
		for (char i : str.toCharArray()) {
			count += i;
		}
		return str+count;
	}
	
	public static Long Byte2StrAndLen(byte[] bytes) {
		String str = JSONObject.toJSONString(bytes);
		Long count = 0l;
		for (char i : str.toCharArray()) {
			count += i;
		}
		return count;
	}
}
