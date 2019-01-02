package com.air.pay.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.pojo.vo.Alipay;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;


@Controller
@RequestMapping("/Alipay")
public class AliconsumeCotroller {
	
	
	AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD,Alipay.APP_ID,Alipay.APP_PRIVATE_KEY,Alipay.FORMAT,Alipay.CHARSET,Alipay.ALIPAY_PUBLIC_KEY,Alipay.SIGN_TYPE);
	    //支付宝支付接口
		@RequestMapping(value="consume",method=RequestMethod.GET)      
		public void consume(String out_trade_no,String total_amount,String subject,String body,HttpServletResponse httpResponse) {
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(Alipay.RETURN_URL);
			alipayRequest.setNotifyUrl(Alipay.NOTIFY_URL);
			alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
					+ "\"total_amount\":\""+ total_amount +"\"," 
					+ "\"subject\":\""+ subject +"\"," 
					+ "\"body\":\""+ body +"\"," 
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			String form="";
		    try {
		        form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		        httpResponse.setContentType("text/html;charset=" +Alipay.CHARSET);
			    httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			    httpResponse.getWriter().flush();
			    httpResponse.getWriter().close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    
	    }
		//支付宝支付查询接口
	    @RequestMapping(value="consumeQuray",method=RequestMethod.POST)      
		public void consumeQuray(String out_trade_no,String trade_no) {
	    	AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
	    	request.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","+"\"trade_no\":\""+ trade_no +"\"}");
	    	AlipayTradeQueryResponse response;
			try {
				response = alipayClient.execute(request);
				if(response.isSuccess()){
			    } else {
			    }
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}	    
	    }
	    //支付宝退款接口
	    @RequestMapping(value="refund",method=RequestMethod.POST)      
		public void refund(String out_trade_no,String trade_no,String refund_amount,String refund_reason,String out_request_no,HttpServletResponse httpResponse) {
	    	AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
	    	alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
	    			+ "\"trade_no\":\""+ trade_no +"\"," 
	    			+ "\"refund_amount\":\""+ refund_amount +"\"," 
	    			+ "\"refund_reason\":\""+ refund_reason +"\"," 
	    			+ "\"out_request_no\":\""+ out_request_no +"\"}");
			try {
				String result = alipayClient.execute(alipayRequest).getBody();
				httpResponse.setContentType("text/html;charset=" +Alipay.CHARSET);
			    httpResponse.getWriter().write(result);//直接将完整的表单html输出到页面
			    httpResponse.getWriter().flush();
			    httpResponse.getWriter().close();
			} catch (Exception e) {
				e.printStackTrace();
			}		    
	    }
	  //支付宝退款查询接口
	    @RequestMapping(value="refundQuray",method=RequestMethod.POST)      
		public void refundQuray(String out_trade_no,String trade_no,String out_request_no) {
	    	AlipayTradeFastpayRefundQueryRequest alipayRequest = new AlipayTradeFastpayRefundQueryRequest();
	    	alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
	    			+"\"trade_no\":\""+ trade_no +"\","
	    			+"\"out_request_no\":\""+ out_request_no +"\"}");
			try {
				String result = alipayClient.execute(alipayRequest).getBody();
				System.out.println(result);
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}	    
	    }
		
	  //单笔转账到支付宝账户
	  		@RequestMapping(value="withdrawal",method=RequestMethod.POST)      
	  		public void withdrawal(String out_biz_no,String payee_account,String amount,String remark) {
	  			AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
	  			request.setBizContent("{" +
	  			"    \"out_biz_no\":\""+out_biz_no+"\"," +
	  			"    \"payee_type\":\"ALIPAY_LOGONID\"," +
	  			"    \"payee_account\":\""+payee_account+"\"," +
	  			"    \"amount\":\""+amount+"\"," +
	  			"    \"remark\":\""+remark+"\"," +
	  			"  }");
	  			AlipayFundTransToaccountTransferResponse response;
				try {
					response = alipayClient.execute(request);
					if(response.isSuccess()){
						
			  		} else {
			  		System.out.println("调用失败");
			  		}		  		    
				} catch (AlipayApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  			
	  	    }
//	  		 //单笔转账订单查询
//	  		@RequestMapping(value="selectWithdrawal",method=RequestMethod.POST)      
//	  		public void selectWithdrawal(String out_biz_no,Withdrawal withdrawal) {
//	  			AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
//	  			request.setBizContent("{" +
//	  			"\"out_biz_no\":\""+out_biz_no+"\"," +
//	  			"  }");
//	  			AlipayFundTransOrderQueryResponse response;
//				try {
//					response = alipayClient.execute(request);
//					if(response.isSuccess()){
//						withdrawal.setState("提现成功");
//						withdrawalService.updateWithdrawal(withdrawal);
//			  		} else {
//			  			withdrawal.setState("提现失败 ");
//			  			withdrawalService.updateWithdrawal(withdrawal);
//			  		}
//				} catch (AlipayApiException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}	  			
//	  	    }		
		
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//	  		@ResponseBody
//	  		@RequestMapping(value=("/aliPayOrder"),method=RequestMethod.POST)
//	  		public Dto aliPayOrder() {
//	  			String pay = aliPay.aliPay("0.01", "123456789987456123");
////	  			System.out.println(pay);
//	  			return DtoUtil.returnSuccess(pay);
//	  		} 
	  		
	
	/**
	 * @param userId
	 *            充值人
	 * @param tradeMoney
	 *            充值money(RMB)
	 * @throws AlipayApiException
	 *             ModelAndView
	 */
	@RequestMapping(value = "api/alipay/createOrder", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public Model alipay(@RequestParam("userId") String userId, @RequestParam("tradeMoney") String tradeMoney, Model m)
			throws AlipayApiException {

		String orderStr = "";
		try {

			/****** 1.封装你的交易订单开始 *****/ // 自己用

			// 此处封装你的订单数据，订单状态可以设置为等待支付

			/****** 1.封装你的交易订单结束 *****/

			Map<String, String> orderMap = new LinkedHashMap<String, String>(); // 订单实体
			Map<String, String> bizModel = new LinkedHashMap<String, String>(); // 公共实体

			/****** 2.商品参数封装开始 *****/ // 手机端用
			// 商户订单号，商户网站订单系统中唯一订单号，必填
			orderMap.put("out_trade_no", "trade.getOrderNumber()");
			// 订单名称，必填
			orderMap.put("subject", "手机网站支付购买游戏币");
			// 付款金额，必填
			orderMap.put("total_amount", tradeMoney);
			// 商品描述，可空
			orderMap.put("body", "您购买游戏币" + tradeMoney + "元");
			// 超时时间 可空
			orderMap.put("timeout_express", "30m");
			// 销售产品码 必填
			orderMap.put("product_code", "QUICK_WAP_PAY");

			/****** 2.商品参数封装结束 *****/

			/****** --------------- 3.公共参数封装 开始 ------------------------ *****/ // 支付宝用
			// 1.商户appid
			bizModel.put("app_id", Alipay.APP_ID);
			// 2.请求网关地址
			bizModel.put("method", Alipay.ALIPAY_BORDER_PROD);
			// 3.请求格式
			bizModel.put("format", Alipay.FORMAT);
			// 4.回调地址
			bizModel.put("return_url", Alipay.RETURN_URL);
			// 5.私钥
			bizModel.put("private_key", Alipay.APP_PRIVATE_KEY);
			// 6.商家id
			bizModel.put("seller_id", "2088102170411333");
			// 7.加密格式
			bizModel.put("sign_type", Alipay.SIGN_TYPE + "");

			/****** --------------- 3.公共参数封装 结束 ------------------------ *****/

			// 实例化客户端
			AlipayClient client = new DefaultAlipayClient(Alipay.ALIPAY_BORDER_PROD, Alipay.APP_ID,
					Alipay.APP_PRIVATE_KEY, Alipay.FORMAT, Alipay.CHARSET,
					Alipay.ALIPAY_PUBLIC_KEY, Alipay.SIGN_TYPE);

			// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
			AlipayTradeAppPayRequest ali_request = new AlipayTradeAppPayRequest();

			/**SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况
				 下取biz_content)。**/
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setPassbackParams(URLEncoder.encode((String) orderMap.get("body").toString()));
			; // 描述信息 添加附加数据
			model.setBody(orderMap.get("body")); // 商品信息
			model.setSubject(orderMap.get("subject")); // 商品名称
			model.setOutTradeNo(orderMap.get("out_trade_no")); // 商户订单号(自动生成)
			model.setTimeoutExpress(orderMap.get("timeout_express")); // 交易超时时间
			model.setTotalAmount(orderMap.get("total_amount")); // 支付金额
			model.setProductCode(orderMap.get("product_code")); // 销售产品码
			model.setSellerId("20881021********"); // 商家id
			ali_request.setBizModel(model);
			ali_request.setNotifyUrl(Alipay.NOTIFY_URL); // 回调地址

			AlipayTradeAppPayResponse response = client.sdkExecute(ali_request);
			orderStr = response.getBody();
			System.err.println(orderStr); // 就是orderString 可以直接给客户端请求，无需再做处理。

			m.addAttribute("result", orderStr);
			m.addAttribute("status", 0);
			m.addAttribute("msg", "订单生成成功");

		} catch (Exception e) {
			m.addAttribute("status", 1);
			m.addAttribute("msg", "订单生成失败");
		}

		return m;
	}
}
