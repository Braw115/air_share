package com.air.interfacerecord.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.interfacerecord.service.InterfaceRecordService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.InterfaceRecord;
import com.air.pojo.vo.InterfaceRecordVO;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/interface")
public class InterfaceRecordController {
	@Autowired
	private InterfaceRecordService interfaceRecordService;
	
	/**
	 * 
	 * <p>Title: getInterfaceRecord</p>
	 * <p>Description: 查询接口记录 </p>
	 * @param iRecordVO
	 * @return
	 */
	@RequestMapping(value ="/getInterfaceRecord", method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"interfaceManage"})
	public Dto<AirCondition> getInterfaceRecord(InterfaceRecordVO iRecordVO){
		PageInfo pageInfo = interfaceRecordService.selectInterfaceRecord(iRecordVO);
		return DtoUtil.returnDataSuccess(pageInfo);
	}
	
	/**
	 * 
	 * <p>Title: delInterfaceRecord</p>
	 * <p>Description: 删除接口根据ID</p>
	 * @param iRecordId
	 * @return
	 */
	@RequestMapping(value ="/delInterfaceRecord", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"interfaceManage"})
	public Dto<AirCondition> delInterfaceRecord(@RequestBody InterfaceRecord iRecord){
		Integer result = interfaceRecordService.deleteInterfaceRecord(iRecord.getInterfaceId());
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("删除失败", SystemCode.ERROR);
		}
		return DtoUtil.returnSuccess();
	}
	
	/**
	 * 
	 * <p>Title: addInterfaceRecord</p>
	 * <p>Description: 添加接口记录</p>
	 * @param iRecord
	 * @return
	 */
	@RequestMapping(value ="/addInterfaceRecord", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"interfaceManage"})
	public Dto<AirCondition> addInterfaceRecord(@RequestBody InterfaceRecord iRecord){
		if (StringUtils.isEmpty(iRecord.getUrl().trim())) {
			return DtoUtil.returnFail("请输入接口URL", SystemCode.ERROR);
		}
		if (StringUtils.isEmpty(iRecord.getDescription().trim())) {
			return DtoUtil.returnFail("请输入接口描述", SystemCode.ERROR);
		}
		if (StringUtils.isEmpty(iRecord.getMethod().trim())) {
			return DtoUtil.returnFail("请输入接口请求方式", SystemCode.ERROR);
		}
		
		Integer result = interfaceRecordService.addInterfaceRecord(iRecord);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("添加失败", SystemCode.ERROR);
		}
		return DtoUtil.returnSuccess();
	}
	
	/**
	 * 
	 * <p>Title: updateInterfaceRecord</p>
	 * <p>Description:修改接口记录 </p>
	 * @param iRecord
	 * @return
	 */
	@RequestMapping(value ="/updateInterfaceRecord", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"interfaceManage"})
	public Dto<AirCondition> updateInterfaceRecord(@RequestBody InterfaceRecord iRecord){
		if (iRecord.getInterfaceId() == null) {
			return DtoUtil.returnFail("请选择要修改的接口", SystemCode.ERROR);
		}
		
		Integer result = interfaceRecordService.updateInterfaceRecord(iRecord);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("修改失败", SystemCode.ERROR);
		}
		return DtoUtil.returnSuccess();
	}
}
