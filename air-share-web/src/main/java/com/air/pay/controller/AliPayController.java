package com.air.pay.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.pojo.entity.Order;
import com.air.pojo.vo.Alipay;
import com.air.user.service.OrderService;
import com.air.utils.AlipayNotify;
import com.air.utils.AlipayUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;

@Controller
public class AliPayController {  
	
	@Resource
	private OrderService orderService;
  
    
    /**  
     * 支付宝支付  
     *  
     * @param orderNo  
     * @param userId  
     * @return  
     * @throws Exception 
     */  
    @ResponseBody
    @RequestMapping(value="/pay",method=RequestMethod.POST)  
    public Dto pay(@RequestBody  HashMap<String, String> map
                           ) throws Exception {  
    	String orderNo = map.get("orderNo");
        //1  实例化客户端  
    	AlipayClient alipayClient = AlipayUtil.getAlipayClient();  
        //2  实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay  
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();  
        //3  SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。  
//        //1 获取订单信息  
        Order order = orderService.queryOrderByNo(orderNo);
        //2 验证订单信息  
        if (order == null || "yes".equals(order.getPaystatus())) {  
            //2.1 订单不存在  
            throw new Exception("订单不存在或已支付");  
        }  
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();  
    
        model.setSubject("SzfllAir");
        model.setOutTradeNo(orderNo);  
        model.setTimeoutExpress("30" + "m");  
        //model.setTotalAmount(order.getRealfee().toString());
        model.setTotalAmount("0.01".toString());
        model.setProductCode("QUICK_MSECURITY_PAY");  
        
        request.setBizModel(model);  
        request.setNotifyUrl(Alipay.NOTIFY_URL);  
        try {  
            //这里和普通的接口调用不同，使用的是sdkExecute  
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);  
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。  
            Map<String, String> payMap = new HashMap<>();  
            payMap.put("orderStr", response.getBody());  
            return DtoUtil.returnSuccess("ok",payMap);  
        } catch (AlipayApiException e) {  
            e.printStackTrace();  
        }  
        return DtoUtil.returnFail("生成字符串失败", ErrorCode.RESP_ERROR);  
    }  
    
    /**
     * 支付宝回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = { "/getPayNotify" }, method = { RequestMethod.POST })
	@ResponseBody
	public String getPayNotify(HttpServletRequest request) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();

		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		boolean flag = AlipaySignature.rsaCheckV1(params, Alipay.ALIPAY_PUBLIC_KEY, Alipay.CHARSET,"RSA2");
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
		// 交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		// 异步通知ID
		String notify_id = request.getParameter("notify_id");
		// sign
		String sign = request.getParameter("sign");
		if (notify_id != "" && notify_id != null) {
			if (AlipayNotify.verifyResponse(notify_id).equals("true"))// 判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
			{
				if(flag)
				{
					if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
						Order order = orderService.queryOrderByNo(out_trade_no);
						order.setPaymethod("支付宝");
						orderService.modifyOrderInfo(order);
					}
					return "success";
				} else {// 验证签名失败
					return "sign fail";
				}
			} else {// 验证是否来自支付宝的通知失败
				return "response fail";
			}
		} else {
			return "no notify message";
		}

	}
    
}  
 