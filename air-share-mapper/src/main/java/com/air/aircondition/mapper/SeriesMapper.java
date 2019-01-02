package com.air.aircondition.mapper;

import java.util.List;

import com.air.pojo.entity.Series;

public interface SeriesMapper {
	/**
	 * 
	 * <p>Title: addSeries</p>
	 * <p>Description: 添加系列</p>
	 * @param series
	 * @return
	 */
	public Integer addSeries(Series series);
	
	/**
	 * 
	 * <p>Title: selectSeriesList</p>
	 * <p>Description:获取系列列表根据条件 </p>
	 * @return
	 */
	public List<Series> selectSeriesListByType(String type);
	
	/**
	 * 
	 * <p>Title: selectSeriesList</p>
	 * <p>Description:获取系列列表 </p>
	 * @return
	 */
	public List<Series> selectSeriesList();
	
	/**
	 * 
	 * <p>Title: selectSeriesById</p>
	 * <p>Description: 根据系列id查询系列信息</p>
	 * @param seriesId
	 * @return
	 */
	public Series selectSeriesById(Integer seriesId);
	
	/**
	 * 
	 * <p>Title: SeriesNumReduceOne</p>
	 * <p>Description: 库存减1根据系列ID</p>
	 * @param seriesId
	 * @return
	 */
	public Integer seriesNumReduceOne(Integer seriesId);
	
	/**
	 * 
	 * <p>Title: UpdateSeries</p>
	 * <p>Description: 修改系列信息根据ID</p>
	 * @param series
	 * @return
	 */
	public Integer updateSeries(Series series);
	
	/**
	 * 
	 * <p>Title: deleteSeries</p>
	 * <p>Description: 删除系列根据id</p>
	 * @param seriesId
	 * @return
	 */
	public Integer deleteSeries(Integer seriesId);
}
