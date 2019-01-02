package com.air.crmuser.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.MD5;
import com.air.crmuser.service.CrmUserLogin;
import com.air.crmuser.service.CrmUserService;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.vo.ItripTokenVO;

@Controller
@RequestMapping("/crm")
public class CrmLoginController{
	
	@Resource
	private CrmUserLogin crmUserLogin; 
	@Resource
	private CrmUserService crmUserService;
	
	@ResponseBody
	@RequestMapping(value="doLogin",method=RequestMethod.POST)
	public Dto crmuserLogin(@RequestBody CrmUser crmuser,
			HttpServletRequest request,
			HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
        String newPs =MD5.getMd5(crmuser.getPassword(),32);
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(crmuser.getUsername(), newPs);
		
		subject.login(usernamePasswordToken);
		CrmUser dbCrmUser = crmUserService.queryUserByUsername(crmuser.getUsername());
		ItripTokenVO tokenVO = crmUserLogin.getToken(request, session, dbCrmUser);
		dbCrmUser.setToken(tokenVO.getToken());
		dbCrmUser.setPassword("");
		
		return DtoUtil.returnDataSuccess(dbCrmUser);
		
	}
	/**
	 * 用户注销
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/logout",method=RequestMethod.GET,produces="application/json",headers="token")
	public @ResponseBody Dto logout(HttpServletRequest request){		
		//验证token
		String token=request.getHeader("token");
		
		System.out.println("注销：" + token);
		
		return crmUserLogin.logout(request,token);	
	}
	
}
