package com.air.repair.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.condition.service.ConditionMqttService;
import com.air.condition.service.ConditionService;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.dispatch.mapper.DispatchMapper;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.Dispatch;
import com.air.pojo.entity.Evaluate;
import com.air.pojo.entity.Notice;
import com.air.pojo.entity.Repair;
import com.air.pojo.vo.BatchOperVO;
import com.air.pojo.vo.ControlVO;
import com.air.pojo.vo.EvaluateVO;
import com.air.pojo.vo.InformationVO;
import com.air.pojo.vo.RepairVO;
import com.air.redis.RedisAPI;
import com.air.repair.mapper.RepairMapper;
import com.air.repair.service.RepairService;
import com.air.timingtasks.mapper.TimingTasksMapper;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class RepairServiceImpl implements RepairService {
	
	@Autowired
	private RepairMapper repairMapper;
	
	@Autowired
	private AirConditionMapper airConditionMapper;
	
	@Autowired
	private RedisAPI redisApi;
	
	@Autowired 
	private DispatchMapper dispatchMapper;
	
	@Resource
	private NoticeService noticeService;
	@Autowired
	private ConditionService  conditionService;
	@Autowired
	private TimingTasksMapper timingTasksMapper;
	@Autowired
	private ConditionMqttService conditionMqttService;
	
	/**
	 * 根据空调ID查找维修记录
	 */
	@Override
	public List<Repair> selectRepairByCondition(String mac) {
		return repairMapper.selectRepairByCondition(mac);
	}
	
	/**
	 * 上报故障
	 */
	@Override
	public Integer addRepair(Repair repair) {
		AirCondition airCondition = airConditionMapper.selectAirConditionById(repair.getAirmac());
//		Boolean fault = airCondition.getFault();
		if(airCondition == null || airCondition.getFault()) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		
//		airCondition.setFault(true);
//		airConditionMapper.updateAirCondition(airCondition);
		
		return repairMapper.addRepair(repair);
	}
	
	/**
	 * 清理故障
	 */
	@Override
	public Integer maintenanceCondition(Repair repair) {
		repair.setService(false);
		Integer result = repairMapper.updateRepair(repair);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		Repair repair2 = repairMapper.selectRepairByRepairId(repair.getRepairId());
		
		AppUserAir appuserAir = new AppUserAir();
		appuserAir.setAppusersId(repair2.getUserId());
		appuserAir.setMac(redisApi.get(repair2.getUserId()+""));
		
		//删除定时
		timingTasksMapper.deleteTiming(repair.getAirmac(), "stop");
		//关闭空调
		ControlVO controlVO =new ControlVO();
		controlVO.setMac(appuserAir.getMac());
		controlVO.setOrder("power");
		try {
			conditionMqttService.controlAirCondition(controlVO, appuserAir.getAppusersId());
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
		redisApi.delete(repair2.getUserId()+"");
		//airConditionMapper.deleteUserAirByUserAndAir(appuserAir);
		
		return result;
	}

	
	/**
	 * 工单接收
	 */
	@Override
	public Integer repairReceive(Integer repairId) {
		Repair repair = repairMapper.selectRepairByRepairId(repairId);
		if(!SystemCode.REPAIR_ASSIGNED.equals(repair.getStatus())) {
			return SystemCode.ADD_UPDATE_FAILE;
		}
		
		repair.setStatus(SystemCode.REPAIR_PROCESSING);
		return repairMapper.updateRepair(repair);
	}
	
	/**
	 * App端根据id查询工单
	 * /
	 @Override
	public PageInfo<Repair> getRepairListByIds(RepairVO repair) {
		PageHelper.startPage(repair.getPageNum(), repair.getPageSize());
		List<Repair> list = repairMapper.selectRepairList(repair);
		PageInfo<Repair> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	 
	/**
	 * 工单列表
	 */
	@Override
	public PageInfo<Repair> getRepairList(RepairVO repair) {
		PageHelper.startPage(repair.getPageNum(), repair.getPageSize());
		List<Repair> list = repairMapper.selectRepairList(repair);
		PageInfo<Repair> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	
	
	/**
	 * 添加空调
	 * 1.确认密码
	 * 2.添加空调
	 */
	@Override
	public Integer addCondition(AirCondition airCondition, Integer userId) {
		// TODO 确认密码
		airCondition.setUseTime(new Date());
		airConditionMapper.addAirCondition(airCondition);
		return null;
	}
	
	/**
	 * 维修评价
	 */
	@Override
	public Integer addEvaluation(Evaluate evaluate) {
		Repair repair = repairMapper.selectRepairByRepairId(evaluate.getRepairId());
		Integer appuserId =  repair.getAppusersId();
		if(appuserId != evaluate.getAppusersId() && !appuserId.equals(evaluate.getAppusersId())) {
			return -1;
		}
		repair.setRepairId(evaluate.getRepairId());
		repair.setStatus(SystemCode.REPAIR_EVALUATED);
		repair.setComment(evaluate.getContent());
		repair.setStars(evaluate.getStars());
		repairMapper.updateRepair(repair);
		evaluate.setTechnicianId(repair.getUserId());
		return repairMapper.addEvaluation(evaluate);
	}
	
	/**
	 * 获取评价列表
	 */
	@Override
	public PageInfo<Evaluate> selectEvaluationList(EvaluateVO evaluate) {
		PageHelper.startPage(evaluate.getPageNum(), evaluate.getPageSize());
		List<Evaluate> list = repairMapper.selectEvaluationList(evaluate);
		PageInfo<Evaluate> pageInfo = new PageInfo<Evaluate>(list);
		if (evaluate.getTechnicianId() != null && pageInfo.getSize() >= 1) {
			List<Integer> evaluateIds = new ArrayList<>();
			for (Evaluate item : list) {
				evaluateIds.add(item.getEvaluateId());
			}
			repairMapper.evaluationReaded(evaluateIds);
		}
		return pageInfo;
	}
	
	
	/**
	 * 审批工单
	 */
	@Override
	public Integer approvalOrders(BatchOperVO batchOper,Integer repairId ) {
		List<Integer> repairIdList = batchOper.getList();
		String operation = batchOper.getOperation();
		String status = "pass".equals(operation)?SystemCode.REPAIR_PROCESSED:SystemCode.REPAIR_PROCESSING;
		//添加工单审核结果消息
		for(Integer repair_id: repairIdList) {
			Repair repair = repairMapper.selectRepairByRepairId(repairId);
			Notice notice = new Notice();
			notice.setAppusersId(repair.getUserId());
			notice.setTitle(SystemCode.REPAIR_APPROVAL_TITLE);
			if("pass".equals(operation)) {
				notice.setContent("您清理上报的工单号为"+repair_id+"的工单已经通过审核!");
			}else {
				notice.setContent("您清理上报的工单号为"+repair_id+"的工单未通过审核!");
			}
			
			noticeService.addNotice(notice);
		}
	
		Integer result = repairMapper.approvalOrders(repairIdList, status);
		//查询该空调是否还有未完成的单
		Repair repair  = getRepairByRepairId(repairId);
		Repair rep = new Repair();
		rep.setAirmac(repair.getAirmac());
		List<Repair> exsit =repairMapper.selectByMacAndStatus(rep);
		if(EmptyUtils.isEmpty(exsit)) {//更新空调的状态为未使用
			AirCondition airCondition =new AirCondition();
			airCondition.setMac(repair.getAirmac());
			airCondition.setFault(false);
			airConditionMapper.updateAirCondition(airCondition);
		}
		return result;
				
	}
	
	/**
	 * 获取用户消息
	 */
	@Override
	public InformationVO getInformationByUser(Integer userId) {
		InformationVO informationVO = new InformationVO();
		Integer newEvaluate = repairMapper.selectNewEvaluateByUser(userId);
		informationVO.setEvaluateNews(newEvaluate);
		Integer newRepair = repairMapper.selectNewRepairByUser(userId);
		informationVO.setRepairNews(newRepair);
		return informationVO;
	}
	
	/**
	 * 获取空调故障记录
	 */
	@Override
	public PageInfo<RepairVO> getConditionRepair(Integer appUserId,String mac, String status, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<RepairVO> list = repairMapper.getConditionRepair(appUserId,mac, status);
		PageInfo<RepairVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 维修授权
	 */
	@Override
	public String repairAuthorization(Integer repairId, Integer appUserId) {
		Repair repair = repairMapper.selectRepairByRepairId(repairId);
		if (SystemCode.REPAIR_PROCESSED.equals(repair.getStatus()) || SystemCode.REPAIR_EVALUATED.equals(repair.getStatus())) {
			return "该工单已维修完成, 无法授权";
		}
		
		if (repair.getAuthorization()) {
			return "该工单已授权过, 无法授权, 请联系客服";
		}
		
		if (repair.getUserId() != appUserId) {
			return "工单授权权限不足";
		}
		
		if (redisApi.exist(appUserId+"")) {
			return "用户已授权过";
		}
		
		//维修人员绑定空调
		AppUserAir appuserAir = new AppUserAir();
		appuserAir.setAppusersId(appUserId);
		appuserAir.setMac(repair.getAirmac());
		AppUserAir exist = airConditionMapper.selectAirConditionByIdAndUser(appuserAir.getMac(), appuserAir.getAppusersId());
		if(EmptyUtils.isEmpty(exist)) {
			airConditionMapper.addAppUserAir(appuserAir);
		}
		//授权两小时
		redisApi.set(appUserId+"",7200, repair.getAirmac().toString());
		repair.setAuthorization(true);
		repair.setService(true);
		repairMapper.updateRepair(repair);
		return "授权成功";
	}

	@Override
	public String cancelAuthorization(Integer appUserId) {
		AppUserAir appuserAir = new AppUserAir();
		appuserAir.setAppusersId(appUserId);
		appuserAir.setMac(redisApi.get(appUserId+""));
		ConditionInfo conditionInfo = airConditionMapper.getConditionInfoByMac(appuserAir.getMac());
		//airConditionMapper.deleteUserAirByUserAndAir(appuserAir);
		//解除绑定并关闭空调
		Integer result;
		try {
			 result = conditionService.releaseBindingByCrmUser(appuserAir);
		} catch (MqttException e) {
			e.printStackTrace();
			return "取消授权失败";
		}
		if(result==0) {
			return "取消授权失败";
		}
		redisApi.delete(appUserId+"");
		return "取消授权成功";
	}
	
	/**
	 * 重新授权
	 */
	@Override
	public Dto reauthorization(Integer repairId) {
		try {
			Repair repair = repairMapper.selectRepairByRepairId(repairId);
			if (!SystemCode.REPAIR_PROCESSING.equals(repair.getStatus())) {
				return DtoUtil.returnFail("无法授权,不在处理中的空调", "403");
			}
			
			if (!repair.getAuthorization()) {
				return DtoUtil.returnFail("该工单无需重新授权", "403");
			}
			AppUserAir appUserAir = airConditionMapper.selectAppUserAirByUseIdAndMac(repair.getUserId(),repair.getAirmac());
			if(EmptyUtils.isEmpty(appUserAir)) {
				 
				return DtoUtil.returnFail("授权失败,维修人员已解绑该空调", "403");
			}
			redisApi.set(repair.getUserId()+"", 7200, repair.getAirmac());
			repair.setAuthorization(true);
			repair.setService(true);
			repair.setAuthorization(false);
			repairMapper.updateRepair(repair);
			return DtoUtil.returnSuccess("重新授权成功","200") ;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return DtoUtil.returnFail("重新授权失败", "403");
		}
	}
	
	
	/**
	 * 指派工单
	 */
	@Override
	public Boolean dispatchOrder(List<Dispatch> dispatchlist) {
		String mac = repairMapper.selectRepairByRepairId(dispatchlist.get(0).getRepairId()).getAirmac();
		AirCondition airCondition = airConditionMapper.selectAirConditionById(mac);
//		Boolean fault = airCondition.getFault();
		if(airCondition == null) {
			return false;
		}
		
		airCondition.setFault(true);
		airConditionMapper.updateAirCondition(airCondition);
		Boolean result = dispatchMapper.insertDispatch(dispatchlist);
			return result;

	}
	/**
	 * 抢单,即更新userId和单的状态为processing
	 */
	@Override
	public Integer assignedOrders(Repair repair) {
		//限制抢单,如果有未完成订单则不能抢单
		Repair rep = new Repair();
		rep.setUserId(repair.getUserId());
		Repair exist = repairMapper.selectByUserIdAndStatus(repair);
		if(!EmptyUtils.isEmpty(exist) && exist.getRepairId()!=null) {
			return -1;
		}
		
		Repair dbrepair = repairMapper.selectRepairByRepairId(repair.getRepairId());

		if(EmptyUtils.isEmpty(repair)) {
			return 0;
		}else {
			if(dbrepair.getUserId() != null || !dbrepair.getStatus().equals("assigned")) {
				return 0;
			}else {
				Integer result =  repairMapper.updateUserId(repair);
				Integer res = dispatchMapper.delDispatchByRepairId(repair.getRepairId());
				if(result!=null && res != null) {
					return result;
				}else {
					return 0;
				}
			}
		}
	}
	
	
	/**
	 * 查询可抢单
	 */
	@Override
	public PageInfo<Repair> CanRobbing(Integer userId,Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Repair> repairList = dispatchMapper.selectByUserId(userId);
		PageInfo pageInfo = new PageInfo(repairList);
		return pageInfo;
	}
	/**
	 * 更新工单
	 */

	@Override
	public Integer updateRepair(Repair repair) {
		return repairMapper.updateRepair(repair);
	}

	@Override
	public Repair getRepairByRepairId(Integer repairId) {
		 return repairMapper.selectRepairByRepairId(repairId);
		
	}	
}
