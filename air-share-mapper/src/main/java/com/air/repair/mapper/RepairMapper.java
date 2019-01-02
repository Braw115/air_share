package com.air.repair.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Evaluate;
import com.air.pojo.entity.Repair;
import com.air.pojo.vo.EvaluateVO;
import com.air.pojo.vo.RepairVO;

public interface RepairMapper {
	
	/**
	 * 
	 * <p>Title: selectRepairByCondition</p>
	 * <p>Description: 根据空调ID查找维修记录</p>
	 * @param mac
	 * @return
	 */
	List<Repair> selectRepairByCondition(String mac);
	
	/**
	 * 
	 * <p>Title: addRepair</p>
	 * <p>Description: 添加故障记录</p>
	 * @param repair
	 * @return 维修记录ID
	 */
	Integer addRepair(Repair repair);
	
	/**
	 * 
	 * <p>Title: updateRepair</p>
	 * <p>Description: 修改维修记录</p>
	 * @param repair
	 * @return
	 */
	Integer updateRepair(Repair repair);
	
	/**
	 * 
	 * <p>Title: selectRepairByRepairId</p>
	 * <p>Description: 根据维修ID查找该条维修记录</p>
	 * @param repairId
	 * @return
	 */
	Repair selectRepairByRepairId(Integer repairId);
	
	/**
	 * 
	 * <p>Title: selectRepairList</p>
	 * <p>Description: 获取工单列表</p>
	 * @param repair
	 * @return
	 */
	List<Repair> selectRepairList(RepairVO repair);
	
	
	/**
	 * 
	 * <p>Title: addEvaluation</p>
	 * <p>Description: 添加评价</p>
	 * @param evaluate
	 * @return
	 */
	Integer addEvaluation(Evaluate evaluate);
	
	/**
	 * 
	 * <p>Title: selectEvaluationList</p>
	 * <p>Description: 获取评价列表</p>
	 * @param evaluate
	 * @return
	 */
	List<Evaluate> selectEvaluationList(EvaluateVO evaluate);
	
	/**
	 * 
	 * <p>Title: approvalOrders</p>
	 * <p>Description: 审批工单</p>
	 * @param repairIdList
	 * @param repairProcessed
	 * @return
	 */
	Integer approvalOrders(@Param("repairIdList")List<Integer> repairIdList, @Param("repairProcessed")String repairProcessed);
	
	/**
	 * 
	 * <p>Title: evaluationReaded</p>
	 * <p>Description: 把查询过的评论修改成已阅读</p>
	 * @param evaluateIds
	 * @return
	 */
	Integer evaluationReaded(@Param("evaluateIds")List<Integer> evaluateIds);
	
	/**
	 * 
	 * <p>Title: selectNewEvaluateByUser</p>
	 * <p>Description: 获取新评论的条数</p>
	 * @param userId
	 * @return
	 */
	Integer selectNewEvaluateByUser(Integer userId);
	
	/**
	 * 
	 * <p>Title: selectNewRepairByUser</p>
	 * <p>Description: 获取未接收的维修记录</p>
	 * @param userId
	 * @return
	 */
	Integer selectNewRepairByUser(Integer userId);
	
	/**
	 * <p>Title: getConditionRepair</p>
	 * <p>Description: 根据空调获取故障记录</p>
	 * @param mac
	 * @param status
	 * @return
	 */
	List<RepairVO> getConditionRepair(@Param("appUserId")Integer appUserId,@Param("mac") String mac, @Param("status") String status);
	
	/**
	 * <p>Title: getConditionRepair</p>
	 * <p>Description:抢单设置userId和status </p>
	 */
	Integer updateUserId(Repair repair);
	/**
	 <p>Title: selectByUserIdAndStatus</p>
	 * <p>Description:查询维修人员是否有未完成工单 </p>
	 */
	Repair selectByUserIdAndStatus(Repair repair);
	/**
	 <p>Title: selectByUserIdAndStatus</p>
	 * <p>Description:根据mac地址,查询该空调是否有未完成的单 </p>
	 */
	List<Repair> selectByMacAndStatus(Repair repair);
}
