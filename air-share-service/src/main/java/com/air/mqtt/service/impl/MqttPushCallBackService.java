package com.air.mqtt.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.aircondition.mapper.CarbonIndicatorsMapper;
import com.air.aircondition.mapper.OperationRecordMapper;
import com.air.condition.service.MqttManager;
import com.air.constant.GenerateNum;
import com.air.constant.SystemCode;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AirUseInfo;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CarbonIndicators;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.entity.Order;
import com.air.pojo.vo.ControlVO;
import com.air.redis.RedisAPI;
import com.air.timingtasks.mapper.TimingTasksMapper;
import com.air.user.mapper.AppUserMapper;
import com.air.user.mapper.OrderMapper;
import com.air.utils.Base64;
import com.air.utils.DateFormats;
import com.air.utils.JsonMqtt;
import com.air.utils.MqttSendUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.jiguang.common.resp.APIRequestException;

@Service
@Transactional
public class MqttPushCallBackService implements MqttCallback {

	@Autowired
	private MqttManager mqttManager;
	@Autowired
	private AirConditionMapper airConditionMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OperationRecordMapper operationRecordMapper;
	@Autowired
	private CarbonIndicatorsMapper carbonIndicatorsMapper;
	@Autowired
	private AppUserMapper appUserMapper;
	@Autowired
	private RedisAPI redisAPI;
	@Autowired 
	private TimingTasksMapper timingTasksMapper;
	/*
	 * @Resource private RedisAPI redisApi;
	 */

	Logger log = Logger.getLogger(MqttPushCallBackService.class);

	public void connectionLost(Throwable cause) {
		System.out.println("连接MQTT");
		mqttManager.initialize();
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.print("接收的主题 : " + topic);
		System.out.println("接收的内容 : " + new String(message.getPayload(), "UTF-8")+"******"+DateFormats.getDateFormat());
		try {
			String msg = new String(message.getPayload(), "UTF-8");
			String string = msg.substring(0, msg.lastIndexOf("}") + 1);
			JSONObject jsonObject = JSONObject.parseObject(string);
			String cmd = jsonObject.getString("cmd");
			JSONObject data = jsonObject.getJSONObject("dt");
//			JSONObject data = jsonObject.getJSONObject("data");
			if (cmd != null && cmd != "") {
				if ("ud".equals(cmd)) {
					AirConditionUpdateInfo(data);
					// System.out.println("success");
					return;
				}
//				if ("update".equals(cmd)) {
//					AirConditionUpdateInfo(data);
//					// System.out.println("success");
//					return;
//				}
				if ("confirm".equals(cmd)) {
					AirConditionOperation(jsonObject.getJSONObject("data"));
					return;
				}
//				if ("firmware".equals(cmd)) {
//					BinaryTransfer(jsonObject.getJSONObject("data"));
//					return;
//				}
				if ("reconnect".equals(cmd)) {
					String mac = jsonObject.getString("mac");
					String version = jsonObject.getString("version");
					System.err.println("************************"+cmd+"====="+mac);
					AirCondition airCondition = new AirCondition();
					airCondition.setMac(mac);
					airCondition.setUseStatus(0);
					airConditionMapper.updateAirCondition(airCondition);//{"order":"rcvcon","value":1,"seq":"seq"}
//					if (!version.equals(redisAPI.get("version"))) {
//						resendBinaryTransfer(mac);
//					}
					Map<String,Object> map = new HashMap<>();
					map.put("order", "rcvcon");
					map.put("value", 1);
					map.put("seq", GenerateNum.getRandomNumber());
					mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(map), SystemCode.MQTT_QOS_ONE, false);
					return;
				}
				if ("disconnect".equals(cmd)) {
					String mac = jsonObject.getString("mac");
//					System.err.println("************************"+cmd+"====="+mac);
					AirCondition airCondition = new AirCondition();
					airCondition.setMac(mac);
					airCondition.setUseStatus(-1);
					airConditionMapper.updateAirCondition(airCondition);
					return;
				}
				if("state".equals(cmd)) {
					JSONObject data1 = jsonObject.getJSONObject("date");
					String mac = data1.getString("mac");
					if (redisAPI.exist(mac)) {
					/************************************断电判断**********************************/
					//断电判断
					String macValue = redisAPI.get(mac);
					HashMap<String, Object> map = JSONObject.parseObject(macValue, HashMap.class);
					Integer state = data1.getInteger("state");
					if(state!=null) {
						//空调实际状态为关
						if(!isStart(state)) {
							//关闭面板
							ConditionInfo conditionInfo  = new ConditionInfo();
							conditionInfo.setMac(mac);
							conditionInfo.setAppuserId(null);
							conditionInfo.setStatus("stop");
							Integer res = airConditionMapper.updateConditionInfoByMac(conditionInfo);
							redisAPI.delete(mac);
							//结束订单
							modifyAndAdd((Integer)map.get("userId"), (Long)map.get("duration"), mac, (String)map.get("model"));
							return;
						}
					}
					/*********************************************************************/
					}	
					return;
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
	// {"cmd":"firmware","data":{"mac":"O3570W7OG7AZ","num":num}}
	/**
	 * 
	 * <p>
	 * Title: BinaryTransfer
	 * </p>
	 * <p>
	 * Description: 固件升级传输二进制信息
	 * </p>
	 * 
	 * @param data
	 * @param num
	 */
	private void BinaryTransfer(JSONObject data) {
		Integer num = data.getInteger("num");
		String mac = data.getString("mac");
		if (0 == num & null == num) {// 硬件为传输num
			System.out.println("=============");// 硬件为传输num值
			return;
		}
		if (num == SystemCode.TRANSPORT_FAIL) {// 传输失败，重新传输 提示用户重新升级？
			System.out.println("*************传输失败，重新传输");
			System.err.println("*********************************9998");
			resendBinaryTransfer(mac);
			return;
		}
		if (num == SystemCode.TRANSPORT_SUCCESS) {// 硬件为传输num
			// redisAPI.set("sussMsg", "true");
			redisAPI.delete("sussMsg");
			System.out.println("=============接收完成");
			System.err.println("*********************************9999");
			return;
		}
		// if (num==SystemCode.TRANSPORT_SIZE) {//文件大小接收失败
		// System.err.println("*********************************9997");
		// SendSizeMsg(mac);
		// return;
		// }
		// if (num!=SystemCode.TRANSPORT_SUCCESS) {//接收到硬件接收成功的信息，传输缺失根据num传输相应的数据
		System.err.println("*********************************" + num);
		System.err.println("*********************************"+redisAPI.ttl("sussMsg"));
		if (-2==redisAPI.ttl("sussMsg")) {
			BinaryTransferByNum(num, mac);
		}
		// }

	}

	private void resendBinaryTransfer(String mac) {

		try {
			String bs = redisAPI.get("value");

			List<String> list = JSONObject.parseArray(bs, String.class);
			Map<String, Object> map = new HashMap<>();
			for (int i = 0; i < list.size(); i++) {
				map.put("by", list.get(i));
				map.put("n", i);
				System.err.println(MqttSendUtil.Map2StrAndLen(map));
				mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(map), SystemCode.MQTT_QOS_ONE, false);
			}
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	private void SendSizeMsg(String mac) {

		try {
			String msg = redisAPI.get("sendMsg");
			Map msgMap = JSONObject.parseObject(msg, Map.class);
			Map<String, Object> mapOrder = new HashMap<>();
			mapOrder.put("order", "firmsize");//
			mapOrder.put("value", msgMap.get("total"));
			mapOrder.put("seq", "seq");
			// System.err.println("***********************************************************************"+mac);
			mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(mapOrder), SystemCode.MQTT_QOS_ONE, false);
			// System.err.println("***********************************************************************");
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * <p>
	 * Title: BinaryTransferByNum
	 * </p>
	 * <p>
	 * Description: 固件升级传输缺失的二进制信息
	 * </p>
	 * 
	 * @param data
	 * @param num
	 * @throws MqttException
	 * @throws MqttPersistenceException
	 */
	private void BinaryTransferByNum(int num, String mac) {

		try {
			String bs = redisAPI.get("value");

			List<String> list = JSONObject.parseArray(bs, String.class);
			Map<String, Object> map = new HashMap<>();
			if (num < 0 && num > list.size() - 1) {
				return;
			}
			map.put("n", num);
			map.put("by", Base64.decode(list.get(num)));
			mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(map) + "FF", SystemCode.MQTT_QOS_ONE, false);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static byte[] toPrimitives(Byte[] oBytes) {
		byte[] bytes = new byte[oBytes.length];

		for (int i = 0; i < oBytes.length; i++) {
			bytes[i] = oBytes[i];
		}

		return bytes;
	}

	/**
	 * 
	 * <p>
	 * Title: AirConditionUpdateInfo
	 * </p>
	 * <p>
	 * Description: 更新空调信息
	 * </p>
	 * 
	 * @param data
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	private void AirConditionUpdateInfo(JSONObject data) throws MqttPersistenceException, MqttException, Exception {
		String mac = data.getString("mac");
		
		Map<String, Object> seqMap = new HashMap<>();
		String seq = data.getString("seq");
		seqMap.put("seq", seq);
		if (!(-2==redisAPI.ttl(mac+"seq"))&&seq.equals(redisAPI.get(mac+"seq"))) {//redis中有seq且和接收到的新的seq相同
			mqttManager.publish(mac,MqttSendUtil.Map2StrAndLen(seqMap),SystemCode.MQTT_QOS_ONE, false);
			return;
		}
		//redis中的没有seq，或seq不相同
		redisAPI.set(mac+"seq", seq);
		mqttManager.publish(mac,MqttSendUtil.Map2StrAndLen(seqMap),SystemCode.MQTT_QOS_ONE, false);
//		if (!(-2==redisAPI.ttl(mac+"ud"))) {
//			System.err.println("**********************"+mac+"return"+MqttSendUtil.Map2StrAndLen(seqMap));
//			mqttManager.publish(mac,
//					 MqttSendUtil.Map2StrAndLen(seqMap),SystemCode.MQTT_QOS_ONE, false);
//			return;
//		}
//		System.err.println(MqttSendUtil.Map2StrAndLen(seqMap)+"**********************"+mac+"enter"+DateFormats.getDateFormat());
		
		
//		redisAPI.set(mac+"ud",60, mac);//避免重复数据参与计算 60s内的带同一个mac算重复数据
		AirCondition condition = airConditionMapper.selectAirConditionById(mac);
		AirCondition dbCondition = new AirCondition();
		dbCondition.setMac(mac);

		if (redisAPI.exist(mac)) {
			
			dbCondition.setUseStatus(1);
			Integer duration = data.getInteger("dur"); // 时间段 单位s
			BigDecimal electricity = data.getBigDecimal("e").divide(new BigDecimal("1000")); // 电流 单位mA /1000 = A
//			BigDecimal voltage = data.getBigDecimal("v");//电压
			BigDecimal maxEle, voltage;
			Integer durEle = duration;
			HashMap<String, Object> eleMap = new HashMap<>();
			
//			AirUseInfo info = new AirUseInfo();
////			redisAPI.get(mac+"info");
//			info.setAirMac(mac);
//			info.setCreated(new Date());
//			info.setEleCurrent(electricity);
//			info.setPower(electricity.multiply(voltage).divide(new BigDecimal("1000"),6));
//			info.setVoltage(voltage);
////			
//			List<AirUseInfo> list = null;
////			list.add(info);
//			if (redisAPI.exist(mac+"info")) {
//				String macInfo = redisAPI.get(mac+"info");
//				list = JSONObject.parseArray(macInfo, AirUseInfo.class);
//				list.add(info);
//			}else {
//				list = new ArrayList<>();
//				list.add(info);
//			}
//			redisAPI.set(mac+"info", JSONObject.toJSONString(list));
			
			
//			Boolean bool = airConditionMapper.insertAirUseInfo(info);
//
//			if (!bool) {
//				throw new Exception("空调使用实时信息保存失败");
//			}
			
			if (redisAPI.exist(mac + "voltage")) {
				eleMap = JSONObject.parseObject(redisAPI.get(mac + "voltage"), HashMap.class);
				maxEle = new BigDecimal(eleMap.get("ele") + "").max(electricity);//电流
				voltage = new BigDecimal(eleMap.get("voltage") + "");//电压
				durEle += (Integer) eleMap.get("durEle");//时间
				if (durEle > SystemCode.VOLTAGE_UPDATE_TIME) {
					voltage = new BigDecimal(condition.getPower() / maxEle.floatValue());
					maxEle = electricity;
					durEle = 0;
					AirCondition airCondition = new AirCondition();
					airCondition.setMac(mac);
					airCondition.setVoltage(voltage);
					airConditionMapper.updateAirCondition(airCondition);
				}
			} else {
				maxEle = electricity;
				// AirCondition condition = airConditionMapper.selectAirConditionById(mac);
				voltage = condition.getVoltage();
			}
			eleMap.put("ele", maxEle.floatValue());
			eleMap.put("voltage", voltage.floatValue());
			eleMap.put("durEle", durEle);
			redisAPI.set(mac + "voltage", JSONObject.toJSONString(eleMap));
			// 获取上一次的数据
			String macValue = redisAPI.get(mac);
			HashMap<String, Object> map = JSONObject.parseObject(macValue, HashMap.class);

			Integer lastDuration = (Integer) map.get("duration");
			BigDecimal lastEle = new BigDecimal(map.get("electricity") + "");
			Long useTime = Long.valueOf(map.get("useTime") + "");
			BigDecimal balance = new BigDecimal(map.get("balance") + "");
			BigDecimal hour = new BigDecimal(map.get("hour") + "");
			String model = (String) map.get("model");
			// String lastSeq = (String) map.get("sequence");
			String authorization = (String) map.get("authorization");
			
			
			// 获取当前的设备数据

			// 电量I(单位:A) * 电压(单位: V) / 1000 得到功率 KW/h * 时间(单位: s)/3600 得到 用电量
			electricity = electricity.multiply(voltage).multiply(new BigDecimal(duration))
					.divide(new BigDecimal("3600"), 6).divide(new BigDecimal("1000"));
			String sequence = data.getString("seq");
			AppUser appuser = new AppUser();
			appuser.setAppusersId((Integer)map.get("userId"));
			AppUser appUser = appUserMapper.selectAppUser(appuser);
			
			JsonMqtt.setSuccess(sequence);
			// mqttManager.publish(mac, "{\"seq\":" + sequence +
			// "}",SystemCode.MQTT_QOS_ONE, false);
			map.put("duration", duration + lastDuration);
			map.put("electricity", lastEle.add(electricity));
			map.put("balance", null == appUser.getBalance() ? new BigDecimal(0) : appUser.getBalance());
			
			redisAPI.set(mac, JSONObject.toJSONString(map));
			boolean bool =false;
			boolean flag = false;
			if ("year".equals(model)) {
				flag = isExpire(model, balance, duration + lastDuration, useTime, new BigDecimal(0));
			} else {
				flag = isExpire(model, balance, duration + lastDuration, 0L, hour);
			}
			if("n".equals(authorization)){//没授权,普通用户
				if(flag) {//余额不足,关闭空调
					Thread.sleep(1000);
					System.out.println("=====================================================================");
					System.out.println("" + model + useTime + duration + lastDuration);
					System.out.println("=====================================================================");
					ControlVO controlVO = new ControlVO();
					controlVO.setMac(mac);
					controlVO.setModel(model);
					controlVO.setOrder("power");
					controlVO.setUserId((Integer) map.get("userId"));
					String shutdownSeq = UUID.randomUUID().toString().substring(30, 36);
					Map<String ,Object> orderMap = new HashMap<>();
					orderMap.put("order", "stop");
					orderMap.put("value", 0);
					orderMap.put("seq", shutdownSeq);
					controlVO.setOrderMap(orderMap);
					redisAPI.set(shutdownSeq, JSON.toJSONString(controlVO));
					mqttManager.publish(controlVO.getMac(), MqttSendUtil.Map2StrAndLen(orderMap),SystemCode.MQTT_QOS_ONE, false);
					System.out.println(MqttSendUtil.Map2StrAndLen(orderMap));
					return;
				}
			}
//			else if("y".equals(authorization)) {//授权
//				if((redisAPI.ttl(Integer.toString((int)map.get("userId")))) <= 0){//授权时间到
//					
//				}
//			}			
//			if (("n".equals(authorization) && flag )|| ("y".equals(authorization) && (redisAPI.ttl(Integer.toString((int)map.get("userId")))) <= 0)) {}
			//如果到24点关机
//			if (isTomorrow((Integer) map.get("userId"), duration)) {
//				// 如果24点了
//				sendOrder(mac, model, "stop");
//				sendOrder(mac, model, "start");
//			}
		}else {
			dbCondition.setUseStatus(0);
		}
		airConditionMapper.updateAirCondition(dbCondition);
	}

	/**
	 * 计算用电量，保存实时电压，电流，功率
	 * 
	 * @param data
	 * @throws Exception
	 */
	private void CountPower(JSONObject data) throws Exception {

		AirUseInfo info = new AirUseInfo();

		String mac = data.getString("mac");
		BigDecimal eleCurrent = data.getBigDecimal("e").divide(new BigDecimal("1000"));
		BigDecimal voltage = data.getBigDecimal("v");

		BigDecimal power = eleCurrent.multiply(voltage).divide(new BigDecimal("3600"), 6)
				.divide(new BigDecimal("1000"));
		Integer duration = data.getInteger("dur"); // 时间段 单位s
		BigDecimal energyUsed = power.multiply(new BigDecimal(duration));

		info.setAirMac(mac);
		info.setCreated(new Date());
		info.setEleCurrent(eleCurrent);
		info.setPower(power);
		info.setVoltage(voltage);

		Boolean flag = airConditionMapper.insertAirUseInfo(info);

		if (!flag) {
			throw new Exception("空调使用实时信息保存失败");
		}

		// 时间，用电量

		Map<String, Object> macInfoMap = JSONObject.parseObject(redisAPI.get(mac), HashMap.class);

		macInfoMap.put("electricity", new BigDecimal(macInfoMap.get("electricity")+"").add(energyUsed));

		macInfoMap.put("duration", (Integer) macInfoMap.get("duration") + duration);

		redisAPI.set(mac, JSONObject.toJSONString(macInfoMap));

	}

	private void sendOrder(String mac, String model, String order) throws MqttPersistenceException, MqttException {
		String uuid = UUID.randomUUID().toString();
		String seq = uuid.substring(uuid.length() - 6);
		ControlVO controlVO = new ControlVO();
		controlVO.setMac(mac);
		controlVO.setModel(model);
		controlVO.setOrder(order);
		boolean set = redisAPI.set(seq, JSON.toJSONString(controlVO));
		Map<String, Object> map = new HashMap<>();
		map.put("seq", seq);
		map.put("order", controlVO.getOrder());
		map.put("value", 0);
		mqttManager.publish(mac, MqttSendUtil.Map2StrAndLen(map), SystemCode.MQTT_QOS_ONE, false);
	}

	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	/**
	 * 
	 * <p>
	 * Title: AirConditionOperation
	 * </p>
	 * <p>
	 * Description: 添加空调操作记录,并更新空调信息
	 * </p>
	 * 
	 * @param data
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	private void AirConditionOperation(JSONObject data) throws MqttPersistenceException, MqttException {
		try {
			String sequence = data.getString("seq");
			if (!redisAPI.exist(sequence)) {
				return;
			}
			String orderJson = redisAPI.get(sequence);
			ControlVO orderMap = JSON.parseObject(orderJson, ControlVO.class);
			String mac = orderMap.getMac();
			String order;
			if(orderMap.getOrderMap()==null) {
				order = (String) orderMap.getOrder();
			}else {
				order = (String) orderMap.getOrderMap().get("order");
			}
			 
			//String order = (String) orderMap.getOrder();
			ConditionInfo conditionInfo = airConditionMapper.getConditionInfoByMac(mac);
			if ("close".equals(order) && "stop".equals(conditionInfo.getStatus())
					|| ("open".equals(order) && "start".equals(conditionInfo.getStatus()))) {
				// 已经开/关的 设备 重复指令 不执行
				return;
			}

			// 授权逻辑
			Integer userId = orderMap.getUserId();
			String authorMac = redisAPI.get(userId + "");
			boolean author = false;
			if (mac.equals(authorMac)) {
				author = true;
			}

			if ("n".equals(data.getString("sup"))) {
				OperationRecord record = new OperationRecord();
				record.setAppusersId(userId);
				record.setMac(mac);
				record.setOrder(order);
				record.setExecute(false);
				operationRecordMapper.addOperationRecord(record);
				return;
			}
			Map macJson = JSON.parseObject(redisAPI.get(mac), HashMap.class);
			if ("stop".equals(order)) {
				Long duration = Long.valueOf(macJson.get("duration") + "");
				String model = (String) macJson.get("model");
				AppUser appUser = appUserMapper.selectById(userId);
				// if (!author && appUser.getModel().equals("hour")) {
				// appUser.setUseTime(appUser.getUseTime()-duration);
				// }
				// 修改空调使用状态
				AirCondition airCondition = new AirCondition();
				airCondition.setMac(mac);
				airCondition.setUseStatus(0);
				airConditionMapper.updateAirCondition(airCondition);

				// 普通用户添加碳指标,生成订单
				if (!"y".equals(macJson.get("authorization"))/* || redisAPI.ttl(Integer.toString((Integer)macJson.get("userId"))) <= 0*/) {
					CarbonIndicators carbon = new CarbonIndicators();
					carbon.setAppusersId(userId);
					BigDecimal electricity = new BigDecimal(macJson.get("electricity") + "");
	
					BigDecimal saveElect = electricity.divide(new BigDecimal("5"));
					BigDecimal addcarbon = saveElect.multiply(new BigDecimal("0.486"));
					carbon.setCatbon(addcarbon);
	
					carbon.setElectricity(electricity);
					carbon.setUseTime(duration);
					carbon.setMac(mac);
					OperationRecord record = operationRecordMapper.getLastOpendRecord(userId, "start");
					carbon.setOpenTime(record.getCreated());
					carbon.setCloseTime(record.getCreated().after(new Date()) ? getCloseDate(new Date()) : new Date());
					carbonIndicatorsMapper.addCarbonIndicators(carbon);
					appUser.setSaveElect(appUser.getSaveElect().add(saveElect));
					appUser.setCarbon(appUser.getCarbon().add(addcarbon));
					appUser.setElectricity(appUser.getElectricity().add(electricity));
					appUser.setTotalTime(appUser.getTotalTime() + duration);
					appUserMapper.updateAppUser(appUser);
					//生成订单
					modifyAndAdd((Integer) macJson.get("userId"), duration, (String) macJson.get("mac"), model);
				}
				

			}

			if ("start".equals(order)) {
				// 修改空调使用状态
				AirCondition airCondition = new AirCondition();
				airCondition.setMac(mac);
				airCondition.setUseStatus(1);
				airConditionMapper.updateAirCondition(airCondition);

				AirCondition condition = airConditionMapper.selectAirConditionByMacAndUserId(mac, userId);
				AppUser appUser = appUserMapper.selectById(userId);

				Map<String, Object> map = new HashMap<>();
				map.put("mac", mac);

				if (null == condition.getTime() || condition.getTime() < System.currentTimeMillis()) {
					map.put("model", "hour");
					map.put("useTime", 0L);
				} else {
					map.put("useTime", condition.getTime());
					map.put("model", "year");
				}

				map.put("balance", null == appUser.getBalance() ? new BigDecimal(0) : appUser.getBalance());

				map.put("authorization", "n");
				if (author) {
					map.put("useTime", redisAPI.ttl(userId + ""));
					map.put("authorization", "y");
				}

				map.put("hour", condition.getHour());
				map.put("userId", userId);
				map.put("electricity", 0);
				map.put("duration", new BigDecimal(0));
				map.put("beginTime", System.currentTimeMillis());
				redisAPI.set(mac, JSON.toJSONString(map));
			}

			// 添加操作记录
			OperationRecord record = new OperationRecord();
			record.setAppusersId(userId);
			record.setMac(mac);
			record.setOrder(order);
			record.setExecute(true);
			Integer result = operationRecordMapper.addOperationRecord(record);

			// 修改空调信息
			switch (orderMap.getOrder()) {
			case "power":
				if("start".equals(order)) {
					conditionInfo.setAppuserId(userId);
					
				}else if("stop".equals(order)) {
					conditionInfo.setAppuserId(null);
					conditionInfo.setTiming(null);
					if(!"y".equals(macJson.get("authorization"))){
						timingTasksMapper.deleteTiming(mac,order);
					}
					
				}
				conditionInfo.setStatus(order);
				break;
			case SystemCode.AIR_CONDITION_ORDER_MODEL: // 模式
				conditionInfo.setModel(
						SystemCode.AIR_CONDITION_MODEL[Integer.parseInt(orderMap.getOrderMap().get("value") + "")]);
				break;
			case SystemCode.AIR_CONDITION_ORDER_WIND_SPEED: // 风速
				Integer index = Integer.parseInt(orderMap.getOrderMap().get("value") + "");
				conditionInfo.setWindSpeed(SystemCode.AIR_CONDITION_WIND_SPEED[index == 0 ? index : index - 1]);
				break;
			case SystemCode.AIR_CONDITION_ORDER_SWING: // 风向
				conditionInfo.setSwing(
						SystemCode.AIR_CONDITION_SWING[Integer.parseInt(orderMap.getOrderMap().get("value") + "")]);
				break;
			case SystemCode.AIR_CONDITION_ORDER_SLEEP: // 睡眠
				conditionInfo.setSleep(
						SystemCode.AIR_CONDITION_SLEEP[Integer.parseInt(orderMap.getOrderMap().get("value") + "")]);
				break;
			case SystemCode.AIR_CONDITION_ORDER_DRY_HOT: // 干燥 辅热
				conditionInfo.setDryHot(
						SystemCode.AIR_CONDITION_DRY_HOT[Integer.parseInt(orderMap.getOrderMap().get("value") + "")]);
				break;
			case SystemCode.AIR_CONDITION_ORDER_STRONG: // 强力
				conditionInfo.setStrong(
						SystemCode.AIR_CONDITION_STRONG[Integer.parseInt(orderMap.getOrderMap().get("value") + "")]);
				if (SystemCode.AIR_CONDITION_STRONG[1].equals(conditionInfo.getStrong())) {
					conditionInfo.setModel(SystemCode.AIR_CONDITION_MODEL[4]);
					conditionInfo.setTemp(16);
					conditionInfo.setWindSpeed(SystemCode.AIR_CONDITION_WIND_SPEED[3]);
				}
				break;

			case SystemCode.AIR_CONDITION_ORDER_TEMP: // 温度
				conditionInfo.setTemp(Integer.valueOf(orderMap.getOrderMap().get("value") + ""));
				break;
			}
			orderMap.setStatus("finsh");
			redisAPI.set(sequence, 300,JSON.toJSONString(orderMap));
			
			Integer res = airConditionMapper.updateConditionInfoByMac(conditionInfo);
			if(res == 1 && "stop".equals(order)) {
				redisAPI.delete(mac);
			}
			//redisAPI.delete(sequence);
			if (result != SystemCode.ADD_UPDATE_FAILE) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return;
		}
	}

	/**
	 * 
	 * <p>
	 * Title: isExpire
	 * </p>
	 * <p>
	 * Description: 用于根据模式判断用户使用时间是否到期
	 * </p>
	 * 
	 * @param model
	 *            year/hour
	 * @param useTime
	 *            可使用时长
	 * @param duration
	 *            已使用时长
	 * @return
	 *//*
		 * public static boolean isExpire(String model, long useTime, long duration) {
		 * switch (model) { case "year": return new Date().after(new Date(useTime));
		 * 
		 * case "hour": return useTime <= duration; } return false; }
		 */

	/**
	 * 
	 * <p>
	 * Title: isExpire
	 * </p>
	 * <p>
	 * Description: 用于根据模式判断用户使用时间是否到期
	 * </p>
	 * 
	 * @param model
	 *            year/hour
	 * @param balance
	 *            用户余额
	 * @param duration
	 *            已使用时长
	 * @param useTime
	 *            可使用时长
	 * @param hour
	 *            包时价格
	 * @return
	 */
	public static boolean isExpire(String model, BigDecimal balance, long duration, long useTime, BigDecimal hour) {
		switch (model) {
		case "year":
			return new Date().after(new Date(useTime));

		case "hour":
			int time = (int) Math.ceil(Double.parseDouble(duration + "") / 60);// 使用时间：分钟

			BigDecimal used = new BigDecimal(time);
			BigDecimal secend = new BigDecimal(60);
			// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
			// int c = a.compareTo(b); // 结果 C = 1
			return balance.compareTo(used.divide(secend, 3,BigDecimal.ROUND_HALF_UP).multiply(hour)) == 1 ? false : true;
		}
		return false;
	}

	/**
	 * <p>
	 * Title: isTomorrow
	 * </p>
	 * <p>
	 * Description: 判断是否过了24点
	 * </p>
	 * 
	 * @param appusersId
	 * @param duration
	 * @return
	 */
	public boolean isTomorrow(Integer appusersId, Integer duration) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		OperationRecord record = operationRecordMapper.getLastOpendRecord(appusersId, "start");
		long time = record.getCreated().getTime() + (duration * 1000);
		return time >= zero.getTime();
	}

	/**
	 * 
	 * <p>
	 * Title: getCloseDate
	 * </p>
	 * <p>
	 * Description: 凌晨5分钟内定位前一天的时间
	 * </p>
	 * 
	 * @param closeDate
	 * @return
	 */
	public Date getCloseDate(Date closeDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return closeDate.getTime() - 3000 > zero.getTime() ? closeDate : new Date(zero.getTime() - 1);
	}

	/**
	 * 生成消费记录，修改用户余额
	 * 
	 * @param userId
	 *            用户id
	 * @param userTime
	 *            使用时间
	 * @param mac
	 *            空调mac地址
	 * @param useType
	 *            year/hour
	 */
	private void modifyAndAdd(Integer userId, long userTime, String mac, String useType) {
		Order order = new Order();
		order.setIsRead(false);
		AppUser appUser = appUserMapper.selectById(userId);
		AirCondition condition = airConditionMapper.selectAirConditionById(mac);

		int time = (int) Math.ceil(Double.parseDouble(userTime + "") / 60);// 使用时间 分钟
		if(userTime<105) {
			time = 2;
		}

		BigDecimal sendMoney = condition.getHour().multiply(new BigDecimal(time)).divide(new BigDecimal(60), 3,BigDecimal.ROUND_HALF_DOWN);
		order.setTheoprice(sendMoney);
		// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
		// int c = a.compareTo(b); // 结果 C = 1
		
		if ("hour".equals(useType)) {
			if (1 == appUser.getBalance().compareTo(sendMoney)) {// 余额大于应付金额
				order.setRealfee(sendMoney);
				appUser.setBalance(appUser.getBalance().subtract(sendMoney));
			} else {
				order.setRealfee(appUser.getBalance());
				appUser.setBalance(new BigDecimal(0));
			}
			appUserMapper.updateAppUser(appUser);
			order.setPaymethod("余额");
			order.setPaystatus("yes");
			order.setModel("hour");
			order.setRechargeId(2);
		} else {
			order.setRealfee(new BigDecimal(0));
			order.setModel("year");
		}
		order.setAirmac(mac);
		order.setIsRead(false);
		order.setAppusersId(userId);
		order.setTelephone(appUser.getTelephone());
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaytime(DateFormats.getDateFormat());
		order.setPaytype("cost");
		order.setNum(time);
		orderMapper.insertOrder(order);

	}
	
	//判断是否开机
	public boolean isStart(Integer status) {
		if(status%2==0) {
			return false;
		}else {
			return true;
		}
		
		
	}
}
