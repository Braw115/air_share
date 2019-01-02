package com.air.utils;

public class PayConfigUtil {
	//初始化
	public final static String APP_ID = "wx2c4b01b6e8eb4ca0"; //公众账号appid（改为自己实际的）wx9abcee36700ed2a0 
	public final static String APP_SECRET = "bc1d6407282eb9f083a0f1f2576c5748";
	public final static String MCH_ID = "1509565411"; //商户号（改为自己实际的）
	public final static String API_KEY = "shenzhenfulanli201808111618szfll"; //（改为自己实际的）key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
	
	//有关url
	public final static String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//预支付请求url
	public final static String NOTIFY_URL = "http://47.106.211.75/air-share-web/payNotifyUrl"; //微信支付回调接口，就是微信那边收到（改为自己实际的）
	//企业向个人账号付款的URL
	public final static String SEND_EED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	
	public final static String CREATE_IP = "47.106.211.75";//发起支付ip（改为自己实际的）
	
	public static final String ORDER_PAY_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery"; // 支付订单查询  

   /**
    * 关闭订单地址
    */
	public static final String  CLOSEORDERURL = "https://api.mch.weixin.qq.com/pay/closeorder";
	public static final String  BODY = "air";
	
	/**
    * 申请退款地址
    */
   public static final String  REFUNDURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

   /**
    * 查询退款地址
    */
   public static final String  REFUNDQUERYURL = "https://api.mch.weixin.qq.com/pay/refundquery";

   /**
    * 下载账单地址
    */
   public static final String  DOWNLOADBILLURL = "https://api.mch.weixin.qq.com/pay/downloadbill";

	
}
