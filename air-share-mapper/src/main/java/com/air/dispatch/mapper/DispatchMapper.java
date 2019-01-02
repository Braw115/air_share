package com.air.dispatch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Dispatch;
import com.air.pojo.entity.Repair;

/**
 * 派单mapper
 * @author lxl
 *
 */

public interface DispatchMapper {
	/**
	 * 插入派单记录
	 * @param dispatch
	 * @return
	 */
	Boolean insertDispatch(@Param("list") List<Dispatch> list);
	/**
	 * 根据id查找派单记录
	 * @param userId
	 * @return
	 */
	 List<Repair> selectByUserId(@Param("userId") Integer userId);
	 /**
	  * 删除派单记录
	  */
	Integer delDispatchByRepairId(@Param("repairId") Integer repairId);
}
