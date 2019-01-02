package com.air.repair.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.air.aspect.record;
import com.air.condition.service.ConditionMqttService;
import com.air.condition.service.ConditionService;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.CrmUserService;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Dispatch;
import com.air.pojo.entity.Evaluate;
import com.air.pojo.entity.Notice;
import com.air.pojo.entity.Repair;
import com.air.pojo.vo.BatchOperVO;
import com.air.pojo.vo.ControlVO;
import com.air.pojo.vo.EvaluateVO;
import com.air.pojo.vo.InformationVO;
import com.air.pojo.vo.RepairVO;
import com.air.pojo.vo.RespVo;
import com.air.redis.RedisAPI;
import com.air.repair.service.RepairService;
import com.air.user.service.AppUserService;
import com.air.utils.DateFormats;
import com.air.utils.FileUploader;
import com.github.pagehelper.PageInfo;

/**
 * 
 * <p>Title: RepairController</p>
 * <p>Description: 工单模块Controller </p>
 * @author czg
 * @date 2018年6月11日
 */
@Controller
@RequestMapping("/repair")
public class RepairController {
	@Autowired
	private RepairService repairService;
	@Autowired
	private RedisAPI redisAPI;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private CrmUserService crmUserService;
	@Resource
	private NoticeService noticeService;
	@Autowired
	private ConditionMqttService conditionMqttService;
	@Autowired 
	private ConditionService conditionService;
	

	/**
	 * 
	 * @description 根据空调ID查找维修记录
	 * @author czg
	 * @param getRepairByCondition
	 * @return
	 */
	@RequestMapping(value = "/getRepairByCondition", method = RequestMethod.GET)
	@ResponseBody
	public Dto<List<Repair>> getRepairByCondition(AirCondition airCondition) {
		String mac = airCondition.getMac();
		if (StringUtils.isEmpty(mac)) {
			return DtoUtil.returnFail("MAC不能为空", SystemCode.ERROR);
		}
		
		List<Repair> result = repairService.selectRepairByCondition(mac);
		return new Dto<List<Repair>>(200, "查找成功", result);

	}

	/**
	 * 
	 * <p>Title: addRepair</p>
	 * <p>Description: 上报故障</p>
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/addRepair", method = RequestMethod.POST)
	@ResponseBody
	public Dto<Repair> addRepair(HttpServletRequest request,Repair repair, @RequestParam("uploadfile") List<MultipartFile> file) {
		
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		
		if (StringUtils.isEmpty(repair.getAirmac().trim())) {
			return DtoUtil.returnFail("空调MAC不能为空", SystemCode.ERROR);
		}

		if (StringUtils.isEmpty(repair.getType().trim())) {
			return DtoUtil.returnFail("故障类型不能为空", SystemCode.ERROR);
		}

		if (StringUtils.isEmpty(repair.getContent().trim())) {
			return DtoUtil.returnFail("故障描述不能为空", SystemCode.ERROR);
		}

		RespVo<String> upload = FileUploader.upload(file);
		repair.setFailPicture((String)upload.getMsg());
		repair.setStatus(SystemCode.REPAIR_PENDING);
		repair.setAppusersId(appusersId);
		Integer result = repairService.addRepair(repair);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("空调不存在或该空调已提交故障", SystemCode.ERROR);
		}

		return new Dto<Repair>(200, "报障成功");
	}

	/**
	 * 
	 * <p>Title: maintenanceCondition</p>
	 * <p>Description: 提交故障清理结果</p>
	 * 
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/maintenanceCondition", method = RequestMethod.POST)
	@ResponseBody
	public Dto<String> maintenanceCondition(Repair repair, @RequestParam("uploadfile") List<MultipartFile> file) {
		if (repair.getRepairId() == null) {
			return new Dto<String>(400, "空调故障记录ID不能为空");
		}

		if (StringUtils.isEmpty(repair.getDetail().trim())) {
			return new Dto<String>(400, "清理描述不能为空");
		}

		if (StringUtils.isEmpty(repair.getResult().trim())) {
			return new Dto<String>(400, "处理结果不能为空");
		}
		
		RespVo<String> upload = FileUploader.upload(file);
		repair.setRepairPicture(upload.getMsg());
		repair.setRepairTime(new Date());
		repair.setStatus(SystemCode.REPAIR_CKECKING);
		Integer result = repairService.maintenanceCondition(repair);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("维修提交失败", SystemCode.ERROR);
		}

		return new Dto<String>(200, "提交成功");
	}

	/**
	 * 
	 * <p>Title: repairReceive</p>
	 * <p>Description: 工单接收</p>
	 * 
	 * @param repairId
	 * @return
	 */
	/*@RequestMapping(value = "/repairReceive", method = RequestMethod.POST)
	@ResponseBody
	public Dto<String> repairCheckIn(@RequestBody Repair repair) {
		Integer repairId = repair.getRepairId();
		if (repairId == null) {
			return DtoUtil.returnFail("维修ID不能为空", SystemCode.ERROR);
		}
		
		Integer result = repairService.repairReceive(repairId);
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("接收失败", SystemCode.ERROR);
		}

		return DtoUtil.returnSuccess();
	}*/
	
	/**
	 * 
	 * <p>Title: getRepairList</p>
	 * <p>Description: App端获取工单列表</p>
	 * <p>Description:根据工单状态查询,REPAIR_PENDING为可抢单</p>
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/getRepairById", method = RequestMethod.GET)
	@ResponseBody
	public Dto getRepairById(HttpServletRequest request, Integer repairId) {
		Repair repair = repairService.getRepairByRepairId(repairId);
		return  DtoUtil.returnDataSuccess(repair);
	}
	
	/**
	 * 
	 * <p>Title: getRepairList</p>
	 * <p>Description: App端获取工单列表</p>
	 * <p>Description:根据工单状态查询,REPAIR_PENDING为可抢单</p>
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/getRepairList", method = RequestMethod.GET)
	@ResponseBody
	public Dto<PageInfo<Repair>> getRepairList(HttpServletRequest request, RepairVO repair) {
		String token = request.getHeader("token");
		if (TokenUtil.respAppReturn(token)) {
			repair.setUserId(TokenUtil.getAppUserId(token));
		}
		
		if (!StringUtils.isEmpty(repair.getBeginDate()) && !DateFormats.isValidDate(repair.getBeginDate())) {
			return DtoUtil.returnFail("开始时间格式错误", SystemCode.ERROR);
		}
		
		if (!StringUtils.isEmpty(repair.getEndDate()) && !DateFormats.isValidDate(repair.getEndDate())) {
			return DtoUtil.returnFail("结束时间格式错误", SystemCode.ERROR);
		}
		
		if (repair.getPageNum() == null || repair.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		
		String status = repair.getStatus();
		PageInfo<Repair> result;
		if (!StringUtils.isEmpty(status) && status.equals(SystemCode.REPAIR_PENDING) 
				&& status.equals(SystemCode.REPAIR_PROCESSED )
				&& status.equals(SystemCode.REPAIR_PROCESSING)
				&& status.equals(SystemCode.REPAIR_CKECKING)
				&& status.equals(SystemCode.REPAIR_ASSIGNED)) {
			return new Dto<>(400, "选择的工单状态有误");
		}
		if(status.equals("assigned")){
			result = repairService.CanRobbing(repair.getUserId(), repair.getPageNum(), repair.getPageSize());
		}else {
			result = repairService.getRepairList(repair);
		}
		 	
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * <p>Title: getRepairList</p>
	 * <p>Description: Crm端获取工单列表</p>
	 * @param repair
	 * @return
	 */
	@RequestMapping(value = "/getRepairListCrm", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"queryRepairOrder"})
	public Dto<PageInfo<Repair>> getRepairListCrm(HttpServletRequest request, RepairVO repair) {
		String token = request.getHeader("token");
		
		if (!StringUtils.isEmpty(repair.getBeginDate()) && !DateFormats.isValidDate(repair.getBeginDate())) {
			return DtoUtil.returnFail("开始时间格式错误", SystemCode.ERROR);
		}
		
		if (!StringUtils.isEmpty(repair.getEndDate()) && !DateFormats.isValidDate(repair.getEndDate())) {
			return DtoUtil.returnFail("结束时间格式错误", SystemCode.ERROR);
		}
		
		if (repair.getPageNum() == null || repair.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		
		String status = repair.getStatus();
		if (!StringUtils.isEmpty(status) && status.equals(SystemCode.REPAIR_PENDING) 
				&& status.equals(SystemCode.REPAIR_PROCESSED )
				&& status.equals(SystemCode.REPAIR_PROCESSING)
				&& status.equals(SystemCode.REPAIR_CKECKING)
				&& status.equals(SystemCode.REPAIR_ASSIGNED)) {
			return new Dto<>(400, "选择的工单状态有误");
		}
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.hasRole("root")) {
			repair.setPartnerId(null);
		}else if(subject.hasRole("admin")){
			CrmUser user =new CrmUser();
			user.setCrmuserId(TokenUtil.getCrmUserId(token));
			CrmUser crmUser = crmUserService.queryCrmUser(user);
			repair.setAreaId(crmUser.getAreaId());
		}else if(subject.hasRole("partner")){
			repair.setPartnerId(TokenUtil.getCrmUserId(token));
		}else {
			repair.setPartnerId(null);
//			return DtoUtil.returnFail("没有权限", SystemCode.ERROR);
		}
		PageInfo<Repair> result = repairService.getRepairList(repair);
		return DtoUtil.returnDataSuccess(result);
	}

	/*
	@RequestMapping(value = "/bindingCondition", method = RequestMethod.POST)
	@ResponseBody
	public Dto<String> bindingCondition(AirCondition airCondition, @RequestParam("userId") Integer userId) {
		if(StringUtils.isEmpty(airCondition.getMac().trim())) {
			return new Dto<>(400, "请输入空调MAC地址");
		}
		
		if(airCondition.getLatitude() == null || airCondition.getLongitude() == null) {
			return new Dto<>(400, "请获取空调的位置");
		}
		
		if(airCondition.getEnterpriseId() == null) {
			return new Dto<>(400, "请选择业主");
		}
		
		if(StringUtils.isEmpty(airCondition.getLocation().trim())) {
			return new Dto<>(400, "请输入位置信息");
		}
		
		if(airCondition.getSeriesId() == null) {
			return new Dto<>(400, "请选择空调系列号");
		}
		
		Integer result = repairService.addCondition(airCondition, userId);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return new Dto<>(200, "密码错误");
		}
		
		return new Dto<>(200, "添加成功");
	}
	*/
	
	/**
	 * 
	 * <p>Title: addEvaluation</p>
	 * <p>Description: 添加评价 </p>
	 * @param evaluate
	 * @return
	 */
	@RequestMapping(value = "/addEvaluation", method = RequestMethod.POST)
	@ResponseBody
	public Dto<String> addEvaluation(HttpServletRequest request, @RequestBody Evaluate evaluate) {
		evaluate.setAppusersId(TokenUtil.getAppUserId(request.getHeader("token")));
		
		if(evaluate.getStars() == null) {
			return DtoUtil.returnFail("请为技术员打分", SystemCode.ERROR);
		}

		if(evaluate.getRepairId() == null) {
			return DtoUtil.returnFail("请选择维修记录", SystemCode.ERROR);
		}
		
		if(StringUtils.isEmpty(evaluate.getContent().trim())) {
			return DtoUtil.returnFail("请输入评价内容", SystemCode.ERROR);
		}
		
		Integer result = repairService.addEvaluation(evaluate);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("评价失败", SystemCode.ERROR);
		}
		if(result == -1) {
			return DtoUtil.returnFail("评价失败,只有报单人才能评价!", SystemCode.ERROR);
		}
	
		return DtoUtil.returnSuccess("评价成功!");
	}
	
	/**
	 * 
	 * <p>Title: getEvaluationList</p>
	 * <p>Description: 获取评价列表 </p>
	 * @param evaluate
	 * @return
	 */
	@RequestMapping(value = "/getEvaluationList", method = RequestMethod.GET)
	@ResponseBody
	public Dto<PageInfo<Evaluate>> getEvaluationList(HttpServletRequest request, EvaluateVO evaluate) {
		if (evaluate.getPageNum() == null || evaluate.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数错误", SystemCode.ERROR);
		}
		String token = request.getHeader("token");
		if (TokenUtil.respAppReturn(token)) {
			evaluate.setTechnicianId(TokenUtil.getAppUserId(token));
		}
		
		PageInfo<Evaluate> result = repairService.selectEvaluationList(evaluate);
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * <p>Title: dispatchOrder</p>
	 * <p>descript:指派工单<p>
	 */
	@RequestMapping(value = "/dispatchOrder", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"dispatchOrder"})
	public Dto<String> dispatchOrder(@RequestBody List<Dispatch> dispatchList){

		Repair repair = new Repair();
		repair.setRepairId(dispatchList.get(0).getRepairId());
		repair.setStatus(SystemCode.REPAIR_ASSIGNED);
		repair.setDispatchTime(new Date());
		Integer res  =repairService.updateRepair(repair);
		if (res == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("指派失败", SystemCode.ERROR);
		}
		boolean result = repairService.dispatchOrder(dispatchList);
		if(result) {
			//添加派单消息
			for(Dispatch dispatch :dispatchList) {
				Notice notice = new Notice();
				notice.setAppusersId(dispatch.getUserId());
				notice.setTitle(SystemCode.REPAIR_TITLE);
				notice.setContent(SystemCode.REPAIR_CONTENT);
				noticeService.addNotice(notice);
			}
			return new Dto<>(200,"指派成功");
		}else{
			repair.setStatus(SystemCode.REPAIR_PENDING);
			repairService.updateRepair(repair);
			return DtoUtil.returnFail("指派失败", SystemCode.ERROR);
		}
		
	}
	
	/**
	 * 
	 * <p>Title: assignedOrders</p>
	 * <p>Description: 抢单</p>
	 * @param evaluate
	 * @return
	 */
	@record(businessLogic="抢单")
	@RequestMapping(value = "/assignedOrders", method = RequestMethod.PUT)
	@ResponseBody
	public Dto<String> assignedOrders(@RequestBody Repair repair) {//抢单
		if (repair.getRepairId() == null) {
			return DtoUtil.returnFail("请选择工单", SystemCode.ERROR);
		}
		
		if (repair.getUserId() == null) {
			return DtoUtil.returnFail("请选择指派的技术人员", SystemCode.ERROR);
		}
		
		repair.setStatus(SystemCode.REPAIR_PROCESSING);
		//repair.setDispatchTime(new Date());
		Integer result = repairService.assignedOrders(repair);
		
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("抢单失败,该单已被抢", SystemCode.ERROR);
		}
		if (result == -1) {
			return DtoUtil.returnFail("抢单失败,您有未完成的订单!", SystemCode.ERROR);
		}
		//授权	
//		String msg = repairService.repairAuthorization(repair.getRepairId(), repair.getUserId());
//		return DtoUtil.returnSuccess(msg);
		return DtoUtil.returnSuccess();
	}
	
	/**
	 * 
	 * <p>Title: approvalOrders</p>
	 * <p>Description: 审批工单</p>
	 * @param repair
	 * @return
	 */
	@record(businessLogic="审批工单")
	@RequestMapping(value = "/approvalOrders", method = RequestMethod.PUT)
	@ResponseBody
	@RequiresPermissions(value= {"auditingRepairOrder"})
	public Dto<String> approvalOrders(@RequestBody BatchOperVO batchOper) {
		List<Integer> repairIdList = batchOper.getList();
		if (repairIdList == null || repairIdList.isEmpty()) {
			return DtoUtil.returnFail("请选择工单", SystemCode.ERROR);
		}
		Integer repairId = repairIdList.get(0);
		Integer result = repairService.approvalOrders(batchOper,repairId);
		
		if (result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("审批失败!", SystemCode.ERROR);
		}
		return DtoUtil.returnSuccess();
	}
	
	/**
	 * 
	 * <p>Title: getInformation</p>
	 * <p>Description: 获取维修人员消息的条数</p>
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getInformation", method = RequestMethod.GET)
	@ResponseBody
	public Dto<InformationVO> getInformation(HttpServletRequest request) {
		String token = request.getHeader("token");
		Integer userId = TokenUtil.getAppUserId(token);
		InformationVO informationVO = repairService.getInformationByUser(userId);
		return DtoUtil.returnDataSuccess(informationVO);
	}
	
	/**
	 * <p>Title: getConditionRepair</p>
	 * <p>Description: 查询空调的故障信息,报障记录</p>
	 * @param mac
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/getConditionRepair", method = RequestMethod.GET)
	@ResponseBody
	public Dto<InformationVO> getConditionRepair(HttpServletRequest request,@RequestParam("mac")String mac, @RequestParam("status")String status, @RequestParam("pageNum")Integer pageNum, @RequestParam("pageSize")Integer pageSize) {
		AppUser appUser = TokenUtil.getAppUser(request.getHeader("token"));
		Integer appusersId = appUser.getAppusersId();
		if ("T".equals(appUser.getPerm())&&"yes".equals(appUser.getReviewStatus())) {
			appusersId = 0;
		}
		PageInfo<RepairVO> pageinfo = repairService.getConditionRepair(appusersId,mac, status, pageNum, pageSize);
		return DtoUtil.returnDataSuccess(pageinfo);
	}
	
	/**
	 * <p>Title: repairAuthorization</p>
	 * <p>Description: 维修授权</p>
	 * @param request
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/repairAuthorization", method = RequestMethod.PUT)
	@ResponseBody
	public Dto repairAuthorization(HttpServletRequest request, @RequestBody RepairVO repair) {
		if (repair.getRepairId() == null) {
			return DtoUtil.returnFail("请选择工单", SystemCode.ERROR);
		}
		AppUser appUser = (AppUser) appUserService.queryAppUserById(TokenUtil.getAppUserId(request.getHeader("token"))).getData();
		
		if (repair.getPassword() != null && !repair.getPassword().equals(appUser.getPassword())) {
			return DtoUtil.returnFail("维修人员密码错误", SystemCode.ERROR);
		}
		
		String msg = repairService.repairAuthorization(repair.getRepairId(), appUser.getAppusersId());
		Repair dbRepair = repairService.getRepairByRepairId(repair.getRepairId());
		//定时两小时后关闭空调
		ControlVO controlVO =new ControlVO();
		controlVO.setMac(dbRepair.getAirmac());
		controlVO.setOrder(SystemCode.AIR_CONDITION_ORDER_TIMING);
		controlVO.setValue(7200);
		try {
			Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
			conditionMqttService.controlAirCondition(controlVO, appuserId);
		}catch(Exception e) {
			e.printStackTrace();
		}

		return DtoUtil.returnSuccess(msg);
	}
	
	
	/**
	 * <p>Title: cancelAuthorization</p>
	 * <p>Description: 取消授权</p>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/cancelAuthorization", method = RequestMethod.PUT)
	@ResponseBody
	public Dto cancelAuthorization(HttpServletRequest request) {
		Integer appUserId = TokenUtil.getAppUserId(request.getHeader("token"));
		String msg = repairService.cancelAuthorization(appUserId);
//		redisAPI.delete(appUserId+"");
		return DtoUtil.returnSuccess(msg);
	}
	
	/**
	 * <p>Title: reauthorization</p>
	 * <p>Description: 重新授权</p>
	 * @param request
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/reauthorization", method = RequestMethod.POST)
	@ResponseBody
	@record(businessLogic="工单重新授权")
	@RequiresPermissions(value= {"redelegation"})
	public Dto reauthorization(HttpServletRequest request, @RequestBody Repair repair) {
		if (repair.getRepairId() == null) {
			return DtoUtil.returnFail("请选择工单", SystemCode.ERROR);
		}
		Repair dbRepair = repairService.getRepairByRepairId(repair.getRepairId());
		Dto<String> dto = repairService.reauthorization(repair.getRepairId());
		//定时两小时后关闭空调
		ControlVO controlVO =new ControlVO();
		controlVO.setMac(dbRepair.getAirmac());
		controlVO.setOrder(SystemCode.AIR_CONDITION_ORDER_TIMING);
		controlVO.setValue(7200);
		try {
			Integer appuserId = dbRepair.getUserId();
			conditionMqttService.controlAirCondition(controlVO, appuserId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
