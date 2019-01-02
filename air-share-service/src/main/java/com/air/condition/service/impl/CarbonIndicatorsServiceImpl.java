package com.air.condition.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.aircondition.mapper.CarbonIndicatorsMapper;
import com.air.aircondition.mapper.OperationRecordMapper;
import com.air.condition.service.CarbonIndicatorsService;
import com.air.constant.SystemCode;
import com.air.crmuser.mapper.CrmUserMapper;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.CarbonIndicators;
import com.air.pojo.entity.CarbonLevel;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.entity.Repair;
import com.air.pojo.vo.PersonalRecordVO;
import com.air.pojo.vo.RepairVO;
import com.air.redis.RedisAPI;
import com.air.repair.mapper.RepairMapper;
import com.air.user.mapper.AppUserMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class CarbonIndicatorsServiceImpl implements CarbonIndicatorsService {
	
	@Autowired
	private CarbonIndicatorsMapper carbonMapper;
	@Autowired
	private OperationRecordMapper recordMapper;
	@Autowired
	private AppUserMapper appUserMapper;
	@Autowired
	private RedisAPI redisAPI;
	@Autowired
	private AirConditionMapper airConditionMapper;
	@Autowired
	private CrmUserMapper crmUserMapper;
	@Autowired
	private RepairMapper repairMapper;
	
	/**
	 * 根据用户查询碳指标
	 */
	@Override
	public List<CarbonIndicators> selectCarbonIndicators(Integer appusersId, String type, Integer pageNum, Integer pageSize) {
		String format = "";
		switch (type) {
		case "month":
			format = "%Y-%m";
			break;
		case "year":
			format = "%Y";
			break;
		}
		if ("daily".equals(type)) {
			PageHelper.startPage(pageNum, pageSize);
			List<CarbonIndicators> list = carbonMapper.selectCarbonIndicators(appusersId, format);
			return new PageInfo(list).getList();
		}
		List<CarbonIndicators> list = carbonMapper.selectCarbonIndicators(appusersId, format);
												   
		List<String> dateList = getDateList(type);
		for (CarbonIndicators item : list) {
			dateList.remove(item.getDate());
		}
		
		for (String dateStr : dateList) {
			CarbonIndicators carbon = new CarbonIndicators();
			carbon.setCatbon(new BigDecimal(0));
			carbon.setDate(dateStr);
			carbon.setElectricity(new BigDecimal(0));
			carbon.setUseTime(0l);
			list.add(0, carbon);
		}
		return list;
	}
	private List<String> getDateList(String type) {
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		if ("month".equals(type)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			for (int i = 0; i < 12; i++) {
				cal.set(Calendar.MONTH, i);
				list.add(format.format(cal.getTime()));
			}
			return list;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		for (int i = 0; i < 6; i++) {
			list.add(format.format(cal.getTime()));
			cal.add(Calendar.YEAR, -1);
		}
		return list;
	}
	
	/**
	 * APP用户个人记录
	 */
	@Override
	public PersonalRecordVO getPersonalRecord(Integer appusersId, String mac) {
		AppUser appUser = appUserMapper.selectById(appusersId);
		PersonalRecordVO personalRecordVO = new PersonalRecordVO();
		CarbonLevel carbonLevel = carbonMapper.selectLevelByCarbon(appUser.getCarbon());
		personalRecordVO.setCarbon(appUser.getCarbon());
		personalRecordVO.setElectricity(appUser.getElectricity());
		personalRecordVO.setSaveElect(appUser.getSaveElect());
		personalRecordVO.setBinding(false);
		personalRecordVO.setCarbonLevel(carbonLevel.getCarbonLevel());
		OperationRecord operRecord = recordMapper.getLastOperRecord(appusersId);
		AirCondition condition = airConditionMapper.selectAirConditionById(mac);
		if (StringUtils.isNotEmpty(mac) && condition != null && condition.getMac() != null) {
			AppUserAir userAir = airConditionMapper.selectAirConditionByIdAndUser(mac, appusersId);
			AirCondition airCondition = airConditionMapper.selectAirConditionById(mac);
			if (airCondition.getEnterpriseId() != null) {
				CrmUser crmUser = new CrmUser();
				crmUser.setCrmuserId(airCondition.getEnterpriseId());
				CrmUser user = crmUserMapper.selectCrmUser(crmUser).get(0);
				personalRecordVO.setName(user.getUsername());
				personalRecordVO.setEnterpriseId(airCondition.getEnterpriseId());
			}
			
			personalRecordVO.setMac(mac);
			RepairVO repair = new RepairVO();
			repair.setAirmac(mac);
			repair.setUserId(appusersId);
			repair.setStatus(SystemCode.REPAIR_PROCESSING);
			List<Repair> repairList = repairMapper.selectRepairList(repair);
			if (repairList != null && repairList.size() > 0) {
				personalRecordVO.setRepairId(repairList.get(0).getRepairId());
				personalRecordVO.setService(repairList.get(0).getService());
			}
			
			if (userAir != null) {
				personalRecordVO.setBinding(true);
				personalRecordVO.setNote(userAir.getNote());
			}
		}
		//没有使用该设备
		if (operRecord == null || "stop".equals(operRecord.getOrder()) || !redisAPI.exist(operRecord.getMac())) {
			personalRecordVO.setUseStatus(false);
			personalRecordVO.setUseTime(appUser.getUseTime() == null? 0:appUser.getUseTime());
			if (StringUtils.isNotEmpty(mac)) {
				//personalRecordVO.setUseTime(appUser.getTotalTime());
				Long useTime = carbonMapper.countUserAirUseTime(appusersId, mac);
				personalRecordVO.setUseTime(useTime);
				BigDecimal electricity = carbonMapper.countUserAirElectricity(appusersId, mac);
				personalRecordVO.setElectricity(electricity);
			}
			
			personalRecordVO.setCurTime(0l);
			return personalRecordVO;
		}
		String macData = redisAPI.get(operRecord.getMac());
		HashMap<String, Object> macMap = JSONObject.parseObject(macData, HashMap.class);
		Long duration = Long.parseLong(macMap.get("duration")+"");
		personalRecordVO.setUseStatus(true);
		if(appUser.getUseTime()==null) {
			personalRecordVO.setUseTime(0L);
		}else {
			personalRecordVO.setUseTime(appUser.getUseTime()-duration);
		}
		
		personalRecordVO.setCurTime(duration);
		
		if (StringUtils.isNotEmpty(mac)) {
			//personalRecordVO.setUseTime(appUser.getTotalTime()+duration);
			Long useTime = carbonMapper.countUserAirUseTime(appusersId, mac);
			BigDecimal electricity = carbonMapper.countUserAirElectricity(appusersId, mac);
			personalRecordVO.setUseTime(useTime);
			personalRecordVO.setElectricity(electricity);
			
		}
		if(redisAPI.get(mac)!= null) {
			Map map = JSON.parseObject(redisAPI.get(mac), HashMap.class);
			Integer appuserId =  (Integer)map.get("userId");
			Long beginTime = (Long)map.get("beginTime");
			if(appuserId == appusersId) {
				personalRecordVO.setBeginTime(beginTime);
			}
			
				
		}
		
		return personalRecordVO;
	}

}
