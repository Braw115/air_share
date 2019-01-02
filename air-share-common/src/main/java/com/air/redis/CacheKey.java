package com.air.redis;

/**
 * @author xinyizhang
 * @date 2015年5月25日 上午9:39:42
 */
public interface CacheKey {
	String OPENID_USERINFO = "wx:openid:%s:userinfo";
	String MP_ACCESS_TOKEN = "wx:mp_access_token";
	
	String WX_JSAPI_TICKET = "wx:jsapi_ticket";
	
	
	String PRODUCT_QUERYBYQUALITY = "product:querybyquality";
	String PRODUCT_QUERYBYQUALITY_v2 = "product:querybyquality_v2";
	String PRODUCT_SQL_CURSOR_LIMIT = "product:%s_%s_%s";
	String PRODUCT_STEADY_PROFIT = "product:steadyprofit";
	String PRODUCT_STEADY_LIST = "product:steadylist:%s_%s_%s_%s_%s";
	
	String NEWSINFO_SQL_CURSOR_LIMIT = "newsinfo:%s_%s_%s";
}
