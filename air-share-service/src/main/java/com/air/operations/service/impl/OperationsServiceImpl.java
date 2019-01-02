package com.air.operations.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.operations.mapper.OperationsMapper;
import com.air.operations.service.OperationsService;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.ConsumeAnalysisVO;
import com.air.pojo.vo.OperationsVO;
import com.air.pojo.vo.PayHabitVO;
import com.air.pojo.vo.StockVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class OperationsServiceImpl implements OperationsService {
	@Autowired
	private OperationsMapper operationsMapper;
	
	/**
	 * 分页查询消费使用记录
	 */
	@Override
	public PageInfo<OperationsVO> consumeStatistics(OperationsVO operations) {
		PageHelper.startPage(operations.getPageNum(), operations.getPageSize());
		List<OperationsVO> list = operationsMapper.consumeStatistics(operations);
		PageInfo<OperationsVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 查询消费使用记录,用于导出
	 */
	@Override
	public List<OperationsVO> consumeStatisticsList(OperationsVO operations) {
		PageHelper.startPage(operations.getPageNum(), operations.getPageSize());
		List<OperationsVO> list = operationsMapper.consumeStatistics(operations);
		return list;
	}
	
	/**
	 * 消费习惯统计
	 */
	@Override
	public List<PayHabitVO> payHabitStatisticsByMethod(PayHabitVO payHabit) {
		List<PayHabitVO> list = operationsMapper.payHabitStatisticsByMethod(payHabit);
		List<String> allList = new ArrayList<>(Arrays.asList("微信", "支付宝", "余额"));
		for (PayHabitVO payHabitVO : list) {
			String method = payHabitVO.getPayMethod();
			allList.remove(method);
		}
		
		for (String payMethod : allList) {
			PayHabitVO payHabitVO = new PayHabitVO();
			payHabitVO.setBeginDate(payHabit.getBeginDate());
			payHabitVO.setEndDate(payHabit.getEndDate());
			payHabitVO.setPayMethod(payMethod);
			payHabitVO.setNum(0);
			list.add(payHabitVO);
		}
		return list;
	}
	
	/**
	 * 消费时间段统计
	 */
	@Override
	public List<PayHabitVO> payHabitStatisticsByTime(PayHabitVO payHabit) {
		List<PayHabitVO> list = operationsMapper.payHabitStatisticsByTime(payHabit);
		List<PayHabitVO> arrayList = new ArrayList<>();
		int i = 0;
		for (int j = 0; j < 24; j++) {
			if (i<list.size() && list.get(i).getPayHour() == j) {
				arrayList.add(list.get(i));
				i++;
				continue;
			}
			PayHabitVO payHabitVO = new PayHabitVO();
			payHabitVO.setPayHour(j);
			payHabitVO.setNum(0);
			arrayList.add(payHabitVO);
		}
		return arrayList;
	}
	
	/**
	 * 消费前几名设备信息
	 */
	@Override
	public List<Series> custumeTop(Integer num, String seriesName) {
		List<Series> consumeTop = operationsMapper.consumeTop(num, seriesName);
		return consumeTop;
	}
	
	/**
	 * 消费后几名的设备信息
	 */
	@Override
	public List<Series> custumeLater(Integer num, String seriesName) {
		List<Series> consumeLater = operationsMapper.consumeLater(num, seriesName);
		return consumeLater;
	}

	/**
	 * 查询指定空调的近N天的消费记录
	 */
	@Override
	public List<Series> consumeTrend(ArrayList<Integer> macList, Integer dayCount) {
		List<Series> list = operationsMapper.consumeTrend(macList, dayCount);
		List<String> existDate = new ArrayList<>(); 
		for (Series series : list) {
			existDate.add(series.getOrder_date());
		}
		List<String> notExistDate = NotExistDate(dayCount, existDate);
		List<Series> arrayList = new ArrayList<>();
		int i = list.size();
		for (String date : notExistDate) {
			if (StringUtils.isEmpty(date)) {
				arrayList.add(list.get(i-1));
				i--;
				continue;
			}
			Series series = new Series();
			series.setOrder_date(date);
			series.setNum(0);
			arrayList.add(series);
		}
		Collections.reverse(arrayList);
		return arrayList;
	}
	
	/**
	 * 
	 * <p>Title: NotExistDate</p>
	 * <p>Description: 获取不存在数据的日期 </p>
	 * @param dayCount
	 * @return
	 */
	private List<String> NotExistDate(Integer dayCount, List<String> existDate){
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < dayCount; i++) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String curDate = format.format(cal.getTime());
			list.add(!existDate.contains(curDate)?curDate:null);
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		return list;
	}
	
	/**
	 * 消费使用分析
	 */
	@Override
	public List<ConsumeAnalysisVO> consumeAnalysis(ConsumeAnalysisVO analysisVO) {
		return operationsMapper.consumeAnalysis(analysisVO);
	}
	
	/**
	 * 存量统计
	 */
	@Override
	public PageInfo<StockVO> stockStatistics(StockVO stockVO) {
		PageHelper.startPage(stockVO.getPageNum(), stockVO.getPageSize());
		List<StockVO> list = operationsMapper.stockStatistics(stockVO);
		PageInfo<StockVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
}
