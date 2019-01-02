package com.air.authlogin.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.pojo.entity.AppUser;
import com.air.pojo.vo.Alipay;
import com.air.pojo.vo.ItripTokenVO;
import com.air.pojo.vo.ZFBUserVo;
import com.air.user.service.AlipayService;
import com.air.user.service.AppUserService;
import com.air.utils.AlipayUtil;
import com.air.utils.EmptyUtils;
import com.air.utils.MsgUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;

@Controller
@RequestMapping("/AliApp")
public class AliAppController {
	
	
	@Resource
    private AlipayService alipayService;
	
	@Resource
    private AppUserService appUserService;
	
	/**
	 * 支付宝授权登录
	 */
	@ResponseBody
	@RequestMapping(value="/loginByZFBcode",method=RequestMethod.POST)
	public Dto loginByZFB(@RequestBody HashMap<String, String> map,HttpServletRequest req,HttpSession session) throws AlipayApiException {
		String authCode = map.get("authcode");
		String accessToken = alipayService.getAccessToken(authCode);
		if (EmptyUtils.isEmpty(accessToken)) {
			return DtoUtil.returnFail("获取accessToken失败", ErrorCode.RESP_ERROR);
		}
		ZFBUserVo zfbUserVo = alipayService.getUserInfoByToken(accessToken);
		
		if (EmptyUtils.isEmpty(zfbUserVo)) {
			return DtoUtil.returnFail("请求数据用户信息为空", ErrorCode.RESP_ERROR);
		}
		
		AppUser appUser = appUserService.queryByZfbUserId(zfbUserVo.getUser_id());
		
		if (EmptyUtils.isEmpty(appUser)||EmptyUtils.isEmpty(appUser.getTelephone())) {
			return DtoUtil.returnSuccess("needPhone",zfbUserVo);
		}
		ItripTokenVO token = appUserService.getToken(req, session, appUser);
		appUser.setToken(token.getToken());
		return DtoUtil.returnSuccess("success",appUser);
	}
	

	
	/**
	 * 生成auth_Info字符串传给前端
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getAuthInfo")
	public Dto getAuthInfo() {
		String content="apiname=com.alipay.account.auth&app_id="+Alipay.APP_ID+"&app_name=mc&auth_type=AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid="+Alipay.PID+"&product_id=APP_FAST_LOGIN&scope=kuaijie&target_id="+System.currentTimeMillis()+"&sign_type=RSA2";
		String sign =null;
		String enCodesign=null;
		try {
			sign = AlipaySignature.rsaSign(content, Alipay.APP_PRIVATE_KEY, Alipay.CHARSET, Alipay.SIGN_TYPE);
			enCodesign = URLEncoder.encode(sign, "UTF-8");
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String authInfo = content+"&sign="+enCodesign;
		return DtoUtil.returnSuccess("ok", authInfo);
	}
	
	/**
	 * 支付宝授权登录
	 */
	@ResponseBody
	@RequestMapping(value="/loginByZFBPhone",method=RequestMethod.POST)
	public Dto loginByZFBPhone(@RequestBody ZFBUserVo zfbUserVo,HttpServletRequest req,HttpSession session) throws AlipayApiException {
		
		if (!MsgUtils.isMobile(zfbUserVo.getPhone())) {
			return DtoUtil.returnFail("手机号不合法", ErrorCode.RESP_ERROR);
		}
		try {
			String msg = MsgUtils.checkMsg(zfbUserVo.getPhone(), zfbUserVo.getSmscode());
			if ("error".equals(msg)) {
				return DtoUtil.returnFail("验证码错误", ErrorCode.RESP_ERROR);
			}
			AppUser dbAppUser = appUserService.queryByPhone(zfbUserVo.getPhone());
			AppUser appUser = appUserService.queryByZfbUserId(zfbUserVo.getUser_id());
			if (EmptyUtils.isEmpty(dbAppUser)) {
				if (EmptyUtils.isEmpty(appUser)) {//手机号未注册支付宝未授权
					return appUserService.addZfbAppUser(req,session,zfbUserVo.getUser_id(),zfbUserVo.getImgurl(),zfbUserVo.getPhone(),zfbUserVo.getNickname(),zfbUserVo.getGender());
				}
				//手机号未注册支付宝已授权
				appUser.setTelephone(zfbUserVo.getPhone());
				appUser.setSex("m".equals(zfbUserVo.getGender())?"M":"W");
				Boolean bool = appUserService.modifyByPrimaryKeySelective(appUser);
			}
			
			if (EmptyUtils.isEmpty(appUser)) {//手机号已注册支付宝未授权
				dbAppUser.setAliImg(zfbUserVo.getImgurl());
				dbAppUser.setAliname(zfbUserVo.getNickname());
				dbAppUser.setAliUserId(zfbUserVo.getUser_id());
				dbAppUser.setSex("m".equals(zfbUserVo.gender)?"M":"W");
				if (EmptyUtils.isEmpty(dbAppUser.getHeadimg())) {
					dbAppUser.setHeadimg(zfbUserVo.getImgurl());
				}
				if (EmptyUtils.isEmpty(dbAppUser.getNickname())) {
					dbAppUser.setNickname(zfbUserVo.getNickname());
				}
				Boolean bool = appUserService.modifyByPrimaryKeySelective(dbAppUser);
				if (!bool) {
					return DtoUtil.returnFail("登陆失败", ErrorCode.RESP_ERROR);
				}
				ItripTokenVO token = appUserService.getToken(req, session, dbAppUser);
				dbAppUser.setToken(token.getToken());
				return DtoUtil.returnSuccess("success",dbAppUser);
			}
			//手机号已注册支付宝已授权 两个不能合一冲突
			return DtoUtil.returnFail("请更换手机号", ErrorCode.RESP_ERROR);
			
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail(e.getMessage(),ErrorCode.RESP_ERROR);
		}
		
	}
}
