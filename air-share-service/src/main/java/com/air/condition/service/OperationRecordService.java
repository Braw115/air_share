package com.air.condition.service;

import java.util.List;

import com.air.pojo.entity.OperationRecord;
import com.air.pojo.vo.OperationRecordVO;

public interface OperationRecordService {

	List<OperationRecord> selectOperationRecord(OperationRecordVO operVO);

}
