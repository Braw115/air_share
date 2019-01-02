package com.air.pay.service;

/**
 * 
 * @author xgchen
 *
 */
public interface IUserService {
	
	String weixinPay(String userId, String productId) throws Exception;
}
