package com.air.crmuser.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.air.constant.Dto;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.vo.ItripTokenVO;

public interface CrmUserLogin {

	ItripTokenVO getToken(HttpServletRequest request, HttpSession session, CrmUser dbCrmUser);

	Dto logout(HttpServletRequest request, String token);
	
}

