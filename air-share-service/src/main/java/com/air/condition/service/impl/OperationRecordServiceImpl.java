package com.air.condition.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.aircondition.mapper.OperationRecordMapper;
import com.air.condition.service.OperationRecordService;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.vo.OperationRecordVO;

@Service
@Transactional
public class OperationRecordServiceImpl implements OperationRecordService {
	@Autowired
	private OperationRecordMapper operationRecordMapper;
	
	/**
	 * 查询操作记录
	 */
	@Override
	public List<OperationRecord> selectOperationRecord(OperationRecordVO operVO) {
		return operationRecordMapper.selectByRecord(operVO);
	}
	

}
