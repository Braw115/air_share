package com.air.operations.service;

import java.util.ArrayList;
import java.util.List;

import com.air.pojo.entity.Series;
import com.air.pojo.vo.ConsumeAnalysisVO;
import com.air.pojo.vo.OperationsVO;
import com.air.pojo.vo.PayHabitVO;
import com.air.pojo.vo.StockVO;
import com.github.pagehelper.PageInfo;

public interface OperationsService {
	
	/**
	 * 
	 * <p>Title: consumeStatistics</p>
	 * <p>Description: 消费使用记录</p>
	 * @param operations
	 * @return
	 */
	public PageInfo<OperationsVO> consumeStatistics(OperationsVO operations);
	public List<OperationsVO> consumeStatisticsList(OperationsVO operations);
	/**
	 * 
	 * <p>Title: consumeStatistics</p>
	 * <p>Description: 消费方式统计</p>
	 * @param payHabit
	 * @return
	 */
	public List<PayHabitVO> payHabitStatisticsByMethod(PayHabitVO payHabit);
	
	/**
	 * 
	 * <p>Title: payHabitStatisticsByTime</p>
	 * <p>Description: 消费时间的统计</p>
	 * @param payHabit
	 * @return
	 */
	public List<PayHabitVO> payHabitStatisticsByTime(PayHabitVO payHabit);
	
	/**
	 * 
	 * <p>Title: custumeTop</p>
	 * <p>Description: 消费前几</p>
	 * @param num
	 * @return
	 */
	public List<Series> custumeTop(Integer num, String seriesName);
	
	/**
	 * 
	 * <p>Title: custumeLater</p>
	 * <p>Description: 消费后几</p>
	 * @param num
	 * @return
	 */
	public List<Series> custumeLater(Integer num, String seriesName);
	
	/**
	 * 
	 * <p>Title: consumeTrend</p>
	 * <p>Description: 获取指定空调 近dayCount天的消费情况</p>
	 * @param macList
	 * @param dayCount
	 * @return
	 */
	public List<Series> consumeTrend(ArrayList<Integer> macList, Integer dayCount);
	
	/**
	 * 
	 * <p>Title: consumeAnalysis</p>
	 * <p>Description: 消费使用分析</p>
	 * @param analysisVO
	 * @return
	 */
	public List<ConsumeAnalysisVO> consumeAnalysis(ConsumeAnalysisVO analysisVO);
	
	/**
	 * 
	 * <p>Title: stockStatistics</p>
	 * <p>Description: 存量统计</p>
	 * @param stockVO
	 * @return
	 */
	public PageInfo<StockVO> stockStatistics(StockVO stockVO);
}
