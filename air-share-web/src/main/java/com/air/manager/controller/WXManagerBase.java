package com.air.manager.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WXManagerBase {
	private static final Log log = LogFactory.getLog(WXManagerBase.class);
	/* 通过oauth认证的code获取access_token */
	public static final String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=$APPID$&secret=$APPSECRET$&code=$CODE$&grant_type=authorization_code";
	/* 通过oauth认证获取的access_token刷新access_token */
	public static final String OAUTH_REFRESH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=$APPID$&grant_type=refresh_token&refresh_token=$REFRESH_TOKEN$";
	/* 通过oauth认证获取的access_token获取用户信息 */
	public static final String OAUTH_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=$ACCESS_TOKEN$&openid=$OPENID$";
	/* 通过oauth认证获取的access_token获取帐号的关注者列表 */
	public static final String OAUTH_GETSUBSCRIBEUSER_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=$ACCESS_TOKEN$&next_openid=$NEXT_OPENID$";

	/* 消息推送 */
	public static final String PUSH_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${access_token}";

	/** 页面授权 */
	public static final String OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=$APPID$&redirect_uri=$REDIRECT_URI$&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

	static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}

	};

	private static void trustAllHttpsCertificates() {
		try {
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
			javax.net.ssl.TrustManager tm = new miTM();
			trustAllCerts[0] = tm;
			javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	
	public static String getWechatInfoUsePost(String uri, String json) {
		HttpURLConnection connection = null;
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			URL url = new URL(uri);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			out.write(json);
			out.flush();
			br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "ISO-8859-1"));
			String lines;
			StringBuffer sb = new StringBuffer();
			while ((lines = br.readLine()) != null) {
				lines = new String(lines.getBytes("ISO-8859-1"), "UTF-8");
				sb.append(lines);
			}
			System.out.println(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			closePrintWriter(out);
			closeConnection(connection);
			closeBufferedReader(br);
		}
		return "";
	}

	/**
	 * 传入和微信通信的URL，去微信取JSON结果集
	 * 
	 * @param url
	 * @return
	 */
	public static String getWechatInfoUseGet(String url) {
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		HttpURLConnection conn = null;
		InputStream in = null;
		BufferedReader br = null;
		try {
			URL urlObj = new URL(url);
			conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("GET");
			in = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
			String lines;
			StringBuffer sb = new StringBuffer();
			while ((lines = br.readLine()) != null) {
				lines = new String(lines.getBytes("ISO-8859-1"), "UTF-8");
				sb.append(lines);
			}
			log.info("WeixinUtil.getWechatInfo:" + sb.toString());
			return sb.toString();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			closeConnection(conn);
			closeInputStream(in);
			closeBufferedReader(br);
		}
		return "";
	}

	private static void closeBufferedReader(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void closeConnection(HttpURLConnection connection) {
		if (connection != null) {
			connection.disconnect();
		}
	}

	private static void closePrintWriter(PrintWriter out) {
		if (out != null) {
			out.close();
		}
	}

	private static void closeInputStream(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
