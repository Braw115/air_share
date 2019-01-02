package com.air.user.service;

import com.air.pojo.vo.ZFBUserVo;
import com.alipay.api.AlipayApiException;

public interface AlipayService {
	
	public void afterPropertiesSet() throws Exception;

	String getAccessToken(String authCode);
	
	String getAppAuthToken(String appAuthCode) throws AlipayApiException;

	ZFBUserVo getUserInfoByToken(String accessToken);
	
	/**
	 * 获取支付宝用户信息授权
	 * @param url
	 * @return
	 */
	String visitUrl(String url);
	
	/**
	 * 获取biz_no
	 * @param cert_name
	 * @param cert_no
	 * @return
	 * @throws AlipayApiException
	 */
	String zhimaCustomerCertificationCertify(String cert_name, String cert_no) throws AlipayApiException;
	
	/**
	 * 生成认证请求URL
	 * @param bizNo
	 * @return
	 * @throws AlipayApiException
	 */
	String zhimaCustomerCertificationCertify(String bizNo) throws AlipayApiException;
	
	/**
	 * 查询认证结果
	 * @param biz_no
	 * @return
	 * @throws AlipayApiException
	 */
	String ZhimaCustomerCertificationQuery(String biz_no) throws AlipayApiException;

	/**
	 *  第三方应用授权拼接url
	 * @param aPP_AUTH_URL
	 * @return
	 */
	String AppUrl(String aPP_AUTH_URL); 
}
