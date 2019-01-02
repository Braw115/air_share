package com.air.aircondition.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.CarbonIndicators;
import com.air.pojo.entity.CarbonLevel;

public interface CarbonIndicatorsMapper {
	
	/**
	 * 
	 * <p>Title: addCarbonIndicators</p>
	 * <p>Description: 添加碳指标记录</p>
	 * @param indicators
	 * @return
	 */
	Integer addCarbonIndicators(CarbonIndicators indicators);
	
	/**
	 * 
	 * <p>Title: selectCarbonIndicators</p>
	 * <p>Description: 查询碳指标根据用户</p>
	 * @param type 
	 * @param appUserId
	 * @return
	 */
	List<CarbonIndicators> selectCarbonIndicators(@Param("appusersId")Integer appusersId, @Param("format")String format);
	
	/**
	 * 
	 * <p>Title: selectLevelByCarbon</p>
	 * <p>Description: 根据碳指标查询等级</p>
	 * @param carbon
	 * @return
	 */
	CarbonLevel selectLevelByCarbon(@Param("carbon") BigDecimal carbon);
	
	/**
	  * <p>Title: countUserAirUseTime</p>
	 * <p>Description: 统计单个人使用同一台空调时长</p>
	 * @param appusersId
	 * @param mac
	 * @return
	 */
	Long countUserAirUseTime(@Param("appusersId") Integer appusersId,@Param("mac") String mac);
	/**
	  * <p>Title: countUserAirElectricity</p>
	 * <p>Description: 统计单个人使用同一台空调电量</p>
	 * @param appusersId
	 * @param mac
	 * @return
	 */
	BigDecimal countUserAirElectricity(@Param("appusersId") Integer appusersId,@Param("mac") String mac);
}
