package com.air.quartz.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.condition.service.MqttManager;
import com.air.constant.SystemCode;
import com.air.mqtt.service.impl.MqttPushCallBackService;
import com.air.notice.mapper.NoticeMapper;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.Notice;
import com.air.pojo.entity.TimingTasks;
import com.air.pojo.vo.ControlVO;
import com.air.redis.RedisAPI;
import com.air.timingtasks.mapper.TimingTasksMapper;
import com.air.user.mapper.AppUserMapper;
import com.air.utils.JpushClientUtil;
import com.air.utils.MqttSendUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.jpush.api.push.PushResult;

public class TimingTaskService {
	@Autowired
	private TimingTasksMapper timingTasksMapper;
	@Autowired
	private AppUserMapper appUserMapper;
	@Autowired
	private RedisAPI redisAPI;
	@Autowired
	private MqttManager mqttManager;
	@Autowired
	private AirConditionMapper airConditionMapper;
	@Autowired
	private NoticeMapper noticeMapper;
	
	/**
	 * 
	 * <p>Title: execute</p>
	 * <p>Description: 5分钟一次 设备的定时开关机  推送消息</p>
	 */
	public void execute() {
		 // 推送消息
        List<Notice> notices = noticeMapper.selectNoticeListBySend(false);
        List<Integer> sendList = new ArrayList<>();
        for (Notice notice : notices) {
        	HashMap<String, String> extras = new HashMap<>();
        	extras.put("title", notice.getTitle());
        	extras.put("content", notice.getContent());
        	extras.put("type", "message");
        	try {
        		PushResult result = JpushClientUtil.push(notice.getTelephone(),notice.getContent(),notice.getTitle(), extras, notice.getCreated().getTime());
        		if (result.isResultOK()) {
        			sendList.add(notice.getNoticeId());
        		}
			} catch (Exception e) {
				continue;
			}
		}
        if (sendList.size() >= 1) {
        	noticeMapper.updateNoticeByIdList(sendList);
		}
	}

	// 每20秒执行一次
	public void timingShutdown() {
        List<TimingTasks> list = timingTasksMapper.selectNotPerformedTask();
        for (TimingTasks timingTasks : list) {
        	Date date = timingTasks.getExeTime();
			Date nowDate = new Date();
			//达到执行时间,执行关机命令
			if(date.compareTo(nowDate)<0) {
				//boolean tager = false;
	        	Long ttl = redisAPI.ttl(timingTasks.getUserId()+"");
	        	AppUser appUser = appUserMapper.selectById(timingTasks.getUserId());
	        	//AirCondition condition = airConditionMapper.selectAirConditionById(timingTasks.getMac());
	        	
	        	ConditionInfo conditionInfo = airConditionMapper.getConditionInfoByMac(timingTasks.getMac());
//	        	if (MqttPushCallBackService.isExpire(appUser.getModel(),appUser.getBalance(), 0, condition.getTime(),condition.getHour()) && ttl <= 0 && "start".equals(timingTasks.getOrder())) {
//					tager = true;
//				}
//	        	if (tager || conditionInfo.getStatus().equals(timingTasks.getOrder())) {
//	        		timingTasksMapper.deleteTimingTasks(timingTasks.getMac(), timingTasks.getUserId());
//	        		conditionInfo.setTiming(null);
//	        		airConditionMapper.updateConditionInfoByMac(conditionInfo);
//	        		continue;
//	        	}
	        	/**
	        	 * 1.非定时关机指令
	        	 * 2.空调原本状态为关机
	        	 * 3.定时用户与空调当前用户不一致
	        	 * */
	        	if ("start".equals(timingTasks.getOrder()) || conditionInfo.getStatus().equals(timingTasks.getOrder()) || timingTasks.getUserId()!=conditionInfo.getAppuserId()) {
	        		timingTasksMapper.deleteTimingTasks(timingTasks.getMac(), timingTasks.getUserId());
	        		conditionInfo.setTiming(null);
	        		airConditionMapper.updateConditionInfoByMac(conditionInfo);
	        		continue;
	        	}
	        	
	        	Map<String, Object> map = new HashMap<>();
	    		String seq = UUID.randomUUID().toString().substring(30, 36);
	    		map.put("seq", seq);
	    		map.put("order", timingTasks.getOrder());
	    		map.put("value", 0);
	    		
	    		ControlVO controlVO = new ControlVO();
	    		
				controlVO.setUseTime(ttl > 0? ttl: appUser.getUseTime());
				controlVO.setMac(timingTasks.getMac());
				controlVO.setModel(appUser.getModel());
				controlVO.setOrderMap(map);
				controlVO.setOrder("power");
				controlVO.setUserId(timingTasks.getUserId());
				redisAPI.set(seq, JSON.toJSONString(controlVO));
				System.out.println(JSON.toJSONString(map));
				try {
						timingTasks.setExecute(true);
						timingTasksMapper.updateTimingTasks(timingTasks);
						conditionInfo.setTiming(null);
		        		airConditionMapper.updateConditionInfoByMac(conditionInfo);
		        		
						mqttManager.publish(controlVO.getMac(), MqttSendUtil.Map2StrAndLen(map), SystemCode.MQTT_QOS_ONE, false);
					
				} catch (MqttException e) {
					continue;
				}
			}
		}  
    }
	
	/**
	 * 
	 * <p>Title: releaseBindingByAppUserTiming</p>
	 * <p>Description: 绑定后一个月App用户自动解除绑定</p>
	 * @param appuserAir
	 * @return
	 */
	public void releaseBindingByAppUserTiming() {
		
		List<AppUserAir> list = airConditionMapper.selectAppUserAirTimeOut();
		
		if (!list.isEmpty()) {
			Boolean bool = airConditionMapper.deleteAppUserAir(list);
			if (bool) {
				addNotice(list);
			}
		}
		
	}
	
	/**
	 * 
	 * <p>Title: addNotice</p>
	 * <p>Description: 绑定后一个月App用户自动解除绑定后添加提示消息</p>
	 * @param appuserAir
	 * @return
	 */
	private void addNotice(List<AppUserAir> list) {
		List<Notice> notices = new ArrayList<Notice>();
		
		for (AppUserAir appUserAir : list) {
			Notice notice = new Notice();
			notice.setTitle("友情提示");
			notice.setAppusersId(appUserAir.getAppuserAirId());
			notice.setContent("由于您的绑定时间达到或超过一个月已自动解绑,如再次使用时请重新绑定");
			notices.add(notice);
		}
		
		Boolean bool = noticeMapper.insertNoticeList(notices);
		
	}

	public void executeByThirtySeconds() {
		// 获取6个字符的序列号的 指令 keys 集合
		Set<String> keys = redisAPI.keys("??????");
		for (String key : keys) {
			ControlVO controlVO = JSONObject.parseObject(redisAPI.get(key), ControlVO.class);
			try {
				mqttManager.publish(controlVO.getMac(), JSON.toJSONString(controlVO.getOrderMap()), SystemCode.MQTT_QOS_ONE, false);
			} catch (MqttException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
