package com.air.condition.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.condition.service.OperationRecordService;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.vo.OperationRecordVO;
import com.air.utils.DateFormats;

@Controller
@RequestMapping("/operReocrd")
public class OperationRecordController {
	@Autowired
	private OperationRecordService operationRecordService;
	
	@RequestMapping(value ="/getOperationRecord", method=RequestMethod.GET)
	@ResponseBody
	public Dto getOperationRecord(HttpServletRequest request, OperationRecordVO operVO){
		String token = request.getHeader("token"); 
		if (TokenUtil.respAppReturn(token)) {
			operVO.setAppusersId(TokenUtil.getAppUserId(token));
		}
		
		if (StringUtils.isEmpty(operVO.getMac().trim())) {
			return DtoUtil.returnFail("请选择空调", SystemCode.ERROR);
		}
		if (!StringUtils.isEmpty(operVO.getBeginDate()) && !DateFormats.isValidDate(operVO.getBeginDate())) {
			return DtoUtil.returnFail("开始时间格式错误", SystemCode.ERROR);
		}
  
		if (!StringUtils.isEmpty(operVO.getEndDate()) && !DateFormats.isValidDate(operVO.getEndDate())) {
			return DtoUtil.returnFail("结束时间格式错误", SystemCode.ERROR);
		}
		
		List<OperationRecord> operationRecordList = operationRecordService.selectOperationRecord(operVO);
		return DtoUtil.returnDataSuccess(operationRecordList);
		 
	}
}
