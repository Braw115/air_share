package com.air.interfacerecord.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.interfacerecord.mapper.InterfaceRecordMapper;
import com.air.interfacerecord.service.InterfaceRecordService;
import com.air.pojo.entity.InterfaceRecord;
import com.air.pojo.vo.InterfaceRecordVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class InterfaceRecordServiceImpl implements InterfaceRecordService {
	@Autowired
	private InterfaceRecordMapper iRecordMapper;
	
	/**
	 * 查询接口记录
	 */
	@Override
	public PageInfo selectInterfaceRecord(InterfaceRecordVO iRecordVO) {
		PageHelper.startPage(iRecordVO.getPageNum(), iRecordVO.getPageSize());
		List<InterfaceRecord> list = iRecordMapper.selectInterfaceRecord(iRecordVO);
		PageInfo<InterfaceRecord> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 通过ID删除接口记录
	 */
	@Override
	public Integer deleteInterfaceRecord(Integer iRecordId) {
		return iRecordMapper.deleteInterfaceRecord(iRecordId);
	}
	
	/**
	 * 添加接口记录
	 */
	@Override
	public Integer addInterfaceRecord(InterfaceRecord iRecord) {
		return iRecordMapper.addInterfaceRecord(iRecord);
	}
	
	/**
	 * 修改接口记录
	 */
	@Override
	public Integer updateInterfaceRecord(InterfaceRecord iRecord) {
		return iRecordMapper.updateInterfaceRecord(iRecord);
	}
	
	

}
