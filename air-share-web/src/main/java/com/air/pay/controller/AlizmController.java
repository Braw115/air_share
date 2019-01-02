package com.air.pay.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.TokenUtil;
import com.air.pojo.vo.Alipay;
import com.air.pojo.vo.AppUserVo;
import com.air.user.service.AlipayService;
import com.air.user.service.AppUserService;
import com.air.utils.AlipayUtil;
import com.air.utils.EmptyUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZhimaCreditScoreGetRequest;
import com.alipay.api.response.ZhimaCreditScoreGetResponse;

@Controller
public class AlizmController {
	@Resource
    private AlipayService alipayService;
	
	@Resource
    private AppUserService appUserService;
	
	/**
	 * 获取芝麻分
	 * @param appAuthCode
	 * @return
	 * @throws AlipayApiException
	 */
	@ResponseBody
	@RequestMapping(value = "getQueryScore", method = RequestMethod.POST)
	public Dto getQueryScore(String appAuthCode) throws AlipayApiException {
//		AlipayClient alipayClient = new DefaultAlipayClient(Alipay.ZHIMA_URL, Alipay.APP_ID, Alipay.APP_PRIVATE_KEY,
//				Alipay.FORMAT, Alipay.CHARSET, Alipay.ALIPAY_PUBLIC_KEY);
		AlipayClient alipayClient = AlipayUtil.getAlipayClient();
		ZhimaCreditScoreGetRequest request = new ZhimaCreditScoreGetRequest();
		request.setBizContent("{" + " \"transaction_id\":\""+System.currentTimeMillis()+"\","
				+ " \"product_code\":\"w1010100100000000001\"" + " }");
		String accessToken = alipayService.getAccessToken(appAuthCode);
		ZhimaCreditScoreGetResponse response = alipayClient.execute(request, accessToken);
		if (response.isSuccess()) {
			System.out.println("调用成功");
			// 取得芝麻分
			System.out.println("芝麻分=" + response.getZmScore());
			return DtoUtil.returnSuccess("", response.getZmScore());
		} else {
			System.out.println("调用失败");
			// 处理各种异常
			System.out.println("查询芝麻分错误 code=" + response.getCode());
		}
		return DtoUtil.returnFail("error", "null");
	}
	
	/**
	 * 芝麻认证
	 * @param cert_name
	 * @param cert_no
	 * @return
	 * @throws AlipayApiException
	 */
	@ResponseBody
	@RequestMapping(value = "/ZMRZ", method = RequestMethod.POST)
	public Dto zmrz(@RequestBody HashMap<String, String> map) throws AlipayApiException {
		String cert_name = map.get("cert_name");
		String cert_no = map.get("cert_no");
		String bizNo = alipayService.zhimaCustomerCertificationCertify(cert_name,cert_no);
		String url = alipayService.zhimaCustomerCertificationCertify(bizNo);
		if (EmptyUtils.isEmpty(url)) {
			return DtoUtil.returnFail("认证请求URL为空", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnDataSuccess(url);
	}
	
	/**
	 * 查询认证信息
	 * @param map
	 * @param req
	 * @return
	 * @throws AlipayApiException
	 */
	@ResponseBody
	@RequestMapping(value = "/ZMRZCheck", method = RequestMethod.POST)
	public Dto ZMRZCheck(@RequestBody HashMap<String, String> map,HttpServletRequest req) throws AlipayApiException {
		String bizNo = map.get("biz_no");
		String passed = alipayService.ZhimaCustomerCertificationQuery(bizNo);
		if ("true".equals(passed)) {
			//修改用户认证状态
			AppUserVo appUserVo = new AppUserVo();
			appUserVo.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
			appUserVo.setIsDoZmrz("yes");
			return appUserService.modifyAppUser(appUserVo);
		}
		return DtoUtil.returnDataSuccess(false);
	}
	
}
