package com.air.operations.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Series;
import com.air.pojo.vo.ConsumeAnalysisVO;
import com.air.pojo.vo.OperationsVO;
import com.air.pojo.vo.PayHabitVO;
import com.air.pojo.vo.StockVO;

public interface OperationsMapper {
	
	/**
	 * 
	 * <p>Title: consumeStatistics</p>
	 * <p>Description: 消费统计</p>
	 * @param operation
	 * @return
	 */
	public List<OperationsVO> consumeStatistics(OperationsVO operation);
	
	/**
	 * 
	 * <p>Title: payHabitStatisticsByTime</p>
	 * <p>Description: 习惯支付时间段</p>
	 * @param payHabit
	 * @return
	 */
	public List<PayHabitVO> payHabitStatisticsByTime(PayHabitVO payHabit);
	
	/**
	 * 
	 * <p>Title: payHabitStatisticsByMethod</p>
	 * <p>Description: 习惯支付方式</p>
	 * @param payHabit
	 * @return
	 */
	public List<PayHabitVO> payHabitStatisticsByMethod(PayHabitVO payHabit);
	
	/**
	 * 
	 * <p>Title: consumeTop</p>
	 * <p>Description: 消费TOP</p>
	 * @param count
	 * @return
	 */
	public List<Series> consumeTop(@Param("count")Integer count, @Param("seriesName")String seriesName);
	
	/**
	 * 
	 * <p>Title: consumeLater</p>
	 * <p>Description: 消费Later</p>
	 * @param count
	 * @return
	 */
	public List<Series> consumeLater(@Param("count")Integer count, @Param("seriesName")String seriesName);
	
	/**
	 * 
	 * <p>Title: consumeTrend</p>
	 * <p>Description: 获取指定空调 近dayCount天的消费情况</p>
	 * @param macList 空调列表
	 * @param dayCount 查询天数
	 * @return
	 */
	public List<Series> consumeTrend(@Param("macList")ArrayList<Integer> macList, @Param("dayCount")Integer dayCount);
	
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
	public List<StockVO> stockStatistics(StockVO stockVO);
}
