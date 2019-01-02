package com.air.repair.service;

import java.util.List;

import com.air.constant.Dto;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.Dispatch;
import com.air.pojo.entity.Evaluate;
import com.air.pojo.entity.Repair;
import com.air.pojo.vo.BatchOperVO;
import com.air.pojo.vo.EvaluateVO;
import com.air.pojo.vo.InformationVO;
import com.air.pojo.vo.RepairVO;
import com.github.pagehelper.PageInfo;

public interface RepairService {
	
	/**
	 * 
	 * <p>Title: selectRepairByCondition</p>
	 * <p>Description: 根据空调ID查找维修记录</p>
	 * @param mac
	 * @return
	 */
	public List<Repair> selectRepairByCondition(String mac);
	
	/**
	 * 
	 * <p>Title: addRepair</p>
	 * <p>Description: 上报故障</p>
	 * @param repair
	 * @return
	 */
	public Integer addRepair(Repair repair);

	/**
	 * 
	 * <p>Title: maintenanceCondition</p>
	 * <p>Description: 清理故障</p>
	 * @param repair
	 * @return
	 */
	public Integer maintenanceCondition(Repair repair);
	
	/**
	 * 
	 * <p>Title: repairReceive</p>
	 * <p>Description: 工单接收</p>
	 * @param repairId
	 * @return
	 */
	public Integer repairReceive(Integer repairId);
	
	/**
	 * 
	 * <p>Title: getRepairList</p>
	 * <p>Description: 获取工单列表</p>
	 * @param repair
	 * @return
	 */
	public PageInfo<Repair> getRepairList(RepairVO repair);
	
	
	/**
	 * 
	 * <p>Title: addCondition</p>
	 * <p>Description: 添加一台新空调</p>
	 * @param airCondition
	 * @param userId
	 * @return
	 */
	public Integer addCondition(AirCondition airCondition, Integer userId);
	
	/**
	 * 
	 * <p>Title: addEvaluation</p>
	 * <p>Description: 维修评价</p>
	 * @param evaluate
	 * @return
	 */
	public Integer addEvaluation(Evaluate evaluate);
	
	/**
	 * <p>Title: selectEvaluationList</p>
	 * <p>Description: 获取评价列表 </p>
	 * @param evaluate
	 * @return 
	 */
	public PageInfo<Evaluate> selectEvaluationList(EvaluateVO evaluate);
	
	/**
	 * <p>Title:dispatchOrder</p>
	 * <p>Description: 工单指派</p>
	 */
	public Boolean dispatchOrder(List<Dispatch> dispatchList);
	/**
	 * <p>Title:dispatchOrder</p>
	 * <p>Description: 根据userId查询可抢单</p> 
	 */
	public PageInfo<Repair> CanRobbing (Integer userId,Integer pageNum, Integer pageSize);
	/**
	 * 
	 * <p>Title: assignedOrders</p>
	 * <p>Description: 抢单</p>
	 * @param repair
	 * @return
	 */
	public Integer assignedOrders(Repair repair);
	
	/**
	 * 
	 * <p>Title: approvalOrders</p>
	 * <p>Description: 审批工单</p>
	 * @param repairIdList
	 * @return
	 */
	public Integer approvalOrders(BatchOperVO batchOper,Integer repairId);
	
	/**
	 * 
	 * <p>Title: getInformationByUser</p>
	 * <p>Description: 获取用户的消息</p>
	 * @param userId
	 * @return
	 */
	public InformationVO getInformationByUser(Integer userId);
	
	/**
	 * 
	 * <p>Title: getConditionRepair</p>
	 * <p>Description: 获取空调的故障列表</p>
	 * @param mac
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageInfo<RepairVO> getConditionRepair(Integer appUsersId,String mac, String status, Integer pageNum, Integer pageSize);
	
	/**
	 * 
	 * <p>Title: repairAuthorization</p>
	 * <p>Description: 维修授权</p>
	 * @param mac
	 * @param appUserId
	 * @return
	 */
	public String repairAuthorization(Integer repairId, Integer appUserId);
	
	/**
	 * 
	 * <p>Title: cancelAuthorization</p>
	 * <p>Description: 取消授权</p>
	 * @param appUserId
	 * @return
	 */
	public String cancelAuthorization(Integer appUserId);
	
	/**
	 * <p>Title: reauthorization</p>
	 * <p>Description: 重新授权</p>
	 * @param repairId
	 * @param appUserId
	 * @return
	 */
	public Dto reauthorization(Integer repairId);
	
	/**
	 * 更新工单
	 */
	public Integer updateRepair(Repair repair);
	/**
	 * 根据工单号查询工单
	 */
	public Repair getRepairByRepairId(Integer repairId);
}
