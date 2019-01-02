package com.air.notice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.TokenUtil;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.Notice;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	
	/**
	 * 
	 * <p>Title: getNoticeList</p>
	 * <p>Description: 获取通知列表</p>
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value ="/getNotice", method=RequestMethod.GET)
	@ResponseBody
	public Dto<AirCondition> getNoticeList(@RequestParam("pageNum")Integer pageNum,HttpServletRequest request, @RequestParam("pageSize")Integer pageSize){
		PageInfo<Notice> pageInfo = noticeService.selectNoticeList(pageNum, pageSize,TokenUtil.getAppUserId(request.getHeader("token")));
		return DtoUtil.returnDataSuccess(pageInfo);
	}
}
