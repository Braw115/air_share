package com.air.appInfo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.appinfo.service.AirInfoService;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.pojo.entity.AppInfo;
import com.air.pojo.vo.RespVo;
import com.air.utils.EmptyUtils;

@Controller
@RequestMapping("/airInfo")
public class airInfoController {

	
	@Resource
	private AirInfoService airInfoService;
	
	/**
	 * 根据type查询相关app信息
	 * @param type
	 * @return
	 */
	@RequestMapping( value="/getInfoBytype" ,method=RequestMethod.GET)
	@ResponseBody
	public Dto getInfoBytype(String type) {
		List<AppInfo> appInfo = airInfoService.queryContentBytype(type);
		if (EmptyUtils.isEmpty(appInfo)) {
			return DtoUtil.returnFail("无相关数据", "error");
		}
		return DtoUtil.returnSuccess("ok", appInfo);
	}
	
	
	
}
