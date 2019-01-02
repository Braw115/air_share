package com.air.series.service.impl;

import java.util.List;

import org.python.antlr.PythonParser.if_stmt_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.aircondition.mapper.SeriesMapper;
import com.air.constant.SystemCode;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.AirConditionVO;
import com.air.series.service.SeriesService;

@Service
public class SeriesServiceImpl implements SeriesService {
	@Autowired
	private SeriesMapper seriesMapper;
	@Autowired
	private AirConditionMapper conditonMapper;
	
	/**
	 * 添加系列
	 */
	@Override
	public Integer addSeries(Series series) {
		List<Series> list = seriesMapper.selectSeriesListByType(series.getName());
		if (list.size() >= 1) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		
		return seriesMapper.addSeries(series);
	}
	
	/**
	 * 获取系列列表
	 */
	@Override
	public List<Series> selectSeriesList() {
		return seriesMapper.selectSeriesList();
	}
	
	/**
	 * 根据系列ID修改系列信息
	 */
	@Override
	public Integer updateSeriesInfo(Series series) {
		return seriesMapper.updateSeries(series);
	}
	
	/**
	 * 删除空调型号
	 */
	@Override
	public Integer deleteSeries(Integer seriesId) {
		AirConditionVO airCondition = new AirConditionVO();
		airCondition.setSeriesId(seriesId);
		List<AirConditionVO> list = conditonMapper.selectAllAirCondition(airCondition);
		for (AirConditionVO airConditionVO : list) {
			conditonMapper.deleteAirCondition(airConditionVO.getMac());
		}
		
		return seriesMapper.deleteSeries(seriesId);
	}

}
