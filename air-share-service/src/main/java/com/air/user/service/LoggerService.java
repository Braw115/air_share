package com.air.user.service;

import com.air.pojo.entity.Logger;
import com.github.pagehelper.PageInfo;

public interface LoggerService {
	/**
	 * 
	 * <p>Title: selectLogger</p>
	 * <p>Description: 查询日志</p>
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageInfo<Logger> selectLogger(Integer pageNum, Integer pageSize);
	
	/**
	 * 
	 * <p>Title: delAllLogger</p>
	 * <p>Description: 清空日志</p>
	 * @return
	 */
	Integer delAllLogger();

}
