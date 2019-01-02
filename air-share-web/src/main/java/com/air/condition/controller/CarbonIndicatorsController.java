package com.air.condition.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.python.antlr.PythonParser.return_stmt_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.condition.service.CarbonIndicatorsService;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.pojo.entity.CarbonIndicators;
import com.air.pojo.vo.PersonalRecordVO;

@Controller
@RequestMapping("/carbon")
public class CarbonIndicatorsController {
	@Autowired
	private CarbonIndicatorsService carbonService;
	
	/**
	 * 
	 * <p>Title: getCarbonIndicators</p>
	 * <p>Description: 查询碳指标</p>
	 * @param carbon
	 * @return
	 */
	@RequestMapping(value ="/getCarbonIndicators", method=RequestMethod.GET)
	@ResponseBody
	public Dto getCarbonIndicators(HttpServletRequest request, @RequestParam("type") String type, Integer pageNum, Integer pageSize){
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		if (!"daily".equals(type) && !"month".equals(type) && !"year".equals(type)) {
			return DtoUtil.returnFail("查询类型错误", SystemCode.ERROR);
		}
		
		if ("daily".equals(type) && (pageNum == null || pageSize == null)) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		
		List<CarbonIndicators> carbonList = carbonService.selectCarbonIndicators(appusersId, type, pageNum, pageSize);
		return DtoUtil.returnDataSuccess(carbonList);
	}
	
	/**
	 * 
	 * <p>Title: getPersonalRecord</p>
	 * <p>Description: App用户个人记录</p>
	 * @param request
	 * @param mac
	 * @return
	 */
	@RequestMapping(value ="/getPersonalRecord", method=RequestMethod.GET)
	@ResponseBody
	public Dto getPersonalRecord(HttpServletRequest request, String mac){
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		
		PersonalRecordVO carbonList = carbonService.getPersonalRecord(appusersId, mac);
		if (carbonList.getMac() == null && mac != null) {
			return DtoUtil.returnFail("该空调不存在", "400", carbonList);
		}
		
		return DtoUtil.returnDataSuccess(carbonList);
	}
}
