package com.air.user.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.MD5;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.CrmUserLogin;
import com.air.pojo.entity.AppUser;
import com.air.pojo.vo.AppUserVo;
import com.air.pojo.vo.ItripTokenVO;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.air.utils.MsgUtils;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private AppUserService appUserService;
	@Resource
	private CrmUserLogin crmUserLogin; 
	
	/**
	 * 用户登录
	 * @return
	 */
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public @ResponseBody Dto usersLogin(@RequestBody AppUser users,
							HttpServletRequest request,
							HttpSession session) {
		if (!EmptyUtils.isEmpty(users.getTelephone()) && !EmptyUtils.isEmpty(users.getPassword())) {
			Dto resp = null;
			try {				
				resp = appUserService.doLogin(users.getTelephone().trim(), MD5.getMd5(users.getPassword().trim(), 32));
			}  catch (Exception e) {
				return DtoUtil.returnFail(e.getMessage(), "error");
			}
			if ("error".equals(resp.getErrorCode())) {
				return resp;
			}
			
			AppUser dbUser = (AppUser)resp.getData();
			if (EmptyUtils.isNotEmpty(dbUser)) {
				
				ItripTokenVO tokenVO = appUserService.getToken(request, session, dbUser);
				dbUser.setToken(tokenVO.getToken());
				return DtoUtil.returnDataSuccess(dbUser);
			} else {
				return DtoUtil.returnFail("用户名或密码错误", "");				
			}
		} else {
			return DtoUtil.returnFail("参数错误！检查提交的参数名称是否正确。", "");			
		}		
	}
	
	/**
	 * 短信验证
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/loginByMsg",method=RequestMethod.POST)
	public Dto loginByMsg(HttpServletRequest request,
			HttpSession session,@RequestBody AppUserVo appUserVo) throws Exception {
		
		boolean mobile = MsgUtils.isMobile(appUserVo.getPhone());
		if (!mobile) {
			return DtoUtil.returnFail("手机号输入不符合规则", "");
		}
		String checkMsg = MsgUtils.checkMsg(appUserVo.getPhone(), appUserVo.getMsg());
		if ("error".equals(checkMsg)) {
			return DtoUtil.returnFail("验证码错误", ErrorCode.RESP_ERROR);
		}
		AppUser appUser = appUserService.queryByPhone(appUserVo.getPhone());
		if (EmptyUtils.isNotEmpty(appUser)) {
			
			ItripTokenVO tokenVO = appUserService.getToken(request, session, appUser);
			appUser.setToken(tokenVO.getToken());
			return DtoUtil.returnDataSuccess(appUser);
		} else {
			return appUserService.addPhoneUser(request,session,appUserVo.getPhone());
			
		}
		
	}
	
	/**
	 * 更换手机号
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/retPhoneByMsg",method=RequestMethod.POST)
	public Dto retPhoneByMsg(HttpServletRequest request,
			HttpSession session,@RequestBody HashMap<String, String> map) throws Exception {
		String newPhone = map.get("newPhone");
		String msgCode = map.get("msgCode");
		String ck= MsgUtils.checkMsg(newPhone, msgCode);
		if ("error".equals(ck)) {
			return DtoUtil.returnFail("验证码错误", ErrorCode.RESP_ERROR);
		}
		
		return appUserService.modifyPhoneForUser(TokenUtil.getAppUserId(request.getHeader("token")),newPhone);
	}
	/**
	 * 短信验证修改密码
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/resetPwdByMsg",method=RequestMethod.POST)
	public Dto retPwdByMsg(@RequestBody HashMap<String, String> map) throws Exception {
		String phone =map.get("phone");
		String newPwd = map.get("newPwd");
		String msgCode = map.get("msgCode");
		boolean mobile = MsgUtils.isMobile(phone);
		if (!mobile) {
			return DtoUtil.returnFail("手机号输入不符合规则", "");
		}
		AppUser appUser = appUserService.queryByPhone(phone);
		if (EmptyUtils.isNotEmpty(appUser)) {
			return DtoUtil.returnFail("该手机号未注册", ErrorCode.RESP_ERROR);
		} 
		String ck = MsgUtils.checkMsg(phone, msgCode);
		
		if ("error".equals(ck)) {
			return DtoUtil.returnFail("验证码错误", ErrorCode.RESP_ERROR);
		}
		
		return appUserService.resetPwd(phone,null,newPwd);
	}
	/**
	 * 修改密码
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/resetPwdByOldpwd",method=RequestMethod.POST)
	public Dto resetPwdByOldpwd(String phone,String oldPwd,String newPwd) throws Exception {
		
		boolean mobile = MsgUtils.isMobile(phone);
		if (!mobile) {
			return DtoUtil.returnFail("手机号输入不符合规则", ErrorCode.RESP_ERROR);
		}
		
		return appUserService.resetPwd(phone,oldPwd,newPwd);
	}
	
	/**
	 * 发送验证码
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value="/sendMsg",method=RequestMethod.POST)
	public Dto sendMsg(@RequestBody HashMap<String, String> map) throws Exception {
		String phone = map.get("phone");
		boolean mobile = MsgUtils.isMobile(phone);
		if (!mobile) {
			return DtoUtil.returnFail("手机号码输入不规范", ErrorCode.RESP_ERROR);
		}
		String sendMsg = MsgUtils.sendMsg(phone);
		if ("error".equals(sendMsg)) {
			return DtoUtil.returnFail("发送失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("发送成功");
	}
	
	/**
	 * 用户注销
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public @ResponseBody Dto logout(HttpServletRequest request){		
		//验证token
		String token=request.getHeader("token");
		
		System.out.println("注销：" + token);
		
		return crmUserLogin.logout(request,token);	
	}   
}

