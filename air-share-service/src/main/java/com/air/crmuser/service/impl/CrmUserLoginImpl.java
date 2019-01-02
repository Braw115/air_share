package com.air.crmuser.service.impl;

import java.util.Calendar;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.crmuser.mapper.CrmUserMapper;
import com.air.crmuser.service.CrmUserLogin;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.vo.ItripTokenVO;
import com.air.redis.RedisAPI;
import com.air.token.service.TokenBiz;
import com.alibaba.fastjson.JSONArray;

@Service
@Transactional
public class CrmUserLoginImpl implements CrmUserLogin {

	
	@Resource
	private CrmUserMapper crmUserMapper;
	@Autowired
	private TokenBiz tokenBiz;
	@Resource 
	private RedisAPI redisAPI;
	
	@Override
	public ItripTokenVO getToken(HttpServletRequest request, HttpSession session, CrmUser dbCrmUser) {
		String token = tokenBiz.generateToken(
				request.getHeader("user-agent"), dbCrmUser);
		tokenBiz.save(token, dbCrmUser);
		
		//返回ItripTokenVO
		ItripTokenVO tokenVO=new ItripTokenVO(token,
				Calendar.getInstance().getTimeInMillis()+TokenBiz.SESSION_TIMEOUT*1000,//2h有效期
				Calendar.getInstance().getTimeInMillis());			
		CrmUser u =crmUserMapper.selectCrmUserByUsername(dbCrmUser.getUsername());
		tokenVO.setAccountId((int) u.getCrmuserId());
		tokenVO.setIdentity("CrmUser");
		tokenVO.setUserName(dbCrmUser.getUsername());
		//tokenVO.setBeadHouseid(u.getBeadHouseid());
		//新增语句
		System.out.println("token：" + token);
		String value = JSONArray.toJSONString(tokenVO);
		System.out.println("登录成功：" + value);
		session.setAttribute("username", dbCrmUser.getUsername());
		return tokenVO;
	}

	@Override
	public Dto logout(HttpServletRequest request,String token) {
		if(!tokenBiz.validate(request.getHeader("user-agent"), token))
			return DtoUtil.returnFail("token无效", ErrorCode.AUTH_TOKEN_INVALID);
		//删除token和信息
		try {
			tokenBiz.delete(token); 
			return DtoUtil.returnSuccess("注销成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DtoUtil.returnFail("注销失败", ErrorCode.AUTH_UNKNOWN);
		}	
	}

}
