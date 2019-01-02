package com.air.series.service;

import java.util.List;

import com.air.pojo.entity.Series;

public interface SeriesService {
	
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
	 * <p>Title: getSeriesList</p>
	 * <p>Description: 获取系列列表</p>
	 * @return
	 */
	public List<Series> selectSeriesList();
	
	/**
	 * 
	 * <p>Title: updateSeriesInfo</p>
	 * <p>Description: 根据系列ID修改系列信息</p>
	 * @param series
	 * @return
	 */
	public Integer updateSeriesInfo(Series series);
	
	/**
	 * 
	 * <p>Title: deleteSeries</p>
	 * <p>Description: 删除系列</p>
	 * @param seriesId
	 * @return
	 */
	public Integer deleteSeries(Integer seriesId);

}
