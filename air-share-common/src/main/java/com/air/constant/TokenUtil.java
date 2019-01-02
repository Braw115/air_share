package com.air.constant;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;

import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CrmUser;
import com.air.redis.RedisAPI;
import com.air.utils.EmptyUtils;
import com.alibaba.fastjson.JSONObject;


public class TokenUtil {
	private static RedisAPI redisApi;
	
	public RedisAPI getRedisApi() {
		return redisApi;
	}

	public void setRedisApi(RedisAPI redisApi) {
		this.redisApi = redisApi;
	}
	public static Integer getAppUserId(String token) {
		//从redis中获取userId
		String str = redisApi.get(token);
		JSONObject json = JSONObject.parseObject(str);
		Integer appusersId = json.getInteger("appusersId");
		return appusersId;
	}
	public static boolean respAppReturn(String token) {
		Integer appusersId = getAppUserId(token);
		if (appusersId == null) {
			return false;
		}
		return true;
	}

	public static Integer getCrmUserId(String token) {
		//从redis中获取userId
		String str = redisApi.get(token);
		JSONObject json = JSONObject.parseObject(str);
		Integer crmuserId = json.getInteger("crmuserId");
		return crmuserId;
	}
	
	public static CrmUser getCrmUser(String token) {
		//从redis中获取crmuser
		String str = redisApi.get(token);
		CrmUser crmUser = JSONObject.parseObject(str, CrmUser.class);
		return crmUser;
	}

	public static AppUser getAppUser(String token) {
		//从redis中获取appuser
		String str = redisApi.get(token);
		AppUser appUser = JSONObject.parseObject(str, AppUser.class);
		return appUser;
	}
//	public static boolean respReturn(String token) {
//		Integer crmuserId = getCrmUserId(token);
//		if (crmuserId == null) {
//			return false;
//		}
//		return true;
//	}
	public static boolean respReturn(String token) {
		Map<String, Integer> userIdMap = getUserId(token);
		if (userIdMap.isEmpty()) {
			return false;
		}
		if (userIdMap.get("crmuserId") == null && userIdMap.get("appusersId")==null) {
			return false;
		}
		return true;
	}
	
	public static Map<String, Integer> getUserId(String token) {
		//从redis中获取userId并且判断是crm还是app端的用户
		Map<String, Integer> map = new HashMap<String,Integer>();
		String str = redisApi.get(token);
		JSONObject json = JSONObject.parseObject(str);
		if ("".equals(json)||json==null) {
			return map;
		}
		Integer crmuserId = json.getInteger("crmuserId");
		Integer appusersId = json.getInteger("appusersId");
		map.put("crmuserId", crmuserId);
		map.put("appusersId", appusersId);
		return map;
	}
	
}
