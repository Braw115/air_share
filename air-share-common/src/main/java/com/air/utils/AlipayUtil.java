package com.air.utils;
import com.air.pojo.vo.Alipay;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;


public class AlipayUtil {

    public static final String ALIPAY_APPID =Alipay.APP_ID; // appid

    public static String APP_PRIVATE_KEY = Alipay.APP_PRIVATE_KEY; // app支付私钥

    public static String ALIPAY_PUBLIC_KEY = Alipay.ALIPAY_PUBLIC_KEY; // 支付宝公钥
    
    public static String SIGN = Alipay.SIGN_TYPE; // 签名方式


    // 统一收单交易创建接口
    private static AlipayClient alipayClient = null;

    public static AlipayClient getAlipayClient() {
        if (alipayClient == null) {
            synchronized (AlipayUtil.class) {
                if (null == alipayClient) {
                    alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, ALIPAY_APPID,
                            APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
                            ALIPAY_PUBLIC_KEY,SIGN);
                }
            }
        }
        return alipayClient;
    }
}