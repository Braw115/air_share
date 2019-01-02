package com.air.condition.service;

import java.util.List;

import com.air.pojo.entity.CarbonIndicators;
import com.air.pojo.vo.PersonalRecordVO;

public interface CarbonIndicatorsService {
	
	/**
	 * 
	 * <p>Title: selectCarbonIndicators</p>
	 * <p>Description: 查询用户的碳指标</p>
	 * @param appUserId
	 * @param type 
	 * @param pageSize 
	 * @param pageNum 
	 * @return
	 */
	List<CarbonIndicators> selectCarbonIndicators(Integer appUserId, String type, Integer pageNum, Integer pageSize);
	
	/**
	 * 
	 * <p>Title: getPersonalRecord</p>
	 * <p>Description: 获取个人记录</p>
	 * @param appusersId
	 * @param mac 
	 * @return
	 */
	PersonalRecordVO getPersonalRecord(Integer appusersId, String mac);

}
