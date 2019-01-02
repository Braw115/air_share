package com.air.area.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.area.mapper.AreaMapper;
import com.air.area.service.AreaService;
import com.air.constant.SystemCode;
import com.air.pojo.entity.Area;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
	@Autowired
	private AreaMapper areaMapper;
	
	/**
	 * 查询区域列表
	 */
	@Override
	public PageInfo<Area> selectAreaList(Integer pageNum, Integer pageSize, Area area) {
		PageHelper.startPage(pageNum, pageSize);
		List<Area> list = areaMapper.selectAreaList(area);
		PageInfo<Area> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 修改区域信息
	 */
	@Override
	public Integer updateArea(Area area) {
		Area area2 = areaMapper.getAreaByName(area.getName());
		if (area2 != null && area2.getAreaId() != area.getAreaId()) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		return areaMapper.updateArea(area);
	}
	
	/**
	 * 删除区域
	 */
	@Override
	public Integer deleteArea(Integer areaId) {
		return areaMapper.deleteArea(areaId);
	}
	
	/**
	 * 添加区域
	 */
	@Override
	public Integer addArea(Area area) {
		Area area2 = areaMapper.getAreaByName(area.getName());
		if (area2 != null) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		
		return areaMapper.addArea(area);
	}

}
