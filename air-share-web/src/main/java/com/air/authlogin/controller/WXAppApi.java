package com.air.authlogin.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.base.controller.BaseController;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.manager.controller.WXAppManager;
import com.air.pojo.entity.AppUser;
import com.air.pojo.vo.AccessTokenReqVo;
import com.air.pojo.vo.AccessTokenVo;
import com.air.pojo.vo.AppUserRespVo;
import com.air.pojo.vo.AppUserVo;
import com.air.pojo.vo.ItripTokenVO;
import com.air.pojo.vo.RespVo;
import com.air.pojo.vo.WebChatVo;
import com.air.redis.RedisAPI;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.air.utils.MsgUtils;
import com.alibaba.fastjson.JSON;


@Controller
@RequestMapping("/wx")
public class WXAppApi extends BaseController {
	private Logger logger = LoggerFactory.getLogger(WXAppApi.class);
	@Autowired
	private WXAppManager wxAppManager;
	
	@Autowired 
	private AppUserService appUserService;
	
	/**
	 * 
	 * @param code
	 * @param req
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/loginbycode",method=RequestMethod.POST)
	public Dto loginByCode(@RequestBody HashMap<String, String> map,HttpServletRequest req,HttpSession session) {
		try {
			String code = map.get("code");
			if (StringUtils.trimToNull(code) == null) {
				return DtoUtil.returnFail("code required",ErrorCode.RESP_ERROR);
			}
			final AccessTokenVo accessTokenVo = wxAppManager.getOauthAccessTokenByCode(code);
			if (accessTokenVo == null || StringUtils.trimToNull(accessTokenVo.getOpenid()) == null) {
				return DtoUtil.returnFail("can't get openid",ErrorCode.RESP_ERROR);
			}

			// TODO check
			List<AppUser> users = appUserService.queryBySql(new HashMap<String, String>(){{
				put("where", "where wxunionid='" + accessTokenVo.getUnionid() + "'");
			}});
						
			WebChatVo webChatVo = wxAppManager.getOauthUserInfo(accessTokenVo.getAccess_token(),
					accessTokenVo.getOpenid());
			accessTokenVo.setImgurl(webChatVo.getHeadimgurl());
			accessTokenVo.setNickname(webChatVo.getNickname());
			if (users.size() > 0) {
				AppUser user = users.get(0);
				if (EmptyUtils.isEmpty(user.getTelephone())) {
					return DtoUtil.returnSuccess("needPhone",accessTokenVo);
				}
				user.setSex("1".equals(webChatVo.getSex())?"M":"W");
				user.setWxnickname(webChatVo.getNickname());
				if (EmptyUtils.isEmpty(user.getNickname())) {
					user.setNickname(webChatVo.getNickname());
				}
				if (EmptyUtils.isEmpty(user.getHeadimg())) {
					user.setHeadimg(webChatVo.getHeadimgurl());
				}
				appUserService.modifyByPrimaryKeySelective(user);
				
			} else{
				users = appUserService.queryBySql(new HashMap<String, String>(){{
					put("where", "where wxappopenid='" + accessTokenVo.getOpenid() + "'");
				}});
				
				if(users.size() == 0 || users.get(0).getTelephone() ==null){
					return DtoUtil.returnSuccess("needPhone",accessTokenVo);
				}else{
					AppUser userUpdate = users.get(0);
					userUpdate.setWxunionid(accessTokenVo.getUnionid());
					userUpdate.setWxnickname(webChatVo.getNickname());
					userUpdate.setSex("1".equals(webChatVo.getSex())?"M":"W");
					if (EmptyUtils.isEmpty(userUpdate.getNickname())) {
						userUpdate.setNickname(webChatVo.getNickname());
					}
					if (EmptyUtils.isEmpty(userUpdate.getHeadimg())) {
						userUpdate.setHeadimg(webChatVo.getHeadimgurl());
					}
					appUserService.modifyByPrimaryKeySelective(userUpdate);
				}
			}
				
			AppUser user = users.get(0);

			ItripTokenVO token = appUserService.getToken(req, session, user);
			user.setToken(token.getToken());
			return DtoUtil.returnSuccess("ok", user);
		} catch (Exception e) {
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.RESP_ERROR);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/loginbyphone",method=RequestMethod.POST)
	public Dto loginbyphone(@RequestBody AccessTokenReqVo accessTokenReqVo,HttpServletRequest req,HttpSession session) {
		try {
			String regex = "1[0-9]{10}";
			if (!accessTokenReqVo.getPhone().matches(regex)) {
				return DtoUtil.returnFail("phonenumber is illegal",ErrorCode.RESP_ERROR);
			}
			String checkMsg = MsgUtils.checkMsg(accessTokenReqVo.getPhone(), accessTokenReqVo.getSmscode());
			if ("error".equals(checkMsg)) {
				return DtoUtil.returnFail("验证码错误",ErrorCode.RESP_ERROR);
			}

			final String wxunionid = accessTokenReqVo.getUnionid();
			List<AppUser> users = appUserService.queryBySql(new HashMap<String, String>(){{
				put("where", "where wxunionid='" + wxunionid + "'");
			}});
			WebChatVo webChatVo = wxAppManager.getOauthUserInfo(accessTokenReqVo.getAccess_token(), accessTokenReqVo.getOpenid());

			AppUser dbAppUser = appUserService.queryByPhone(accessTokenReqVo.getPhone());
			if (users.size() == 0) {
				if(dbAppUser != null) {
					dbAppUser.setSex("1".equals(webChatVo.getSex())?"M":"W");
					dbAppUser.setWxunionid(accessTokenReqVo.getUnionid());
					dbAppUser.setWxappopenid(accessTokenReqVo.getOpenid());
					dbAppUser.setWxnickname(webChatVo.getNickname());
					if (EmptyUtils.isEmpty(dbAppUser.getNickname())) {
						dbAppUser.setNickname(webChatVo.getNickname());
					}
					if (EmptyUtils.isEmpty(dbAppUser.getHeadimg())) {
						dbAppUser.setHeadimg(webChatVo.getHeadimgurl());
					}
					appUserService.modifyByPrimaryKeySelective(dbAppUser);
				}
				else {
					Boolean bool = appUserService.addWxappUser(accessTokenReqVo.getOpenid(), webChatVo.getNickname(),
							webChatVo.getHeadimgurl(),webChatVo.getSex(), accessTokenReqVo.getUnionid(), accessTokenReqVo.getPhone());
					if (!bool) {
						return DtoUtil.returnFail("create user err",ErrorCode.RESP_ERROR);
					}
				}
				dbAppUser = appUserService.queryByPhone(accessTokenReqVo.getPhone());
			

				ItripTokenVO token = appUserService.getToken(req, session, dbAppUser);
				dbAppUser.setToken(token.getToken());
				return DtoUtil.returnSuccess("ok", dbAppUser);
			}
			
			if (EmptyUtils.isNotEmpty(dbAppUser)) {
				return DtoUtil.returnFail("手机号已注册", ErrorCode.RESP_ERROR);
			}
			
			AppUser appUserUpdate = users.get(0);
			appUserUpdate.setTelephone(accessTokenReqVo.getPhone());
			Boolean bool = appUserService.modifyByPrimaryKeySelective(appUserUpdate);
			if (!bool) {
				return DtoUtil.returnFail("请求错误", ErrorCode.RESP_ERROR);	
			}
			ItripTokenVO token = appUserService.getToken(req, session, appUserUpdate);
			appUserUpdate.setToken(token.getToken());
			return DtoUtil.returnSuccess("ok", appUserUpdate);
		} catch (Exception e) {
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.RESP_ERROR);
		}
	}
	
}
