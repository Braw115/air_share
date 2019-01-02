package com.air.user.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.air.pojo.vo.Alipay;
import com.air.pojo.vo.ZFBUserVo;
import com.air.user.service.AlipayService;
import com.air.utils.AlipayUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.ZhimaCustomerCertificationCertifyModel;
import com.alipay.api.domain.ZhimaCustomerCertificationInitializeModel;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.alipay.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.alipay.api.request.ZhimaCustomerCertificationQueryRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.alipay.api.response.ZhimaCustomerCertificationCertifyResponse;
import com.alipay.api.response.ZhimaCustomerCertificationInitializeResponse;
import com.alipay.api.response.ZhimaCustomerCertificationQueryResponse;

@Service
public class AlipayServiceImpl implements AlipayService{

	private static final Logger LOGGER = LoggerFactory.getLogger(AlipayServiceImpl.class);
	
	 /**Alipay客户端*/
    private AlipayClient alipayClient;

	@Override
	public void afterPropertiesSet() throws Exception {
		alipayClient = AlipayUtil.getAlipayClient();
	}
	
	@Override
	public String getAccessToken(String authCode) {
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID, Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);

		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setGrantType("authorization_code");
        request.setCode(authCode);
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
//            return oauthTokenResponse.getBody();
            return oauthTokenResponse.getAccessToken();
        } catch (Exception e) {

            LOGGER.error("使用authCode获取信息失败！", e);
            return null;
        }
	}

	  /**
     * 根据access_token获取用户信息
     * @param token
     * @return
     */
	@Override
	public ZFBUserVo getUserInfoByToken(String accessToken) {
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID, Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);

		AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest ();
        try {
            AlipayUserInfoShareResponse response =  alipayClient.execute(request, accessToken);
            if (response.isSuccess()) {
                //打印响应信息
//                System.out.println(ReflectionToStringBuilder.toString(response));
                //封装支付宝对象信息
//                AlipayUserVo alipayUser = new AlipayUserVo();
//                alipayUser.setAddress(response.getAddress());
//                alipayUser.setCertNo(response.getCertNo());
//                alipayUser.setCity(response.getCity());
//                alipayUser.setCollegeName(response.getCollegeName());
//                alipayUser.setDegree(response.getDegree());
//                alipayUser.setMobile(response.getMobile());
//                alipayUser.setPhone(response.getPhone());
//                alipayUser.setProvince(response.getProvince());
//                alipayUser.setUserName(response.getUserName());
//                alipayUser.setNickName(response.getNickName());
            	ZFBUserVo zfbUserVo = new ZFBUserVo();
            	zfbUserVo.setUser_id(response.getUserId());
            	zfbUserVo.setImgurl(response.getAvatar());//用户头像地址
            	zfbUserVo.setGender(response.getGender());//性别
            	zfbUserVo.setNickname(response.getNickName());//昵称
                return zfbUserVo;
            }
            LOGGER.error("根据 access_token获取用户信息失败!");
            return null;

        } catch (Exception e) {
            LOGGER.error("根据 access_token获取用户信息抛出异常！", e);
            return null;
        }
	}

	@Override
	public String getAppAuthToken(String appAuthCode) throws AlipayApiException {
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID, Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);

		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizContent("{" +
				"    \"grant_type\":\"authorization_code\"," +
				"    \"code\":\"2780e9c214744c8f998314d64440aX75\"" +
				"  }");
		AlipayOpenAuthTokenAppResponse response = null;
		try {
			response = alipayClient.execute(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		
		return null==response?null:response.getAppAuthToken();
	}

	@Override
	public String visitUrl(String url) {
		String encodeUrl="";
		try {
			encodeUrl = URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String scope = "auth_user,auth_base";
		String visitUrl="https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id="+Alipay.APP_ID+"&scope="+scope+"&redirect_uri="+encodeUrl;
		return visitUrl;
	}

	@Override
	public String zhimaCustomerCertificationCertify(String cert_name, String cert_no) throws AlipayApiException {
//		AlipayClient alipayClient = AlipayUtil.getAlipayClient();
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID, Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);
		ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
		ZhimaCustomerCertificationInitializeModel model = new ZhimaCustomerCertificationInitializeModel();
		// 商户请求的唯一标志
		model.setTransactionId(System.currentTimeMillis()+"");
		// 芝麻认证产品码
		model.setProductCode("w1010100000000002978");
		// 认证场景码,FACE:人脸认证
		model.setBizCode("FACE");
		// 值为一个json串,必须包含身份类型identity_type,不同的身份类型需要的身份信息不同，详细介绍，请参考文档
		model.setIdentityParam(
				"{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\""+cert_name+"\",\"cert_no\":\""+cert_no+"\"}");
		request.setBizModel(model);
		ZhimaCustomerCertificationInitializeResponse response = alipayClient.execute(request);
		System.out.println(response.getBody());
		System.out.println("bizNo:" + response.getBizNo());

		if (response.isSuccess()) {
			System.out.println("调用成功");
			return response.getBizNo();
		} else {
			System.out.println("调用失败");
			return null;
		}
	}

	public String zhimaCustomerCertificationCertify(String bizNo) throws AlipayApiException {
		// AlipayClient alipayClient = AlipayUtil.getAlipayClient();
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID,
				Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);
		ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
		ZhimaCustomerCertificationCertifyModel model = new ZhimaCustomerCertificationCertifyModel();
		model.setBizNo(bizNo);
		request.setBizModel(model);
		// 生成认证请求URL,必要要传回调地址return_url，
		// 回调支持在支付宝APP打开(示例:alipays://www.taobao.com)、在浏览器打开（示例:https://www.taobao.com）、在商户app打开(使用商户schema协议)。
		request.setReturnUrl("friendly://zhima");
		ZhimaCustomerCertificationCertifyResponse response = alipayClient.pageExecute(request, "GET");
		System.out.println(response.getBody());
		if (response.isSuccess()) {
			System.out.println("调用成功");
			return response.getBody();
		} else {
			System.out.println("调用失败");
			return null;
		}
	}

	@Override
	public String ZhimaCustomerCertificationQuery(String biz_no) throws AlipayApiException {
		// AlipayClient alipayClient = AlipayUtil.getAlipayClient();
		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID,
				Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);
		ZhimaCustomerCertificationQueryRequest request = new ZhimaCustomerCertificationQueryRequest();
		request.setBizContent("{\"biz_no\":\""+biz_no+"\"}");
		ZhimaCustomerCertificationQueryResponse response = alipayClient.execute(request);
		System.out.println(response.getBody());
//		ZM201807273000000767600528711992 ZM201807273000000686800530252887
		if (response.isSuccess()) {
			System.out.println("调用成功");
			return response.getPassed();
		} else {
			System.out.println("调用失败");
			return null;
		}
	}

	/**
	 * 拼接第三方授权url
	 */
	@Override
	public String AppUrl(String appUrl) {
		String encodeUrl="";
		try {
			encodeUrl = URLEncoder.encode(appUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String visitUrl="https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id="+Alipay.APP_ID+"&redirect_uri="+encodeUrl;
		return visitUrl;
	}

}
