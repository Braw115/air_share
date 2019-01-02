package com.air.interfacerecord.mapper;

import java.util.List;

import com.air.pojo.entity.InterfaceRecord;
import com.air.pojo.vo.InterfaceRecordVO;

public interface InterfaceRecordMapper {
	
	/**
	 * 
	 * <p>Title: selectInterfaceRecord</p>
	 * <p>Description: 查询接口记录</p>
	 * @param iRecordVO
	 * @return
	 */
	public List<InterfaceRecord> selectInterfaceRecord(InterfaceRecordVO iRecordVO);
	
	/**
	 * 
	 * <p>Title: deleteInterfaceRecord</p>
	 * <p>Description: 删除接口记录通过ID</p>
	 * @param iRecordId
	 * @return
	 */
	public Integer deleteInterfaceRecord(Integer iRecordId);
	
	/**
	 * 
	 * <p>Title: addInterfaceRecord</p>
	 * <p>Description: 添加接口记录</p>
	 * @param iRecord
	 * @return
	 */
	public Integer addInterfaceRecord(InterfaceRecord iRecord);
	
	/**
	 * 
	 * <p>Title: updateInterfaceRecord</p>
	 * <p>Description: 修改接口记录</p>
	 * @param iRecord
	 * @return
	 */
	public Integer updateInterfaceRecord(InterfaceRecord iRecord);

}
