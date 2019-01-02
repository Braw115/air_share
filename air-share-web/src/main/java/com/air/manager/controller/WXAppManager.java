package com.air.manager.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.air.pojo.vo.AccessTokenVo;
import com.air.pojo.vo.WebChatVo;
import com.air.utils.PayConfigUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;



@Component
public class WXAppManager extends WXManagerBase {
	private static final Log log = LogFactory.getLog(WXAppManager.class);

	public static final String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=$APPID$&secret=$APPSECRET$&code=$CODE$&grant_type=authorization_code";
	public static final String OAUTH_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=$ACCESSTOKEN$&openid=$OPENID$";
	public static final String APPID =PayConfigUtil.APP_ID;
	public static final String APPSECRET =PayConfigUtil.APP_SECRET;
	
	/**
	 * 通过oauth认证的code获取access_token
	 * 
	 * @return
	 */
	public AccessTokenVo getOauthAccessTokenByCode(String code) {
		String json = getWechatInfoUseGet(
				OAUTH_ACCESS_TOKEN_URL.replace("$CODE$", code).replace("$APPID$", APPID)
						.replace("$APPSECRET$", APPSECRET));
		JSONObject obj = (JSONObject) JSON.parse(json);
		if (StringUtils.isNoneBlank(obj.getString("errcode"))) {
			log.error("can not get access_token from weixin errcode:" + obj.getString("errcode"));
			return null;
		}
		AccessTokenVo result = JSON.toJavaObject(obj, AccessTokenVo.class);
		return result;
	}

	public WebChatVo getOauthUserInfo(String accesstoken, String openid) {
		String json = getWechatInfoUseGet(
				OAUTH_USER_INFO_URL.replace("$ACCESSTOKEN$", accesstoken).replace("$OPENID$", openid));
		JSONObject obj = (JSONObject) JSON.parse(json);
		if (StringUtils.isNoneBlank(obj.getString("errcode"))) {
			log.error("can not get access_token from weixin errcode:" + obj.getString("errcode"));
			return null;
		}
		WebChatVo result = JSON.toJavaObject(obj, WebChatVo.class);
		return result;
	}
}
