package com.air.config.controller;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.appinfo.service.AirInfoService;
import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.pojo.entity.AppInfo;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/crm/config")
public class CrmConfigController {
	
	
	@Resource
	private AirInfoService airInfoService; 
	
	
	/**
	 * 添加 支付/平台 配置
	 * @return
	 */
	@record(businessLogic="添加 支付/平台 配置")
	@ResponseBody
	@RequestMapping(value="/addConfig",method=RequestMethod.POST)
	@RequiresPermissions(value= {"systemSetup"})
	public Dto addConfig(@RequestBody AppInfo appInfo) {
		if (EmptyUtils.isEmpty(appInfo.getContent())&&EmptyUtils.isEmpty(appInfo.getType())) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		Boolean bool = airInfoService.addConfig(appInfo);
		if (!bool) {
			return DtoUtil.returnFail("添加失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 通过id获取支付配置信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getConfigById")
	@RequiresPermissions(value= {"systemSetup"})
	public Dto getConfigById(Integer appInfoId) {
		if (EmptyUtils.isEmpty(appInfoId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		AppInfo appInfo= airInfoService.queryConfigById(appInfoId);
		return DtoUtil.returnSuccess("ok",appInfo);
	}
	
	/**
	 * type或content模糊查询获取支付配置信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getConfig",method=RequestMethod.POST)
	@RequiresPermissions(value= {"systemSetup"})
	public Dto getConfig(@RequestBody AppInfo appInfo,Integer curPage, Integer pageSize) {
		PageInfo<AppInfo> appInfoList = airInfoService.queryConfig(appInfo,curPage,pageSize);
		return DtoUtil.returnSuccess("ok",appInfoList);
	}
	
	/**
	 * 修改支付配置
	 * @param apInfo
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/modifyConfig",method=RequestMethod.POST)
	@RequiresPermissions(value= {"systemSetup"})
	public Dto modifyConfig(@RequestBody AppInfo appInfo) {
		
		Boolean bool = airInfoService.modifyConfig(appInfo);
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 获取所有支付配置相关信息
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllConfig")
	@RequiresPermissions(value= {"systemSetup"})
	public Dto getAllConfig(Integer curPage, Integer pageSize) {
		PageInfo<AppInfo> appInfo = airInfoService.queryAllConfig(curPage,pageSize);
		return DtoUtil.returnSuccess("ok",appInfo);
	}
	
//	/**
//	 * 删除支付配置
//	 * @param curPage
//	 * @param pageSize
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/delConfig",method=RequestMethod.POST)
////	@RequestMapping(value= {"admin"})
//	public Dto delConfig(@RequestBody HashMap<String, Integer> map) {
//		Integer appinfoId = map.get("appInfoId");
//		Boolean bool = airInfoService.delConfig(appinfoId);
//		if (!bool) {
//			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
//		}
//		return DtoUtil.returnSuccess("删除成功",ErrorCode.RESP_SUCCESS);
//	}
	

}
