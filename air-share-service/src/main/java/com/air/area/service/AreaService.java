package com.air.area.service;

import com.air.pojo.entity.Area;
import com.github.pagehelper.PageInfo;

public interface AreaService {
	
	/**
	 * 
	 * <p>Title: selectAreaList</p>
	 * <p>Description: 获取区域信息</p>
	 * @param pageNum
	 * @param pageSize
	 * @param area
	 * @return
	 */
	public PageInfo<Area> selectAreaList(Integer pageNum, Integer pageSize, Area area);
	
	/**
	 * 
	 * <p>Title: updateArea</p>
	 * <p>Description: 修改区域信息根据区域ID</p>
	 * @param area
	 * @return
	 */
	public Integer updateArea(Area area);
	
	/**
	 * 
	 * <p>Title: deleteArea</p>
	 * <p>Description: 删除区域根据区域ID</p>
	 * @param areaId
	 * @return
	 */
	public Integer deleteArea(Integer areaId);
	
	/**
	 * 
	 * <p>Title: addArea</p>
	 * <p>Description: 添加区域</p>
	 * @param area
	 * @return
	 */
	public Integer addArea(Area area);

}
