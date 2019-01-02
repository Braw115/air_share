package com.air.condition.service.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.appinfo.mapper.AirInfoMapper;
import com.air.condition.service.ConditionMqttService;
import com.air.condition.service.MqttManager;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.SystemCode;
import com.air.mqtt.service.impl.MqttPushCallBackService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.TimingTasks;
import com.air.pojo.vo.ControlVO;
import com.air.redis.RedisAPI;
import com.air.timingtasks.mapper.TimingTasksMapper;
import com.air.user.mapper.AppUserMapper;
import com.air.utils.Base64;
import com.air.utils.EmptyUtils;
import com.air.utils.MqttSendUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class ConditionMqttServiceImpl implements ConditionMqttService {

	@Autowired
	private MqttManager mqttManager;
	@Autowired
	private AppUserMapper appUserMapper;
	@Autowired
	private RedisAPI redisAPI;
	@Autowired
	private AirConditionMapper airConditionMapper;
	@Autowired
	private TimingTasksMapper timingtasksMapper;
	@Autowired
	private AirInfoMapper airInfoMapper;

	
	// 修改空调剩余时间
	
	// 重启设备
	@Override
	public void rebootAirCondition(String mac) throws MqttPersistenceException, MqttException {
		Map<String,String> map = new HashMap<String, String>();
		map.put("cmd", "reboot");
		map.put("mac", "abcd-aaa-aaa-c2a1");
		mqttManager.publish(SystemCode.MQTT_TOPIC_TWO, JSON.toJSONString(map), SystemCode.MQTT_QOS_ONE, false);
	}

	/**
	 * 用户操作空调
	 */
	@Override
	public Dto controlAirCondition(ControlVO controlVO, Integer appuserId) throws MqttPersistenceException, MqttException {
		ConditionInfo conditionInfo = airConditionMapper.getConditionInfoByMac(controlVO.getMac());
		AirCondition airCondition = airConditionMapper.selectAirConditionById(controlVO.getMac());
//		String distance = MapDistance.getDistance(controlVO.getLatitude()+"", controlVO.getLongitude()+"", airCondition.getLatitude()+"", airCondition.getLongitude()+"");
		/*AppInfo appInfo = new AppInfo();
		appInfo.setType("controlDistance");
		AppInfo controlDistance = airInfoMapper.selectConfigByAppInfo(appInfo).get(0);
		if (Float.parseFloat(controlDistance.getContent()) < Float.parseFloat(distance)) {
			return "您已超出遥控的控制距离";
		}*/
		AppUserAir appUserAir = airConditionMapper.selectAppUserAirByUseIdAndMac(appuserId,controlVO.getMac());
		if(EmptyUtils.isEmpty(appUserAir)) {
			 
			return DtoUtil.returnFail("您还没有绑定这台空调,请绑定后再操作", "403");
		}
			
		if (redisAPI.exist(controlVO.getMac())) {
			String macValue = redisAPI.get(controlVO.getMac());
			HashMap hashMap = JSONObject.parseObject(macValue, HashMap.class);
			Integer uId = (Integer) hashMap.get("userId");
//			System.out.println(uId + "**********************" + TokenUtil.getAppUserId(token));
			if (uId != appuserId) {
				return DtoUtil.returnFail("您没有空调的控制权", "403");
				//return "您没有空调的控制权";
			}
		}

		if ("stop".equals(conditionInfo.getStatus()) && !"power".equals(controlVO.getOrder()) && !"timing".equals(controlVO.getOrder())) {
			return DtoUtil.returnFail("请开机后再执行其他操作", "403");
			//return "请开机后再执行其他操作";
		}
		/*if (redisAPI.exist(controlVO.getMac()+"order")) {
			return "操作不能太频繁";
		}
		redisAPI.set(controlVO.getMac()+"order", 1, "order");*/
		
		Map<String, Object> map = new HashMap<>();
		String seq = UUID.randomUUID().toString().substring(30, 36);
		map.put("seq", seq);
		controlVO.setUserId(appuserId);
		int index;
		Long ttl = redisAPI.ttl(appuserId+"");
		String mac = redisAPI.get(appuserId+"");
		switch (controlVO.getOrder()) {
		case "power":	// 电源
			AppUser appUser = appUserMapper.selectById(appuserId);
			controlVO.setUseTime(appUser.getUseTime());
			String mod =appUserAir.getModel();
			//空调原来状态
			String power = "start".equals(conditionInfo.getStatus())? "stop": "start";
			map.put("order", power);
			map.put("value", 0);
			if (ttl > 0 && mac.equals(conditionInfo.getMac()) ) {
				controlVO.setUseTime(ttl);
			}else {
				if("start".equals(power)) {
					if("year".equals(mod)) {//包年
						if(appUserAir.getTime()<System.currentTimeMillis()) { //包年过期
							AppUserAir userAir = new AppUserAir();
							userAir.setModel("hour");
							userAir.setTime(null);
							airConditionMapper.updateAirConditionNote(userAir);
							controlVO.setModel("hour");
							BigDecimal level = (new BigDecimal("3.000")).divide(new BigDecimal(60)).multiply(airCondition.getHour());
							if (!(1==appUser.getBalance().compareTo(level))) {
								return DtoUtil.returnFail("余额不足以开启空调, 请充值", "403");
							}
						}
							
					}else {//包时
						controlVO.setModel("hour");
						BigDecimal level = (new BigDecimal("3.000")).divide(new BigDecimal(60)).multiply(airCondition.getHour());
						if (!(1==appUser.getBalance().compareTo(level))) {
							return DtoUtil.returnFail("余额不足以开启空调, 请充值", "403");
						}
					}
						
				}			
			}
		break;
			
		case SystemCode.AIR_CONDITION_ORDER_MODEL:	// 模式
//			index = getIndexForArray(SystemCode.AIR_CONDITION_MODEL, conditionInfo.getModel());
//			Integer model = (index+1)%(SystemCode.AIR_CONDITION_MODEL.length);
			Integer model =controlVO.getValue();
			map.put("order", SystemCode.AIR_CONDITION_ORDER_MODEL);
			map.put("value", model);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_WIND_SPEED:	// 风速
			index = getIndexForArray(SystemCode.AIR_CONDITION_WIND_SPEED, conditionInfo.getWindSpeed());
			Integer windSpeed = ((index+1)%(SystemCode.AIR_CONDITION_WIND_SPEED.length));
			map.put("order", SystemCode.AIR_CONDITION_ORDER_WIND_SPEED);
			map.put("value", windSpeed == 0? windSpeed: windSpeed+1);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_SWING:	//风向
			index = getIndexForArray(SystemCode.AIR_CONDITION_SWING, conditionInfo.getSwing());
			Integer swing = (index+1)%(SystemCode.AIR_CONDITION_SWING.length);
			map.put("order", SystemCode.AIR_CONDITION_ORDER_SWING);
			map.put("value", swing);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_SLEEP:	//睡眠
			map.put("order", SystemCode.AIR_CONDITION_ORDER_SLEEP);
			Integer sleep = "open".equals(conditionInfo.getSleep())? 0: 1;
			map.put("value", sleep);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_DRY_HOT:	// 干燥 辅热
			index = getIndexForArray(SystemCode.AIR_CONDITION_DRY_HOT, conditionInfo.getDryHot());
			Integer dryHot = (index+1)%(SystemCode.AIR_CONDITION_DRY_HOT.length);
			map.put("order", SystemCode.AIR_CONDITION_ORDER_DRY_HOT);
			map.put("value", dryHot);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_STRONG:	//强力
			map.put("order", SystemCode.AIR_CONDITION_ORDER_STRONG);
			Integer strong = "open".equals(conditionInfo.getStrong())? 0: 1;
			map.put("value", strong);
			break;
			
		case SystemCode.AIR_CONDITION_ORDER_TIMING:	//定时
			map.put("order", SystemCode.AIR_CONDITION_ORDER_TIMING);
//			String timing = conditionInfo.getStatus().equals("start")? "stop": "start";
			String timing = "stop";
			TimingTasks timingTasks = new TimingTasks();
			timingTasks.setExecute(false);
			timingTasks.setOrder(timing);
			timingTasks.setUserId(appuserId);
			timingTasks.setMac(controlVO.getMac());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			if (conditionInfo.getTiming() == null || "stop".equals(conditionInfo.getTiming()) ) {// 没开启定时
				if (controlVO.getValue() == null) {
					return DtoUtil.returnSuccess("请输入空调定时时间", "200");
					//return "请输入空调定时时间";
				}
				calendar.add(Calendar.SECOND, controlVO.getValue());
				timingTasks.setExeTime(calendar.getTime());
				timingtasksMapper.insertTimingTasks(timingTasks);
				conditionInfo.setTiming("start");
			}else {// 开启过定时 
				if (controlVO.getValue() == null) {//关闭定时
					
					timingtasksMapper.deleteTimingTasks(controlVO.getMac(), appuserId);
					conditionInfo.setTiming(null);
				}else {//覆盖定时ijiajn 
					calendar.add(Calendar.SECOND, controlVO.getValue());
					timingTasks.setExeTime(calendar.getTime());
					timingtasksMapper.modifyTimingTasks(timingTasks);
				}
			}
			
			airConditionMapper.updateConditionInfoByMac(conditionInfo);
			return DtoUtil.returnSuccess("ok", seq);
			//return SystemCode.SUCCESS;
			
		case SystemCode.AIR_CONDITION_ORDER_TEMP:	// 温度
			map.put("order", SystemCode.AIR_CONDITION_ORDER_TEMP);
			map.put("value", controlVO.getValue());
			break;
		default:
			return DtoUtil.returnFail("未找到该指令", "403");
			//return "未找到该指令";
		}
		
		controlVO.setOrderMap(map);
		if ("start".equals(controlVO.getOrder()) || "updateTime".equals(controlVO.getOrder())) {
			AppUser appUser = appUserMapper.selectById(controlVO.getUserId());
			controlVO.setUseTime(ttl > 0? ttl: appUser.getUseTime());
			controlVO.setModel(appUser.getModel());
		}
		controlVO.setStatus("doing");
		
		boolean set = redisAPI.set(seq, 60, JSON.toJSONString(controlVO));
		System.out.println(JSON.toJSONString(map));
		mqttManager.publish(controlVO.getMac(), MqttSendUtil.Map2StrAndLen(map), SystemCode.MQTT_QOS_ONE, false);
		return DtoUtil.returnSuccess("ok",seq );
	}
	
	/**
	 * 
	 * <p>Title: getIndexForArray</p>
	 * <p>Description:根据数组元素获取下标 </p>
	 * @param array
	 * @param item
	 * @return
	 */
	private int getIndexForArray(String[] array, String item) {
		List<String> asList = Arrays.asList(array);
		return asList.indexOf(item);
	}

	@Override
	public String crmControlAirCondition(ControlVO controlVO) {
		
		return SystemCode.SUCCESS;
	}
	
	/**
	 * 读取二进制文件分段传输
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	@Override
	public byte[] readFileInBytesToString() throws MqttPersistenceException, MqttException {
		Map<String,Object> map = new HashMap<>();
		final int readArraySizePerRead = 32;
		File file = new File("C:\\Users\\wzq\\Desktop\\slot2.bin");
		ArrayList<Byte> bytes = new ArrayList<>();
		ArrayList<String> bs = new ArrayList<>();
		try {
			if (file.exists()) {
				DataInputStream isr = new DataInputStream(new FileInputStream(file));
				byte[] tempchars = new byte[readArraySizePerRead];
				int charsReadCount = 0;
				DataOutputStream dos = null;
				int num = 0;
				int total = 0;//总字节数
				
				while ((charsReadCount = isr.read(tempchars)) != -1) {
//					dos = new DataOutputStream(new FileOutputStream("C:\\Users\\wzq\\Desktop\\bin\\"+num+".bin"));
					map.put("by", tempchars);
					map.put("n", num);
//					dos.write(tempchars);
					byte[] temps = new byte[charsReadCount];
					for (int i = 0; i < charsReadCount; i++) {
						temps[i]=tempchars[i];
					}
					bs.add(num, Base64.encode(temps));
					for (int i = 0; i < charsReadCount; i++) {
						bytes.add(tempchars[i]);
					}
//					System.out.println(num+":"+Arrays.toString(toPrimitives(bytes.toArray(new Byte[0]))));
					System.out.println(MqttSendUtil.Map2StrAndLen(map));
					mqttManager.publish("O3570W7OG7AZ",MqttSendUtil.Map2StrAndLen(map)+"FF",SystemCode.MQTT_QOS_ONE, false);
					Thread.sleep(1000l);
					num++;
					total+=charsReadCount;
//					if (num==70) {
//						break;
//					}
				}
				redisAPI.set("value", JSONObject.toJSONString(bs));
				Thread.sleep(1500l);
				endOrder(total,bs);
				isr.close();
				if (dos!=null) {
					dos.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return MqttPushCallBackService.toPrimitives(bytes.toArray(new Byte[0]));
	}

	/**
	 * 传输固件升级所需文件完成后需要发送的信息
	 * @param total
	 * @param bs
	 */
	private void endOrder(int total, ArrayList<String> bs) {
		try {
			Map<String,Object> map = new HashMap<>();
//			map.put("total", bs.size());
			byte[] totalChars = new byte[total];//总字符
			int num = 0;
			for (int i = 0; i < bs.size(); i++) {
				byte[] bytes = Base64.decode(bs.get(i));
				for (int j = 0; j < bytes.length; j++) {
					totalChars[num]=bytes[j];
					num++;
				}
			}
			redisAPI.set("sussMsg", "true");
//			boolean flag = true;
			while (true) {
//				String msg = redisAPI.get("sussMsg");
				Long ttl = redisAPI.ttl("sussMsg");
				System.err.println(redisAPI.ttl("sussMsg"));
//				if ("true".equals(msg)) {
//					redisAPI.set("sussMsg", "false");
////					flag=false;
//					System.err.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//					break;
//				}
				if (-2==ttl) {
					break;
				}
				Map<String, Object> mapOrder = new HashMap<>();
//				mapOrder.put("order", "firmsize");//
//				mapOrder.put("value", bs.size());
//				mapOrder.put("seq", "seq");
//				mqttManager.publish("O3570W7OG7AZ", MqttSendUtil.Map2StrAndLen(mapOrder), SystemCode.MQTT_QOS_ONE,
//						false);
				try {
					Thread.sleep(1500l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mapOrder.put("order", "checksum");//
//				Long sum = MqttSendUtil.Byte2StrAndLen(totalChars);
				String checksum = redisAPI.get("checksum");
				Long sum = Long.valueOf(checksum);
				System.err.println("**********************"+sum);
//				map.put("sum", sum);
				mapOrder.put("value", sum);
//				redisAPI.set("sendMsg", JSONObject.toJSONString(map));
				mqttManager.publish("O3570W7OG7AZ", MqttSendUtil.Map2StrAndLen(mapOrder), SystemCode.MQTT_QOS_ONE,
						false);
				try {
					Thread.sleep(1500l);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				mapOrder.put("order", "fota");//
				mapOrder.put("value", 1);
				mqttManager.publish("O3570W7OG7AZ", MqttSendUtil.Map2StrAndLen(mapOrder), SystemCode.MQTT_QOS_ONE,
						false);
//				redisAPI.set("sussMsg", "false");
				try {
					Thread.sleep(1500l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

		
	}
	
	public void readInBytesToString(String mac){
		
		try {
			int total = 0;
			String json = redisAPI.get("value");
			List<String> list = JSONObject.parseArray(json, String.class);
			Map<String, Object> map = new HashMap<>();
			for (int i = 0; i < list.size(); i++) {
				byte[] bytes = Base64.decode(list.get(i));
				map.put("by", bytes);
				map.put("n", i);
				total+=bytes.length;
				mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(map) + "FF", SystemCode.MQTT_QOS_ONE, false);
			}
			endOrder(total, (ArrayList<String>) list);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
