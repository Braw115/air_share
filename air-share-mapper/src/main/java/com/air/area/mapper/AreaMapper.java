package com.air.area.mapper;

import java.util.List;

import com.air.pojo.entity.Area;

public interface AreaMapper {
	
	/**
	 * 
	 * <p>Title: selectAreaList</p>
	 * <p>Description: 查询区域列表</p>
	 * @param area
	 * @return
	 */
	public List<Area> selectAreaList(Area area);
	
	/**
	 * 
	 * <p>Title: updateArea</p>
	 * <p>Description: 修改区域</p>
	 * @param area
	 * @return
	 */
	public Integer updateArea(Area area);
	
	/**
	 * 
	 * <p>Title: deleteArea</p>
	 * <p>Description: 删除区域</p>
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
	
	/**
	 * 
	 * <p>Title: getAreaByName</p>
	 * <p>Description:根据区域名称查区域 </p>
	 * @param name
	 * @return
	 */
	public Area getAreaByName(String name);
	
	

}
