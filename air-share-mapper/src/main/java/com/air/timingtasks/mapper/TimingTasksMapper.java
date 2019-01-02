package com.air.timingtasks.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.TimingTasks;

public interface TimingTasksMapper {
	/**
	 * 
	 * <p>Title: selectNotPerformedTask</p>
	 * <p>Description: 查询未执行的定时任务</p>
	 * @return
	 */
	public List<TimingTasks> selectNotPerformedTask();
	
	/**
	 * 
	 * <p>Title: insertTimingTasks</p>
	 * <p>Description: 添加定时任务</p>
	 * @param timingTasks
	 * @return
	 */
	public Integer insertTimingTasks(TimingTasks timingTasks);
	
	/**
	 * 
	 * <p>Title: updateTimingTasks</p>
	 * <p>Description: 修改定时任务</p>
	 * @param timingTasks
	 * @return
	 */
	public Integer updateTimingTasks(TimingTasks timingTasks);
	
	/**
	 * 
	 * <p>Title: deleteTimingTasks</p>
	 * <p>Description: 删除定时任务</p>
	 * @param mac
	 * @param userId
	 * @return
	 */
	public Integer deleteTimingTasks(@Param("mac") String mac,@Param("userId") Integer userId);
	/**
	 * 关机删除定时
	 * @param mac
	 * @param order
	 * @return
	 */
	public Integer deleteTiming(@Param("mac") String mac,@Param("order") String order); 
			
	
	public Integer modifyTimingTasks(TimingTasks timingTasks);
}
