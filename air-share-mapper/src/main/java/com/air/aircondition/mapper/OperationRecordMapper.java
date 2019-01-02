package com.air.aircondition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.OperationRecord;
import com.air.pojo.vo.OperationRecordVO;

public interface OperationRecordMapper {
	
	/**
	 * 
	 * <p>Title: addOperationRecord</p>
	 * <p>Description: 添加一个空调的操作记录</p>
	 * @param oRecord
	 * @return
	 */
	Integer addOperationRecord(OperationRecord oRecord);
	
	/**
	 * 
	 * <p>Title: selectByRecord</p>
	 * <p>Description: 获取记录列表</p>
	 * @param operVO
	 * @return
	 */
	List<OperationRecord> selectByRecord(OperationRecordVO operVO);
	
	/**
	 * 
	 * <p>Title: getLastOperRecord</p>
	 * <p>Description: 获取用户的最后一条操作指令</p>
	 * @param appuserId
	 * @return
	 */
	OperationRecord getLastOperRecord(Integer appusersId);
	
	/**
	 * 
	 * <p>Title: getLastOpendRecord</p>
	 * <p>Description: 获取最后一条指令的设备</p>
	 * @param userId
	 * @return
	 */
	OperationRecord getLastOpendRecord(@Param("userId")Integer userId, @Param("order")String order);
	
	/**
	 * 
	 * <p>Title: getLastOperRecordByMac</p>
	 * <p>Description: 获取空调的最后一条指令 </p>
	 * @param mac
	 * @return
	 */
	OperationRecord getLastOperRecordByMac(String mac);
}
