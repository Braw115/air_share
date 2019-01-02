package com.air.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.pojo.entity.Logger;
import com.air.user.mapper.LoggerMapper;
import com.air.user.service.LoggerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class LoggerServiceImpl implements LoggerService {
	
	@Autowired
	private LoggerMapper loggerMapper;
	/**
	 * 查询日志
	 */
	@Override
	public PageInfo<Logger> selectLogger(Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Logger> list = loggerMapper.selectLogger(); 
		return new PageInfo<>(list);
	}
	/**
	 * 清空日志
	 */
	@Override
	public Integer delAllLogger() {
		
		return loggerMapper.delAllLogger();
	}

}
