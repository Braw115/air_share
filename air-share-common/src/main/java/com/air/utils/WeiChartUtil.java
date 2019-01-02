package com.air.utils;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class WeiChartUtil{

   /**
    * 返回状态码
    */
   public static final String ReturnCode = "return_code";

   /**
    * 返回信息
    */
   public static final String ReturnMsg = "return_msg";

   /**
    * 业务结果
    */
   public static final String ResultCode = "result_code";

   /**
    * 预支付交易会话标识
    */
   public static final String PrepayId = "prepay_id";
   /**
    * 得到微信预付单的返回ID
    * @param orderId  商户自己的订单号
    * @param totalFee  总金额  （分）
    * @return
    */
   public static Map<String, String> getPreyId(String orderId,
                                  String totalFee,String schoolLabel){
      Map<String, String> reqMap = new HashMap<String, String>();
      reqMap.put("appid", PayConfigUtil.APP_ID);
      reqMap.put("mch_id", PayConfigUtil.MCH_ID);
      reqMap.put("nonce_str", getRandomString());

      reqMap.put("body", "【"+schoolLabel+"】"+ PayConfigUtil.BODY);
      //reqMap.put("detail", WeiChartConfig.subject); //非必填
      //reqMap.put("attach", "附加数据"); //非必填
      reqMap.put("out_trade_no", orderId); //商户系统内部的订单号,
      reqMap.put("total_fee", totalFee); //订单总金额，单位为分
      reqMap.put("spbill_create_ip", getHostIp()); //用户端实际ip
      // reqMap.put("time_start", "172.16.40.18"); //交易起始时间 非必填
      // reqMap.put("time_expire", "172.16.40.18"); //交易结束时间  非必填
      // reqMap.put("goods_tag", "172.16.40.18"); //商品标记 非必填
      reqMap.put("notify_url", PayConfigUtil.NOTIFY_URL); //通知地址
      reqMap.put("trade_type", "APP"); //交易类型
      //reqMap.put("limit_pay", "no_credit"); //指定支付方式,no_credit 指定不能使用信用卡支  非必填
      reqMap.put("sign", getSign(reqMap));

      String reqStr = creatXml(reqMap);
      String retStr = HttpClientUtil.postHtpps(PayConfigUtil.UFDODER_URL, reqStr);
      return getInfoByXml(retStr);
   }

   /**
    * 关闭订单
    * @param orderId  商户自己的订单号
    * @return
    */
   public static Map<String, String> closeOrder(String orderId){
      Map<String, String> reqMap = new HashMap<String, String>();
      reqMap.put("appid", PayConfigUtil.APP_ID);
      reqMap.put("mch_id", PayConfigUtil.MCH_ID);
      reqMap.put("nonce_str", getRandomString());
      reqMap.put("out_trade_no", orderId); //商户系统内部的订单号,
      reqMap.put("sign", getSign(reqMap));

      String reqStr = creatXml(reqMap);
      String retStr = HttpClientUtil.postHtpps(PayConfigUtil.CLOSEORDERURL, reqStr);
      return getInfoByXml(retStr);
   }


   /**
    * 查询订单
    * @param orderId 商户自己的订单号
    * @return
    */
   public static String getOrder(String orderId){
      Map<String, String> reqMap = new HashMap<String, String>();
      reqMap.put("appid", PayConfigUtil.APP_ID);
      reqMap.put("mch_id", PayConfigUtil.MCH_ID);
      reqMap.put("nonce_str", getRandomString());
      reqMap.put("out_trade_no", orderId); //商户系统内部的订单号,
      reqMap.put("sign", getSign(reqMap));

      String reqStr = creatXml(reqMap);
      String retStr = HttpClientUtil.postHtpps(PayConfigUtil.ORDER_PAY_QUERY, reqStr);
      return retStr;
   }


//   /**
//    * 退款
//    * @param orderId  商户订单号
//    * @param refundId  退款单号
//    * @param totralFee 总金额（分）
//    * @param refundFee 退款金额（分）
//    * @param opUserId 操作员ID
//    * @return
//    */
//   public static Map<String, String> refundWei(String orderId,String refundId,String totralFee,String refundFee,String opUserId){
//      Map<String, String> reqMap = new HashMap<String, String>();
//      reqMap.put("appid", PayConfigUtil.APP_ID);
//      reqMap.put("mch_id", PayConfigUtil.MCH_ID);
//      reqMap.put("nonce_str", getRandomString());
//      reqMap.put("out_trade_no", orderId); //商户系统内部的订单号,
//      reqMap.put("out_refund_no", refundId); //商户退款单号
//      reqMap.put("total_fee", totralFee); //总金额
//      reqMap.put("refund_fee", refundFee); //退款金额
//      reqMap.put("op_user_id", opUserId); //操作员
//      reqMap.put("sign", getSign(reqMap));
//
//      String reqStr = creatXml(reqMap);
//      String retStr = "";
//      try{
//         retStr = HttpClientUtil.postHttplientNeedSSL(PayConfigUtil.REFUNDURL, reqStr, PayConfigUtil.REFUND_FILE_PATH, PayConfigUtil.MCH_ID);
//      }catch(Exception e){
//         e.printStackTrace();
//         return null;
//      }
//      return getInfoByXml(retStr);
//   }


   /**
    * 退款查询
    * @param refundId  退款单号
    * @return
    */
   public static Map<String, String> getRefundWeiInfo(String refundId){
      Map<String, String> reqMap = new HashMap<String, String>();
      reqMap.put("appid", PayConfigUtil.APP_ID);
      reqMap.put("mch_id", PayConfigUtil.MCH_ID);
      reqMap.put("nonce_str", getRandomString());
      reqMap.put("out_refund_no", refundId); //商户退款单号
      reqMap.put("sign", getSign(reqMap));

      String reqStr = creatXml(reqMap);
      String retStr = HttpClientUtil.postHtpps(PayConfigUtil.REFUNDQUERYURL, reqStr);
      return getInfoByXml(retStr);
   }

   /**这个方法 可以自己写，以前我使用的是我公司封装的类，后来很多人找我要JAR包，所以我改成了这样，方便部分人直接使用代码，我自己未测试，不过应该问题不大，欢迎使用有问题的找我。
    * 传入map  生成头为XML的xml字符串，例：<xml><key>123</key></xml>
    * @param reqMap
    * @return
    */
   public static String creatXml(Map<String, String> reqMap){
      Set<String> set = reqMap.keySet();
      StringBuffer b = new StringBuffer();
      b.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      b.append("<xml>");
      for(String key : set){
         b.append("<"+key+">").append(reqMap.get(key)).append("</"+key+">");
      }
      b.append("</xml>");
      return b.toString();
   }

   /**
    * 得到加密值
    * @param map
    * @return
    */
   public static String getSign(Map<String, String> map){
      String[] keys = map.keySet().toArray(new String[0]);
      Arrays.sort(keys);
      StringBuffer reqStr = new StringBuffer();
      for(String key : keys){
         String v = map.get(key);
         if(v != null && !v.equals("")){
            reqStr.append(key).append("=").append(v).append("&");
         }
      }
      reqStr.append("key").append("=").append(PayConfigUtil.API_KEY);

      return MD5Util.MD5Encode(reqStr.toString(),"utf-8").toUpperCase();
   }

   /**
    * 得到10 位的时间戳
    * 如果在JAVA上转换为时间要在后面补上三个0 
    * @return
    */
   public static String getTenTimes(){
      String t = new Date().getTime()+"";
      t = t.substring(0, t.length()-3);
      return t;
   }

   /**
    * 得到随机字符串
    * @param length
    * @return
    */
   public static String getRandomString(){
      int length = 32;
      String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      Random random = new Random();
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < length; ++i){
         int number = random.nextInt(62);//[0,62)  
         sb.append(str.charAt(number));
      }
      return sb.toString();
   }

   /**
    * 得到本地机器的IP
    * @return
    */
   private static String getHostIp(){
      String ip = "";
      try{
         ip = InetAddress.getLocalHost().getHostAddress();
      }catch(UnknownHostException e){
         e.printStackTrace();
      }
      return ip;
   }

   public static Map<String, String> getInfoByXml(String xmlStr){
      try{
         Map<String, String> m = new HashMap<String, String>();
         Document d = DocumentHelper.parseText(xmlStr);
         Element root = d.getRootElement();
         for ( Iterator<?> i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String name = element.getName();
            if(!element.isTextOnly()){
               //不是字符串 跳过。确定了微信放回的xml只有根目录
               continue;
            }else{
               m.put(name, element.getTextTrim());
            }
         }
         //对返回结果做校验.去除sign 字段再去加密
         String retSign = m.get("sign");
         m.remove("sign");
         String rightSing = getSign(m);
         if(rightSing.equals(retSign)){
        	m.put("sign", retSign);
            return m;
         }
      }catch(DocumentException e){
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
}

   /**
    * 将金额转换成分
    * @param fee 元格式的
    * @return 分
    */
   public static String changeToFen(Double fee){
      String priceStr = "";
      if(fee != null){
          int p = (int)(fee * 100); //价格变为分
          priceStr = Integer.toString(p);
      }
      return priceStr;
   }
	
	public static Map<String, String> respData(Map<String, String> preyId) {
		Map<String, String> reqMap = new HashMap<String, String>();
		
		reqMap.put("appid", PayConfigUtil.APP_ID);
	    reqMap.put("partnerid", PayConfigUtil.MCH_ID);
	    reqMap.put("noncestr", getRandomString());
	    reqMap.put("timestamp", System.currentTimeMillis()/1000+"");
	    reqMap.put("package", "Sign=WXPay");
	    reqMap.put("prepayid", preyId.get("prepay_id"));
	    reqMap.put("sign", getSign(reqMap));
//	    reqMap.put("sign", preyId.get("sign"));
	    reqMap.remove("appid");
		return reqMap;
	}

}