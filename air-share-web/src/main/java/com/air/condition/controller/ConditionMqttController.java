package com.air.condition.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.condition.service.ConditionMqttService;
import com.air.condition.service.ConditionService;
import com.air.condition.service.MqttManager;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.pojo.vo.ControlVO;
import com.air.redis.RedisAPI;
import com.air.utils.EmptyUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/conditionMqtt")
public class ConditionMqttController {
	@Autowired
	private MqttManager mqttManager;
	@Autowired
	private ConditionMqttService conditionMqttservice;
	@Autowired
	private ConditionService conditionService;
	@Autowired
	private RedisAPI redisAPI;
	
	/**
	 * 
	 * <p>Title: updateAirConditionInfo</p>
	 * <p>Description: 修改空调信息</p>
	 * @param airConditionId
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@RequestMapping(value ="/updateAirConditionInfo", method=RequestMethod.GET)
	@ResponseBody
	public Dto<String> updateAirConditionInfo(@RequestParam("airConditionId") String airConditionId) throws MqttPersistenceException, MqttException{
		mqttManager.publish(airConditionId, "hello", SystemCode.MQTT_QOS_ONE, false);
		return DtoUtil.returnDataSuccess("send success");
		 
	}
	
	/**
	 * 
	 * <p>Title: rebootAirCondition</p>
	 * <p>Description: 重启设备</p>
	 * @param mac
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@RequestMapping(value ="/rebootAirCondition", method=RequestMethod.GET)
	@ResponseBody
	public Dto<String> rebootAirCondition(@RequestParam("mac") String mac) throws MqttPersistenceException, MqttException{
		conditionMqttservice.rebootAirCondition(mac);
		return DtoUtil.returnDataSuccess("send success");
	}
	
	@RequestMapping(value ="/crmControlAirCondition", method=RequestMethod.POST)
	@ResponseBody
	public Dto<String> crmControlAirCondition(@RequestBody ControlVO controlVO) throws MqttPersistenceException, MqttException{
		if (StringUtils.isEmpty(controlVO.getMac().trim())) {
			return DtoUtil.returnFail("空调MAC地址不能为空", SystemCode.ERROR);
		}
		
		if (StringUtils.isEmpty(controlVO.getOrder().trim())) {
			return DtoUtil.returnFail("操作指令不能为空", SystemCode.ERROR);
		}
		
		switch (controlVO.getOrder()) {
		case SystemCode.AIR_CONDITION_ORDER_TEMP:
			if (controlVO.getValue() == null || controlVO.getValue() <= 15 || controlVO.getValue() >= 32) {
				return DtoUtil.returnFail("温度设置错误(16-31℃)", SystemCode.ERROR);
			}
			break;
		case SystemCode.AIR_CONDITION_ORDER_TIMING:
			if (controlVO.getValue() == null || controlVO.getValue() <= 0) {
				return DtoUtil.returnFail("定时设置错误", SystemCode.ERROR);
			}
			break;
		}
		
		String result = conditionMqttservice.crmControlAirCondition(controlVO);
		if (!result.equals(SystemCode.SUCCESS)) {
			return DtoUtil.returnFail(result, SystemCode.ERROR);
		}
		
		return DtoUtil.returnSuccess(result);
	}
	
	/**
	 * 
	 * <p>Title: controlAirCondition</p>
	 * <p>Description: 空调设备的控制 </p>
	 * @param controlVO
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@RequestMapping(value ="/controlAirCondition", method=RequestMethod.POST)
	@ResponseBody
	public Dto<String> controlAirCondition(HttpServletRequest request, @RequestBody ControlVO controlVO) throws MqttPersistenceException, MqttException{
		if (StringUtils.isEmpty(controlVO.getMac().trim())) {
			return DtoUtil.returnFail("空调MAC地址不能为空", SystemCode.ERROR);
		}
		
		if (StringUtils.isEmpty(controlVO.getOrder().trim())) {
			return DtoUtil.returnFail("操作指令不能为空", SystemCode.ERROR);
		}
			
		switch (controlVO.getOrder()) {
		case SystemCode.AIR_CONDITION_ORDER_TEMP:
			if (controlVO.getValue() == null || controlVO.getValue() <= 15 || controlVO.getValue() >= 32) {
				return DtoUtil.returnFail("温度设置错误(16-31℃)", SystemCode.ERROR);
			}
			break;
		/*case SystemCode.AIR_CONDITION_ORDER_TIMING:
			if (controlVO.getValue() == null || controlVO.getValue() <= 0) {
				return DtoUtil.returnFail("定时设置错误", SystemCode.ERROR);
			}
			break;*/
		}
		
		String token = request.getHeader("token");
		Integer appuserId = TokenUtil.getAppUserId(token);
		Dto result = conditionMqttservice.controlAirCondition(controlVO, appuserId);
		
		return result;
	}
	
	/**
	 * 
	 * <p>Title: controlAirCondition</p>
	 * <p>Description: 查询空调控制指令状态 </p>
	 * @param controlVO
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@RequestMapping(value ="/queryOrderStatus", method=RequestMethod.POST)
	@ResponseBody
	public Dto<ControlVO> queryOrderStatus(HttpServletRequest request, @RequestBody ControlVO controlVO) {
		String orderJson = redisAPI.get(controlVO.getSeq());
		ControlVO orderMap;
		if(orderJson==null) {
			controlVO.setStatus("fail");
			orderMap = controlVO;
		}else {
			orderMap = JSON.parseObject(orderJson, ControlVO.class);
		}
		
		return DtoUtil.returnDataSuccess(orderMap);
	}
	
	
	/**
	 * 
	 * <p>Title: increase</p>
	 * <p>Description: 固件升级功能 </p>
	 * @param controlVO
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@RequestMapping(value ="/increase", method=RequestMethod.GET)
	@ResponseBody
	public Dto<String> increase(Long checksum,String version) throws MqttPersistenceException, MqttException{
		if (EmptyUtils.isEmpty(checksum)) {
			return DtoUtil.returnFail("校验码为空", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isEmpty(version)) {
			return DtoUtil.returnFail("版本号为空", ErrorCode.RESP_ERROR);
		}
		redisAPI.set("checksum", JSONObject.toJSONString(checksum));
		redisAPI.set("version", JSONObject.toJSONString(version));
		List<String> macList = conditionService.queryAllAirMac();
		
		if (macList.isEmpty()) {
			return DtoUtil.returnFail("没有空调可用来升级，请先添加空调", ErrorCode.RESP_ERROR);
		}
		int num = 1;//计算次数进行判断，第一次的话需要把一些数据保存在redis中
//		for (int i = 0; i < macList.size(); i++) {
//			byte[] bs = conditionMqttservice.readFileInBytesToString(macList.get(i),num);
//			num++;
//		}
		byte[] bs = conditionMqttservice.readFileInBytesToString();
		return DtoUtil.returnDataSuccess(Arrays.toString(bs));
	}
	
}
