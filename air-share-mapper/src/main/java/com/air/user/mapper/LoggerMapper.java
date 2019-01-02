package com.air.user.mapper;

import java.util.List;

import com.air.pojo.entity.Logger;

public interface LoggerMapper {
	
	/**
	 * 
	 * <p>Title: selectLogger</p>
	 * <p>Description: 查询用户日志</p>
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<Logger> selectLogger();
	
	/**
	 * 
	 * <p>Title: addLogger</p>
	 * <p>Description: 添加日志 </p>
	 * @param logger
	 * @return
	 */
	public Integer addLogger(Logger logger);
	
	/**
	 * 
	 * <p>Title: delAllLogger</p>
	 * <p>Description: 清空日志</p>
	 * @return
	 */
	public Integer delAllLogger();

}
