package com.air.condition.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.aircondition.mapper.OperationRecordMapper;
import com.air.aircondition.mapper.SeriesMapper;
import com.air.condition.service.ConditionMqttService;
import com.air.condition.service.ConditionService;
import com.air.condition.service.MqttManager;
import com.air.constant.SystemCode;
import com.air.crmuser.mapper.CrmUserMapper;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Notice;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.AirConditionVO;
import com.air.pojo.vo.AppUserAirVo;
import com.air.pojo.vo.BindingVO;
import com.air.pojo.vo.ControlVO;
import com.air.pojo.vo.SeriesVO;
import com.air.redis.RedisAPI;
import com.air.timingtasks.mapper.TimingTasksMapper;
import com.air.utils.EmptyUtils;
import com.air.utils.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class ConditionServiceImpl implements ConditionService {
	@Autowired
	private AirConditionMapper airmapper;
	@Autowired
	private OperationRecordMapper recordMapper;
	@Autowired
	private CrmUserMapper crmUserMapper;
	@Autowired
	private SeriesMapper seriesMapper;
	@Autowired
	private RedisAPI redisApi;
	@Autowired
	private MqttManager mqttManager;
	@Autowired
	private ConditionMqttService conditionMqttService;
	@Autowired
	private TimingTasksMapper timingtasksMapper;
	
	/**
	 * 根据ID查找空调信息
	 */
	@Override
	public AirCondition selectAirConditionById(String airConditionId) {
		return airmapper.selectAirConditionById(airConditionId);
	}
	
	/**
	 * 
	 */
	@Override
	public AirCondition selectAirConditionByMacAndUserId(String mac,Integer appusersId) {
		return airmapper.selectAirConditionByMacAndUserId(mac,appusersId);
	}
	
	/**
	 * 根据APP用户ID查找绑定的空调
	 */
	@Override
	public List<AppUserAir> selectAirConditionByUserId(Integer appuserId) {
		return airmapper.selectAirConditionByUserId(appuserId);
	}
	
	/**
	 * 根据App用户ID 空调MAC修改空调备注
	 */
	@Override
	public Integer updateAirConditionNote(AppUserAir appuserAir) {
		return airmapper.updateAirConditionNote(appuserAir);
	}
	
	/**
	 * 用户绑定空调
	 */
	@Override
	public Integer bindingAirConditionByAppUser(AppUserAir appuserAir) {
		try {
			AppUserAir exist = airmapper.selectAirConditionByIdAndUser(appuserAir.getMac(), appuserAir.getAppusersId());
			//不能重复绑定
			if(exist != null && exist.getAppuserAirId() != null) {
				return SystemCode.ADD_UPDATE_FAILE;
			}
			
			return airmapper.addAppUserAir(appuserAir);
		}catch(Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return SystemCode.ADD_UPDATE_FAILE;
		}
	}

	/**
	 * 获取空调列表
	 */
	@Override
	public PageInfo<AirConditionVO> selectAirConditionList(AirConditionVO airCondition) {
		PageHelper.startPage(airCondition.getPageNum(), airCondition.getPageSize());
		List<AirConditionVO> list = airmapper.selectAllAirCondition(airCondition);
		PageInfo<AirConditionVO> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 系列列表
	 */
	@Override
	public PageInfo<Series> selectSeriesList(SeriesVO series) {
		PageHelper.startPage(series.getPageNum(), series.getPageSize());
		List<Series> list = airmapper.selectSeriesList(series);
		PageInfo<Series> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	
	/**
	 * 获取全部空调
	 */
	@Override
	public List<AirConditionVO> selectAllAirCondition() {
		return airmapper.selectAllAirCondition(new AirConditionVO());
	}
	
	
	/**
	 * 删除空调信息
	 */
	@Override
	public Integer deleteCondition(String mac) {
		Boolean boolean1 = airmapper.deleteConditionInfo(mac);
		return airmapper.deleteAirCondition(mac);
	}
	
	/**
	 * 修改空调信息
	 */
	@Override
	public Integer updateAirCondition(AirCondition airCondition) {
		return airmapper.updateAirCondition(airCondition);
	}
	
	/**
	 * 添加空调设备
	 */
	@Override
	public Integer addAirCondition(AirCondition airCondition) {
		ConditionInfo conditionInfo = new ConditionInfo();
		conditionInfo.setMac(airCondition.getMac());
		conditionInfo.setDryHot(SystemCode.AIR_CONDITION_DRY_HOT[0]);
		conditionInfo.setStatus(SystemCode.AIR_CONDITION_ORDER_STOP);
		conditionInfo.setModel(SystemCode.AIR_CONDITION_MODEL[0]);
		conditionInfo.setSleep(SystemCode.AIR_CONDITION_SLEEP[0]);
		conditionInfo.setStrong(SystemCode.AIR_CONDITION_STRONG[0]);
		conditionInfo.setSwing(SystemCode.AIR_CONDITION_SWING[0]);
		conditionInfo.setWindSpeed(SystemCode.AIR_CONDITION_WIND_SPEED[0]);
		AirCondition  dbaircondiction = airmapper.selectAirConditionById(airCondition.getMac());
		if(!EmptyUtils.isEmpty(dbaircondiction)) {
			return -1;
		}
		airmapper.addConditionInfo(conditionInfo );
		return airmapper.addAirCondition(airCondition);
	}
	
	/**
	 * 空调绑定业主,编组
	 */
	@Override
	public BindingVO bindingCondition(BindingVO bindingVO,Integer appUserId) {
		AirCondition airCondition = new AirCondition();
		try {
			Map map = new HashMap<>();
			map.put("username", bindingVO.getUsername());
			map.put("roleName", "owner");
			CrmUser crmUser = crmUserMapper.getUserByUserNameAndRole(map);
//			CrmUser crmUser = crmUserMapper.selectCrmUserByUsername(bindingVO.getUsername());
			if (crmUser == null) {
				return null;
			}
			
			
			BeanUtils.copyProperties(bindingVO, airCondition);
			airCondition.setEnterpriseId(crmUser.getCrmuserId());//设置业主id
			AirCondition condition = airmapper.selectAirConditionById(airCondition.getMac());
			if (condition == null || condition.getMac() == null) {
				airmapper.addAirCondition(airCondition);
				return bindingVO;
			}
			airmapper.updateAirCondition(airCondition);
			//空调绑定维修人员
			AppUserAir appuserAir = new AppUserAir();
			appuserAir.setAppusersId(appUserId);
			appuserAir.setMac(airCondition.getMac());
			bindingAirConditionByAppUser(appuserAir);
			//授权两小时
			redisApi.set(appUserId+"", 7200, airCondition.getMac());
			return bindingVO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *结束安装新空调
	 */
	public String finshInstall(Integer appUserId,String mac) {
		AppUserAir appuserAir = new AppUserAir();
		appuserAir.setAppusersId(appUserId);
		appuserAir.setMac(mac);
//		airmapper.deleteUserAirByUserAndAir(appuserAir);
		//删除定时任务
		timingtasksMapper.deleteTimingTasks(mac, appUserId);
		//解除绑定,并关闭空调
//		Integer result = releaseBindingByCrmUser(appuserAir);
		//如果空调是开的,则强制关掉空调
		ConditionInfo conditionInfo = airmapper.getConditionInfoByMac(mac);
		if("start".equals(conditionInfo.getStatus())) {
				ControlVO controlVO =new ControlVO();
				controlVO.setMac(mac);
				controlVO.setOrder("power");
				try {
					conditionMqttService.controlAirCondition(controlVO, appuserAir.getAppusersId());
				}catch(Exception e) {
					e.printStackTrace();
				}
		}
		redisApi.delete(appUserId+"");
		return "结束安装成功";
	}
	
	/**
	 * 获取用户的最后一条操作指令
	 */
	@Override
	public OperationRecord getLastOperRecord(Integer appuserId) {
		return recordMapper.getLastOperRecord(appuserId);
	}
	
	/**
	 * app用户自己解除绑定
	 */
	@Override
	public Integer releaseBindingByAppUser(AppUserAir appuserAir) {
		ConditionInfo conditionInfo = airmapper.getConditionInfoByMac(appuserAir.getMac());
//		if("start".equals(conditionInfo.getStatus()) && redisApi.exist(appuserAir.getMac()+"")) {
//			return -1;
//		}
		AirCondition airCondition = airmapper.selectAirConditionByMacAndUserId(appuserAir.getMac(), appuserAir.getAppusersId());
		if(EmptyUtils.isEmpty(airCondition)) {
			return -2; //未绑定该空调
		}
		if(EmptyUtils.isNotEmpty(airCondition.getTime())) {
			return -3; //存在包年信息
		}
		if(redisApi.exist(appuserAir.getMac())) {
			Map macJson = JSON.parseObject(redisApi.get(appuserAir.getMac()), HashMap.class);
			if(appuserAir.getAppusersId()==(Integer)macJson.get("userId")) {
				return -1;
			}
		}
		Integer result = airmapper.deleteUserAirByUserAndAir(appuserAir);
		return result;
	}
	/**
	 * crm端管理员强制解除绑定
	 */
	@Override
	public Integer releaseBindingByCrmUser(AppUserAir appuserAir){
		ConditionInfo conditionInfo = airmapper.getConditionInfoByMac(appuserAir.getMac());
		//如果空调是开的,则强制关掉空调
		if("start".equals(conditionInfo.getStatus())) {
			ControlVO controlVO =new ControlVO();
			controlVO.setMac(appuserAir.getMac());
			controlVO.setOrder("power");
			try {
				conditionMqttService.controlAirCondition(controlVO, appuserAir.getAppusersId());
				AirCondition aircondition = new AirCondition();
				aircondition.setUseStatus(0);
				airmapper.updateAirCondition(aircondition);
			}catch(Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		Integer result = airmapper.deleteUserAirByUserAndAir(appuserAir);
		return result;
	}
	
	
	
	/**
	 * 获取空调的指令状态
	 */
	@Override
	public ConditionInfo getConditionInfoByMac(String mac, Integer appUserId) {
		OperationRecord lastRecord= recordMapper.getLastOperRecordByMac(mac);
		ConditionInfo conditionInfo = airmapper.getConditionInfoByMac(mac);
		conditionInfo.setUse(true);
		if(EmptyUtils.isEmpty(lastRecord)) {
			conditionInfo.setUse(false);
		}else {
			if ("start".equals(conditionInfo.getStatus()) &&  lastRecord.getAppusersId() != appUserId) {
				conditionInfo.setUse(false);
			}
		}
		
		return conditionInfo;
	}

	@Override
	public List<AppUserAirVo> queryBindingInfoByMac(String mac) {
		
		return airmapper.selectBindingInfoByMac(mac);
	}

	@Override
	public List<String> queryAllAirMac() {
		
		return airmapper.selectAllAirMac();
	}
	/**
	 * @param in
	 * @param multipartFile
	 * <p>Description:从excel批量导入空调</p>
	 */
	@Override
	public Boolean addAirConditionFromExcel(InputStream in, MultipartFile multipartFile, int adminId) throws Exception {
		List<List<Object>> listob = ExcelUtil.getBankListByExcel(in,multipartFile.getOriginalFilename());    
		List<AirCondition> listAir = new ArrayList<AirCondition>();
		List<ConditionInfo> listInfo = new ArrayList<ConditionInfo>();
		//遍历listob数据，把数据放到List中    
		for (int i = 0; i < listob.size(); i++) {        
			List<Object> ob = listob.get(i);        
			AirCondition airCondition = new AirCondition();
			airCondition.setMac(ob.get(0).toString());
			airCondition.setAreaId(Integer.parseInt(ob.get(1).toString()));
			airCondition.setSeriesId(Integer.parseInt(ob.get(2).toString()));
			airCondition.setPower(Integer.parseInt(ob.get(3).toString()));
			airCondition.setHour(new BigDecimal(ob.get(4).toString()));
			listAir.add(airCondition);
		}  
		for (int i = 0; i < listAir.size(); i++) {
			ConditionInfo conditionInfo = new ConditionInfo();
			conditionInfo.setMac(listAir.get(i).getMac());
			conditionInfo.setDryHot(SystemCode.AIR_CONDITION_DRY_HOT[0]);
			conditionInfo.setStatus(SystemCode.AIR_CONDITION_ORDER_STOP);
			conditionInfo.setModel(SystemCode.AIR_CONDITION_MODEL[0]);
			conditionInfo.setSleep(SystemCode.AIR_CONDITION_SLEEP[0]);
			conditionInfo.setStrong(SystemCode.AIR_CONDITION_STRONG[0]);
			conditionInfo.setSwing(SystemCode.AIR_CONDITION_SWING[0]);
			conditionInfo.setWindSpeed(SystemCode.AIR_CONDITION_WIND_SPEED[0]);
			listInfo.add(conditionInfo);
		}
		Boolean boolInfo = airmapper.insertConditionInfoList(listInfo);
		Boolean boolAir = airmapper.insertAirConditionList(listAir);
		return boolAir&&boolInfo;
	}
	public CrmUser getUserByUserNameAndRole(Map map) {
	return crmUserMapper.getUserByUserNameAndRole(map);
	}
}
