package com.air.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class MsgUtils {
	
	private static final String SERVER_URL_SEND = "https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
	private static final String SERVER_URL_CHECK = "https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
	private static final String APP_KEY = "c19a577deb82281182d37bfc1abb2681";//网易云信分配的账号
	private static final String APP_SECRET = "5f9cfe74a83e";//网易云信分配的密钥
	private static final String NONCE = "123456";//随机数
	 //短信模板ID
    private static final String TEMPLATEID="4132189";
	  //验证码长度，范围4～10，默认为4
    private static final String CODELEN="6";
	
	public static String sendMsg(String phone ) throws Exception {
		 DefaultHttpClient httpClient = new DefaultHttpClient();
		 HttpPost httpPost = new HttpPost(SERVER_URL_SEND);
        
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

        // 设置请求的header
        httpPost.addHeader("AppKey", APP_KEY);
        httpPost.addHeader("Nonce", NONCE);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的的参数，requestBody参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        /*
         * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
         * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
         * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
         */
        nvps.add(new BasicNameValuePair("templateid", TEMPLATEID));
        nvps.add(new BasicNameValuePair("mobile", phone));
        nvps.add(new BasicNameValuePair("codeLen", CODELEN));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        /*
         * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
         * 2.具体的code有问题的可以参考官网的Code状态表
         */
//        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");
        //判断是否发送成功，发送成功返回true
        String code = JSON.parseObject(responseEntity).getString("code");
        if (code.equals("200")) {
           return "success";
        }
        															return "error";
	}
	public static String checkMsg(String phone ,String msgCode) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(SERVER_URL_CHECK);

        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);

        //设置请求的header
        post.addHeader("AppKey", APP_KEY);
        post.addHeader("Nonce", NONCE);
        post.addHeader("CurTime", curTime);
        post.addHeader("CheckSum", checkSum);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //设置请求参数
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mobile", phone));
        nameValuePairs.add(new BasicNameValuePair("code", msgCode));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

        //执行请求
        HttpResponse response = httpClient.execute(post);
        String responseEntity = EntityUtils.toString(response.getEntity(), "utf-8");

        //判断是否发送成功，发送成功返回true
        String code = JSON.parseObject(responseEntity).getString("code");
        if (code.equals("200")) {
           return "success";
        }
        return "error";
	}
	 public static boolean isMobile(String mobile){  
	        String regex = "(\\+\\d+)?1[3456789]\\d{9}$";    
	        return Pattern.matches(regex, mobile);  
	  } 

}
