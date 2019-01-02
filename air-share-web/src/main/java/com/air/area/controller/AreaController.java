package com.air.area.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.area.service.AreaService;
import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.pojo.entity.Area;
import com.github.pagehelper.PageInfo;

import cn.jiguang.common.utils.StringUtils;

/**
 * 
 * <p>Title: AreaController</p>
 * <p>Description: 区域模块</p>
 * @author czg
 * @date 2018年6月13日
 */
@Controller
@RequestMapping("/area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	
	/**
	 * 
	 * <p>Title: getAreaList</p>
	 * <p>Description: 获取区域列表</p>
	 * @param pageNum
	 * @param pageSize
	 * @param area
	 * @return
	 */
	@RequestMapping(value ="/getAreaList", method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"areaManage"})
	public Dto<PageInfo<Area>> getAreaList(Integer pageNum, Integer pageSize, Area area){
		PageInfo<Area> pageInfo = areaService.selectAreaList(pageNum, pageSize, area);
		return DtoUtil.returnDataSuccess(pageInfo);
	}
	
	/**
	 * 
	 * <p>Title: updateArea</p>
	 * <p>Description: 修改区域根据ID </p>
	 * @param area
	 * @return
	 */
	@record(businessLogic="修改区域")
	@RequestMapping(value ="/updateArea", method=RequestMethod.POST )
	@ResponseBody
	@RequiresPermissions(value= {"areaManage"})
	public Dto<String> updateArea(@RequestBody Area area){
		if(area.getAreaId() == null) {
			return DtoUtil.returnFail("请选择区域", "400");
		}
		Integer result = areaService.updateArea(area);
		return DtoUtil.returnSuccess("修改成功");
	}
	
	/**
	 * 
	 * <p>Title: deleteArea</p>
	 * <p>Description: 删除区域根据ID</p>
	 * @param areaId
	 * @return
	 */
	@record(businessLogic="删除区域")
	@RequestMapping(value ="/deleteArea", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"areaManage"})
	public Dto<String> deleteArea(@RequestBody Area area){
		Integer areaId = area.getAreaId();
		if (areaId == null) {
			return DtoUtil.returnFail("请选择区域", SystemCode.ERROR);
		}
		
		Integer result = areaService.deleteArea(areaId);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("删除失败", SystemCode.ERROR);
		}
		
		return DtoUtil.returnSuccess("删除成功");
	}
	
	/**
	 * 
	 * <p>Title: addArea</p>
	 * <p>Description: 添加区域</p>
	 * @param area
	 * @return
	 */
	@record(businessLogic="添加区域")
	@RequestMapping(value ="/addArea", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"areaManage"})
	public Dto<String> addArea(@RequestBody Area area){
		if(StringUtils.isEmpty(area.getName())) {
			return DtoUtil.returnFail("区域名不能为空", SystemCode.ERROR);
		}
		Integer result = areaService.addArea(area);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("添加区域失败", SystemCode.ERROR);
		}
		
		return DtoUtil.returnSuccess("添加区域成功");
	}
	
}
