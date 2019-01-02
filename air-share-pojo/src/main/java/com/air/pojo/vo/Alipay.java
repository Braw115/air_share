package com.air.pojo.vo;

public class Alipay {
	
	 // 1.商户appid
	public static String APP_ID ="2018062060380630"; 
//	public static String APP_ID = "2016091400506758";//沙箱 2016091400506758
	 //2.合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
    public static String PID = "2088131488908598";
	
	public static String FORMAT="json";
	
	//3.商户私钥，您的PKCS8格式RSA2私钥
    public static String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDVtzfzmFhAOV04zF1dcD/FO89r1LRt/+DFAxqNBZWAfPD8hq+PQOt3AN+RBXH166Sty5c2aCrAlWXbB7bUvU+5dGPyMuGRezCbHT83v4US07lZhGBBh7Xq3gI87+SMFYjeDiQU5tg+44BchLhAGU9Bsl7mD4fF9p1Lfx13XghHsXT2/JeFnX1Ge2OnQNxlUS+fbT+u+2uKzH8sBQK48wvm02sxv4SN88ctuJ4vWY2ayZFpCgqtvrQLMKLOL5Vse/ycrjAESZ/ap/rP+PesW5b7IMJjLquTF+N8EfAfnOGhW+TXXuwi9q1/M3Zi+XtI0j8l5E4Ui/3mjyIZo7esnGXAgMBAAECggEAWMPrMaOlMs9NRSuLsMwt7Pg0HSeSPMUzfl6daWXctrJJEuRc6LgJ9YYeIkdXxSHjDauaPQykxw8j09wyGp89uw3ODXRzAUPfGSP+CS2Y4RRotSe/qsj9LVvpG3vJvL7IaN6GtBydhfVd62IymjkPMyiUIeEqEw+nCM1UtEBDQXY3Ik4EUuhsG0fEvHX5RXdX/MnlUWA0ANHlz4KFT1dDw13EA7boitXhISYO9WpkXs1qzgO8e7uhEUbcNTDfwG9OwK/1BFWcQ26YNIss+DjNl19RhwACgUUIr/Goc3uUDTdHv6pZyBzeDu9ugr4gZUEydD5DmznlPogVaxUYu/L7QQKBgQDHDbdDo8G/c4x5WoWyWjAxoRZbhHooVqTI/Iu8sV//6CFmGsZXSDEMr87Es1904pfhLvbGhDkWOieAk06JfUnhh3HJz+5y1K0yDNPnFc0uZqSrTqLJMgq1vA4bCFp+DoKuKF9wDVL6xWH2j9WMLliO+9a07yjQBCQ8zwlAHcyhYQKBgQCo6ePLyeE3mmCHPBSOPO4tjmLG9HiojxMCVtpQ4JDNOq1IqpnzXaYfiT/OITuxhjbAkzWPSjZnr/dMA2MYR8H5yNU3qwM3ZEfQgpSNWZTvqOn/lca1FgE7SIhocKgzchWsNO8u64FLGLaNUYLeumJkqcg7ACZNliJiZlv8MjLd9wKBgQC0OoQD12LXAT1gp4jEawqV9R/0LygYHBNJ8/hmGkgswjZzZ6zMXI4kyY4y5aJ5h60y8733EsNJj9Ayh64lqO5bKLLrorxo9o328kxvQHsuFzvj6+hSuV+fGy2+ZjrhRiwmmmGHaFkIu+0uPR+CtHf0xqup2Z2fcxCIioD6r8CXIQKBgEvBGbi7wZX+VmxQNafVC7zCruormjF+eTgDW8YJ8OcZ+b0tRf4vceGS3FrF7M+hMc0MOGkbD2tRL8eXfUnWZNhxia59s+OTxeFtrtH16yXQy6Ekuwlb5RGBZivw2n64G6Av3VVdqY7UdorKk7bwQFCFFJfhaUxCmQkofviMSuPhAoGATwtwvsEqbaiLKwDvAnh7f/bQGZ0QM8lnmnqgeBWxDbT7TGVpTqbrC6IsLwnQ6fVslqdBT8y5D5EkwoH/Kb2j7CJ4EYcA/lV4++WvyBTAFUUUgZ9bCUtlZCu9T7KPKVKOYUaKLLQGotwsX2YRTIp/x6Qc42B2r88csAzJ/MShJLE=";
	
	// 4.支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvlym8n+sT1RrO14fYnXHrzoIBB0IewPPzRSikn1nCY4vMaoNt2czC+Y8SoySSPxKXvLoHC4o2wUypCZSEQ5mUzpcI8tPw5vuPsRS7Q5LMIhQbpLCqYlZdvCBHYiutpxBeC3iFl7rK0UzSsgEI/IOaeP+sDLC1Y2FsWxDLZ7J83xKcRdTqlOCY+5qDqpcmH9mp3W7cEsxhQtG1tYih9QxghCm1TT71LgzxrTdr+SMpCB+g9OPTYkLeg8ypN+MFIuXtkkkjeG+c1sZn7afo5L0i+Ev6rWkFOCotAtK2K+1nBozoyWUq3+UioTbTlv8SKr4irUHOyzhZnwtED4wmAdhVwIDAQAB";
	
	//5. 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //"http://watermelons.cn/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp"
	public static String NOTIFY_URL = "https://47.106.211.75/air-share-web/getPayNotify";

	//	public static String notify_url = "http://192.168.0.159/air-share-web/Alipay/payNotify" ;
	public static String notify_url = "http://watermelons.cn/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";
	
	public static String AUTH_URL = "http://192.168.0.159/air-share-web/AliApp/loginByZFBcode" ;
	public static String APP_AUTH_URL = "http://192.168.0.159/air-share-web/AliApp/getAppAuthToken" ;

	//6. 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String RETURN_URL = "http://watermelons.cn/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

	//7.签名方式
	public static String SIGN_TYPE = "RSA2";
	
	//8.字符编码格式
	public static String CHARSET = "utf-8";
	
	//9.支付宝网关
	public static final String ALIPAY_BORDER_PROD = "https://openapi.alipay.com/gateway.do";//正式环境

	/**支付宝网关*/
	public static final String ALIPAY_BORDER_DEV = "https://openapi.alipaydev.com/gateway.do";//沙箱环境

	//10.收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = "2088131488908598" ;
	
	//11.调试用，创建TXT日志文件夹路径
	public static String LOG_PATH = "D:\\";
	
	//12.支付类型 ，无需修改
	public static String payment_type = "1";
	
	//13.芝麻开放平台地址
	public static String ZHIMA_URL = "https://zmopenapi.zmxy.com.cn/openapi.do";
	
	// 14.芝麻公钥
	public static String ZHIMA_PUBLIC_KEY = "";

	
}
