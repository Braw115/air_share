package com.air.user.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.pojo.entity.Logger;
import com.air.user.service.LoggerService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/logger")
public class LoggerController {
	
	@Autowired
	private LoggerService loggerService;
	
	/**
	 * 
	 * <p>Title: usersLogin</p>
	 * <p>Description: 查询用户操作日志</p>
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/getLogger",method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"queryLogs"})
	public Dto getLogger(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
		PageInfo<Logger> pageInfo = loggerService.selectLogger(pageNum, pageSize);
		return DtoUtil.returnDataSuccess(pageInfo);
	}
	
	/**
	 * 
	 * <p>Title: delAllLogger</p>
	 * <p>Description: 清空日志</p>
	 * @return
	 */
	@record(businessLogic="清空操作日志")
	@RequestMapping(value="/delAllLogger",method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"claerLogs"})
	public Dto delAllLogger() {
		Integer result = loggerService.delAllLogger();
		return DtoUtil.returnDataSuccess(result);
	}
}
