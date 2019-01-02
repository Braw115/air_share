package com.air.condition.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.BeanUtils;
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
import com.air.constant.ErrorCode;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.CrmUserService;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Notice;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.vo.AirConditionVO;
import com.air.pojo.vo.AppUserAirVo;
import com.air.pojo.vo.BindingVO;
import com.air.pojo.vo.ControlVO;
import com.air.redis.RedisAPI;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.air.utils.MapDistance;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

import cn.jiguang.common.utils.StringUtils;

/**
 * 
 * <p>Title: ConditionController</p>
 * <p>Description: 空调模块Controller</p>
 * @author czg
 * @date 2018年6月11日
 */
@Controller
@RequestMapping("/condition")
public class ConditionController {
	@Autowired
	private ConditionService conditionService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private CrmUserService crmuserService;
	@Autowired
	private ConditionMqttService conditionMqttService;
	@Resource
	private NoticeService noticeService;
	@Autowired 
	private RedisAPI redisAPI;
	
	/**
	 * 
	 * @description 根据空调ID查找某个空调的信息
	 * @author czg
	 * @param airConditionId
	 * @return
	 */
	@RequestMapping(value ="/getSomeAirCondition", method=RequestMethod.GET)
	@ResponseBody
	public Dto<AirCondition> getSomeAirCondition(HttpServletRequest request,@RequestParam("mac") String mac){
		//AirCondition airCondition = conditionService.selectAirConditionById(mac);
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		AirCondition airCondition = conditionService.selectAirConditionById(mac);
		AirCondition airCon = conditionService.selectAirConditionByMacAndUserId(mac, appusersId);
		if(airCondition == null) {
			return DtoUtil.returnFail("未查到该空调信息", "400");
		}
		if(airCon != null && EmptyUtils.isNotEmpty(airCon)) {
			airCondition.setTime(airCon.getTime());	
		}
		String objString = redisAPI.get(mac+"");
		if(objString !=null && EmptyUtils.isNotEmpty(objString )) {
			Map map = JSON.parseObject(objString, HashMap.class);
			String authorization = (String)map.get("authorization");
			airCondition.setAuthorization(authorization.toString());
		}
 
		return DtoUtil.returnDataSuccess(airCondition);
	}
	
	/**
	 * 
	 * <p>Title: getUserAirCondition</p>
	 * <p>Description: 根据APP用户ID查找绑定的空调</p>
	 * @param appuserId
	 * @return
	 */
	@RequestMapping(value ="/getUserAirCondition", method=RequestMethod.GET)
	@ResponseBody
	public Dto<List<AppUserAir>> getUserAirCondition(HttpServletRequest request){
		Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
		return DtoUtil.returnDataSuccess(conditionService.selectAirConditionByUserId(appuserId));
	}
	
	/**
	 * 
	 * <p>Title: updateAirConditionNote</p>
	 * <p>Description: APP用户修改空调备注</p>
	 * @param appuserAir
	 * @return
	 */
	@RequestMapping(value ="/updateAirConditionNote", method=RequestMethod.PUT)
	@ResponseBody
	public Dto<String> updateAirConditionNote(HttpServletRequest request, @RequestBody AppUserAir appuserAir){
		Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
		appuserAir.setAppusersId(appuserId);
		if(StringUtils.isEmpty(appuserAir.getMac())) {
			return DtoUtil.returnFail("空调MAC不能为空", "400");
		}
		conditionService.updateAirConditionNote(appuserAir);
		return DtoUtil.returnSuccess("修改成功");
	}
	
	/**
	 * 
	 * <p>Title: bindingAirConditionByAppUser</p>
	 * <p>Description: App用户绑定空调 </p>
	 * @param appuserId
	 * @return
	 */
	@RequestMapping(value ="/bindingAirConditionByAppUser", method=RequestMethod.POST)
	@ResponseBody
	public Dto<String> bindingAirConditionByAppUser(HttpServletRequest request, @RequestBody AppUserAir appuserAir){
		Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
		appuserAir.setAppusersId(appuserId);
		if(StringUtils.isEmpty(appuserAir.getMac())) {
			return DtoUtil.returnFail("空调MAC不能为空", SystemCode.ERROR);
		}
		
		Integer result = conditionService.bindingAirConditionByAppUser(appuserAir);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("绑定失败", SystemCode.ERROR);
		}
			 
		return DtoUtil.returnSuccess("绑定成功");
	}
	
	
	/**
	 * 
	 * <p>Title: releaseBindingByAppUser</p>
	 * <p>Description: App用户解除绑定空调 </p>
	 * @param appuserId
	 * @return
	 */
	@RequestMapping(value ="/releaseBindingByAppUser", method=RequestMethod.DELETE)
	@ResponseBody
	public Dto<String> releaseBindingByAppUser(HttpServletRequest request, @RequestBody AppUserAir appuserAir){
		Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
		appuserAir.setAppusersId(appuserId);
		if(StringUtils.isEmpty(appuserAir.getMac())) {
			return DtoUtil.returnFail("空调MAC不能为空", "400");
		}
		
		Integer result = conditionService.releaseBindingByAppUser(appuserAir);
		if(result == -1) {
			return DtoUtil.returnFail("解绑失败,请关闭空调后再试", "403");
		}
		if(result == -2) {
			return DtoUtil.returnFail("解绑失败,您还未绑定该空调", "403");
		}
		if(result == -3) {
			return DtoUtil.returnFail("解绑失败,存在包年信息不能解绑", "403");
		}
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("解绑失败", "400");
		}
			 
		return DtoUtil.returnSuccess("解绑成功");
	}
	
	
	/**
	 * 
	 * <p>Title: getAirConditionList</p>
	 * <p>Description: 获取空调系列列表</p>
	 * @param airCondition
	 * @return
	 */
	@RequestMapping(value ="/getAirConditionList", method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"devQuery"})
	public Dto<PageInfo<AirConditionVO>> getAirConditionList(AirConditionVO airCondition,HttpServletRequest request){
		Integer crmUserId = TokenUtil.getCrmUserId(request.getHeader("token"));
		CrmUser crmuser =new CrmUser();
		crmuser.setCrmuserId(crmUserId);
		CrmUser dbCrmUser = crmuserService.queryCrmUser(crmuser);
		if (airCondition.getPageNum() == null || airCondition.getPageSize() == null) {
			return DtoUtil.returnFail("分页参数不能为空", SystemCode.ERROR);
		}
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.hasRole("root")) {
			airCondition.setEnterpriseId(null);
		}else if(subject.hasRole("admin")) {
			airCondition.setAreaId(dbCrmUser.getAreaId());
		}else if(subject.hasRole("partner")){
			airCondition.setEnterpriseId(null);
			airCondition.setPartnerId(crmUserId);
		}else if(subject.hasRole("owner")) {
			airCondition.setEnterpriseId(crmUserId);
	
		}else {
			
			return DtoUtil.returnFail("用户无权限!", "401");
		}
		
		PageInfo<AirConditionVO> result = conditionService.selectAirConditionList(airCondition);
		
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: getAllAirCondition</p>
	 * <p>Description: 查询全部的空调信息,即设备分布</p>
	 * @param airCondition
	 * @return
	 */
	@RequestMapping(value ="/getAllAirCondition", method=RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"devScatter"})
	public Dto<List<AirConditionVO>> getAllAirCondition(){
		
		List<AirConditionVO> result = conditionService.selectAllAirCondition();
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: getSeriesList</p>
	 * <p>Description: 获取系列列表 </p>
	 * @param series
	 * @return
	 */
	/*@RequestMapping(value ="/getSeriesList", method=RequestMethod.GET)
	@ResponseBody
	public Dto<PageInfo<Series>> getSeriesList(SeriesVO series){
		
		PageInfo<Series> result = conditionService.selectSeriesList(series);
		return DtoUtil.returnDataSuccess(result);
	}*/
	
	/**
	 * 
	 * <p>Title: deleteAirCondition</p>
	 * <p>Description: 删除空调信息 </p>
	 * @param mac
	 * @return
	 */
	@record(businessLogic="删除空调")
	@RequestMapping(value ="/deleteAirCondition", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"devDelete"})
	public Dto<String> deleteAirCondition(@RequestBody AirCondition airCondition){
		String mac = airCondition.getMac();
		if (mac == null || StringUtils.isEmpty(mac.trim())) {
			return DtoUtil.returnFail("MAC不能为空", "400");
		}
		
		Integer result = conditionService.deleteCondition(mac);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("删除失败", "400");
		}
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: updateAirCondition</p>
	 * <p>Description: 修改空调信息</p>
	 * @param airCondition
	 * @return
	 */
	@record(businessLogic="修改空调信息")
	@RequestMapping(value ="/updateAirCondition", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"devUpdate"})
	public Dto<String> updateAirCondition(@RequestBody AirCondition airCondition){
		if (StringUtils.isEmpty(airCondition.getMac().trim())) {
			return DtoUtil.returnFail("请选择空调", SystemCode.ERROR);
		}
		
		Integer result = conditionService.updateAirCondition(airCondition);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("修改失败", SystemCode.ERROR);
		}
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: addAirCondition</p>
	 * <p>Description: 添加空调设备</p>
	 * @param airCondition
	 * @return
	 */
	@record(businessLogic="添加一台设备")
	@RequestMapping(value ="/addAirCondition", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value= {"devAdd"})
	public Dto<String> addAirCondition(@RequestBody AirCondition airCondition){
		// TODO 添加设备信息校验
		if(StringUtils.isEmpty(airCondition.getMac().trim())) {
			return new Dto<>(400, "请输入空调MAC地址");
		}
		if(null==airCondition.getPower()&&0==airCondition.getPower()) {
			return new Dto<>(400, "请输入空调的额定功率");
		}
		if(airCondition.getSeriesId() == null) {
			return new Dto<>(400, "请选择空调型号");
		}
		
		Integer result = conditionService.addAirCondition(airCondition);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("添加失败", SystemCode.ERROR);
		}if(result == -1) {
			return DtoUtil.returnFail("添加失败,空调mac已存在!", SystemCode.ERROR);
		}
		return DtoUtil.returnDataSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: bindingCondition</p>
	 * <p>Description: 空调绑定业主,新空调编组</p>
	 * @param airCondition
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/bindingCondition", method = RequestMethod.POST)
	@ResponseBody
	public Dto<String> bindingCondition(HttpServletRequest request ,@RequestBody BindingVO airCondition) {
		if(StringUtils.isEmpty(airCondition.getMac().trim())) {
			return new Dto<>(400, "请输入空调MAC地址");
		}
		
		if(airCondition.getLatitude() == null || airCondition.getLongitude() == null) {
			return new Dto<>(400, "请获取空调的位置");
		}
		
		if(StringUtils.isEmpty(airCondition.getUsername().trim())) {
			return new Dto<>(400, "请输入业主账号");
		}
		
		if(StringUtils.isEmpty(airCondition.getPassword().trim())) {
			return new Dto<>(400, "请输入确认密码");
		}
		
		/*if(StringUtils.isEmpty(airCondition.getType())) {
			return new Dto<>(400, "请选择型号");
		}*/
		
		if(airCondition.getPartnerId() == null ) {
			return new Dto<>(400, "请选择合作商");
		}
		
		AppUser appUser = TokenUtil.getAppUser(request.getHeader("token"));
		Integer appUserId = appUser.getAppusersId();
		AppUser data = (AppUser) appUserService.queryAppUserById(appUserId).getData();
		
		if (!airCondition.getPassword().equals(data.getPassword())) {
			return new Dto<>(400, "密码错误");
		}
		
		if(StringUtils.isEmpty(airCondition.getLocation().trim())) {
			return new Dto<>(400, "请输入位置信息");
		}
		AirCondition air = conditionService.selectAirConditionById(airCondition.getMac());
		//新装空调,编组
		if(EmptyUtils.isEmpty(air.getPartnerId()) && EmptyUtils.isEmpty(air.getEnterpriseId())) {
			BindingVO binding = conditionService.bindingCondition(airCondition,appUserId);
			if(binding == null) {
				return DtoUtil.returnFail("业主名称不对,绑定失败", SystemCode.ERROR);
			}
			//定时两小时后关闭空调
			ControlVO controlVO =new ControlVO();
			controlVO.setMac(airCondition.getMac());
			controlVO.setOrder(SystemCode.AIR_CONDITION_ORDER_TIMING);
			controlVO.setValue(7200);
			try {
				Integer appuserId = TokenUtil.getAppUserId(request.getHeader("token"));
				conditionMqttService.controlAirCondition(controlVO, appuserId);
			}catch(Exception e) {
				e.printStackTrace();
			}
			binding.setPassword(null);
			binding.setAppNickName(appUser.getNickname());
			binding.setBindingDate(new Date());
			
			return DtoUtil.returnDataSuccess(binding);
		}else {//修改编组
			Map map = new HashMap<>();
			map.put("username", airCondition.getUsername());
			map.put("roleName", "owner");
			CrmUser crmUser = conditionService.getUserByUserNameAndRole(map);
//			CrmUser crmUser = crmUserMapper.selectCrmUserByUsername(bindingVO.getUsername());
			if (crmUser == null) {
				return DtoUtil.returnFail("业主名称不对,绑定失败", SystemCode.ERROR);
			}			
			AirCondition airCondition_update = new AirCondition();
			BeanUtils.copyProperties(airCondition, airCondition_update);
			airCondition_update.setEnterpriseId(crmUser.getCrmuserId());
			conditionService.updateAirCondition(airCondition_update);
			AirCondition aircon = conditionService.selectAirConditionById(airCondition.getMac());
			BindingVO binding = new BindingVO();
			BeanUtils.copyProperties(aircon,binding);
			binding.setUsername(crmUser.getUsername());
			binding.setPassword(null);
			binding.setAppNickName(appUser.getNickname());
			binding.setBindingDate(new Date());
			return DtoUtil.returnDataSuccess(binding);
		}
		
		
	}

	/**
	 * @author lxl
	 * @param mac
	 * @param userId
	 * <p>Description:结束安装空调</p>
	 */
	@RequestMapping(value = "/finshInstall", method = RequestMethod.GET)
	@ResponseBody
	public Dto<String> finishInstall(HttpServletRequest request, @RequestParam("mac")String mac, @RequestParam("appUserId")Integer appUserId){
		String msg = conditionService.finshInstall(appUserId, mac);
		return DtoUtil.returnSuccess(msg);
	}
	/**
	 * <p>Title: remindShutDown</p>
	 * <p>Description: 提醒关机</p>
	 * @param airCondition
	 * @return
	 */
	@RequestMapping(value = "/remindShutDown", method = RequestMethod.GET)
	@ResponseBody
	public Dto<String> remindShutDown(HttpServletRequest request, Float longitude, Float latitude) {
		Integer appUserId = TokenUtil.getAppUserId(request.getHeader("token"));
		OperationRecord record = conditionService.getLastOperRecord(appUserId);
		if (record == null || "stop".equals(record.getOrder())) {
			return DtoUtil.returnSuccess("Unopened");
		}
		
		AirCondition airCondition = conditionService.selectAirConditionById(record.getMac());
		String distance = MapDistance.getDistance(latitude+"", longitude+"", airCondition.getLongitude()+"", airCondition.getLatitude()+"");
		System.out.println("距离:"+distance);
		if (Float.parseFloat(distance)/1000 >= SystemCode.OperationRange) {
			return DtoUtil.returnSuccess("Out Of Range");
		}
		
		return DtoUtil.returnSuccess("Within Range");
	}
	
	/**
	 * 
	 * <p>Title: getConditionInfoByMac</p>
	 * <p>Description:获取空调的使用信息 </p>
	 * @param request
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/getConditionInfoByMac", method = RequestMethod.GET)
	@ResponseBody
	public Dto<String> getConditionInfoByMac(HttpServletRequest request,@RequestParam("mac") String mac) {
		Integer appUserId = TokenUtil.getAppUserId(request.getHeader("token"));
		ConditionInfo conditionInfo = conditionService.getConditionInfoByMac(mac, appUserId);
		
		return DtoUtil.returnDataSuccess(conditionInfo);
	}
	
	/**
	 * 获取空调的绑定信息
	 * @param request
	 * @param mac
	 * @return
	 */
	@RequestMapping(value = "/getBindingInfoByMac", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions(value= {"devQuery"})
	public Dto<String> getBindingInfoByMac(@RequestParam("mac") String mac) {
		
		List<AppUserAirVo> list =  conditionService.queryBindingInfoByMac(mac);
		
		return DtoUtil.returnDataSuccess(list);
	}
	
	/**
	 * 
	 * <p>Title: releaseBindingByAppUser</p>
	 * <p>Description: crm端解除App用户的绑定空调 </p>
	 * @param appuserId
	 * @return
	 * @throws MqttException 
	 * @throws MqttPersistenceException 
	 */
	@RequestMapping(value ="/releaseBindingByAppUserCrm", method=RequestMethod.DELETE)
	@ResponseBody
	@RequiresPermissions(value= {"devUntie"})
	public Dto<String> releaseBindingByAppUserCrm(HttpServletRequest request, @RequestBody AppUserAir appuserAir) throws MqttPersistenceException, MqttException{
		if(StringUtils.isEmpty(appuserAir.getMac())&&EmptyUtils.isEmpty(appuserAir.getAppusersId())) {
			return DtoUtil.returnFail("空调MAC或用户id不能为空", "400");
		}
		
		Integer result = conditionService.releaseBindingByCrmUser(appuserAir);
		if(result == SystemCode.ADD_UPDATE_FAILE) {
			return DtoUtil.returnFail("解绑失败", "400");
		}
		Notice notice = new Notice();
		notice.setAppusersId(appuserAir.getAppusersId());
		notice.setTitle(SystemCode.RELEASE_TITLE);
		notice.setContent("您绑定的编号为"+appuserAir.getMac()+"的空调已经被解除绑定");
		noticeService.addNotice(notice); 
		return DtoUtil.returnSuccess("解绑成功");
	}
	
	@ResponseBody
	@RequestMapping(value="/addAirConditionFromExcel",method=RequestMethod.POST)
	public Dto addAirConditionFromExcel(MultipartFile multipartFile) throws Exception {     
		int adminId = 1;     
		//获取上传的文件    
//		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;     
//		MultipartFile file = multipart.getFile("upfile");     
//		String month = request.getParameter("month");  
		InputStream in = multipartFile.getInputStream();
//		InputStream in = file.getInputStream();     
		//数据导入    
		Boolean bool = conditionService.addAirConditionFromExcel(in,multipartFile,adminId);     
		in.close();  
		if (!bool) {
			return DtoUtil.returnFail("添加失败",ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功");
	}
	
}
