package com.air.series.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.pojo.entity.Series;
import com.air.series.service.SeriesService;

@Controller
@RequestMapping("/series")
public class SeriesController {
	@Autowired
	private SeriesService seriesService;
	
	/**
	 * 
	 * <p>Title: addSeries</p>
	 * <p>Description: 添加型号</p>
	 * @param series
	 * @return
	 */
	@record(businessLogic="添加型号")
	@RequestMapping(value = "/addSeries", method = RequestMethod.POST)
	@RequiresPermissions(value="typeManage")
	@ResponseBody
	
	public Dto<String> addSeries(@RequestBody Series series) {
		if (StringUtils.isEmpty(series.getName().trim())) {
			return new Dto<String>(400, "设备型号不能为空");
		}

		if (StringUtils.isEmpty(series.getSpecification().trim())) {
			return new Dto<String>(400, "设备规格不能为空");
		}

//		if (StringUtils.isEmpty(series.getType().trim())) {
//			return new Dto<String>(400, "类别不能为空");
//		}
		
//		if (series.getPrice() == null || series.getPrice() <= 0) {
//			return DtoUtil.returnFail("价格格式错误", SystemCode.ERROR);
//		}
		
		Integer result = seriesService.addSeries(series);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("提交失败", SystemCode.ERROR);
		}

		return new Dto<String>(200, "提交成功");
	}
	
	/**
	 * 
	 * <p>Title: addSeries</p>
	 * <p>Description: 获取型号列表</p>
	 * @return
	 */
	@RequestMapping(value = "/getSeriesList", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"typeManage","consumeTrendAnalyse"},logical=Logical.OR)
	public Dto<List<Series>> addSeries() {
		return new Dto<>(200, "提交成功", seriesService.selectSeriesList());
	}
	
	/**
	 * 
	 * <p>Title: updateSeriesInfo</p>
	 * <p>Description:根据系列ID修改系列信息 </p>
	 * @return
	 */
	@record(businessLogic="根据系列ID修改系列信息")
	@RequestMapping(value = "/updateSeriesInfo", method = RequestMethod.POST)
	@ResponseBody
	
	public Dto<String> updateSeriesInfo(@RequestBody Series series) {
		if(series.getSeriesId() == null) {
			return new Dto<>(400, "请选择要修改的系列");
		}
		
		Integer result = seriesService.updateSeriesInfo(series);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return new Dto<String>(400, "修改失败");
		}
		
		return new Dto<>(200, "修改成功");
	}
	
	/**
	 * 
	 * <p>Title: deleteSeries</p>
	 * <p>Description: 删除系列根据ID</p>
	 * @param seriesId
	 * @return
	 */
	@record(businessLogic="删除系列根据ID")
	@RequestMapping(value = "/deleteSeries", method = RequestMethod.POST)
	@ResponseBody
	
	public Dto<String> deleteSeries(@RequestBody Series series) {
		Integer seriesId = series.getSeriesId();
		if (seriesId == null) {
			return DtoUtil.returnFail("型号ID不能为空", SystemCode.ERROR);
		}
		
		Integer result = seriesService.deleteSeries(seriesId);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return new Dto<String>(400, "删除失败");
		}
		
		return new Dto<>(200, "删除成功");
	}
}
