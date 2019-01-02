package com.air.condition.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.web.multipart.MultipartFile;

import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.OperationRecord;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.AirConditionVO;
import com.air.pojo.vo.AppUserAirVo;
import com.air.pojo.vo.BindingVO;
import com.air.pojo.vo.SeriesVO;
import com.github.pagehelper.PageInfo;

public interface ConditionService {
	
	/**
	 * @description 根据空调ID查找某个空调的信息
	 * @param airConditionId
	 * @return
	 */
	public AirCondition selectAirConditionById(String airConditionId);
	
	/**
	 * 
	 * <p>Title: selectAirConditionByUserId</p>
	 * <p>Description: 根据APP用户ID查找绑定的空调</p>
	 * @param appuserId
	 * @return
	 */
	public List<AppUserAir> selectAirConditionByUserId(Integer appuserId);
	
	/**
	 * 
	 * <p>Title: updateAirConditionNote</p>
	 * <p>Description: 根据App用户ID 空调MAC修改空调备注</p>
	 * @param appuserAir
	 * @return
	 */
	public Integer updateAirConditionNote(AppUserAir appuserAir);

	
	/**
	 * 
	 * <p>Title: bindingAirConditionByAppUser</p>
	 * <p>Description: APP用户绑定空调</p>
	 * @param appuserId
	 * @return
	 */
	public Integer bindingAirConditionByAppUser(AppUserAir appuserAir);
	
	/**
	 * 
	 * <p>Title: selectAirConditionList</p>
	 * <p>Description: 获取空调列表</p>
	 * @param airCondition
	 * @return
	 */
	public PageInfo<AirConditionVO> selectAirConditionList(AirConditionVO airCondition);
	
	/**
	 * 
	 * <p>Title: selectSeriesList</p>
	 * <p>Description: 查询系列列表</p>
	 * @param series
	 * @return
	 */
	public PageInfo<Series> selectSeriesList(SeriesVO series);
	
	/**
	 * 
	 * <p>Title: selectAllAirCondition</p>
	 * <p>Description: 获取全部的空调信息</p>
	 * @return
	 */
	public List<AirConditionVO> selectAllAirCondition();
	
	/**
	 * 
	 * <p>Title: deleteCondition</p>
	 * <p>Description: 删除空调</p>
	 * @param mac
	 * @return
	 */
	public Integer deleteCondition(String mac);
	
	/**
	 * 
	 * <p>Title: updateAirCondition</p>
	 * <p>Description: </p>
	 * @param airCondition
	 * @return
	 */
	public Integer updateAirCondition(AirCondition airCondition);
	
	/**
	 * 
	 * <p>Title: addAirCondition</p>
	 * <p>Description: 添加空调设备</p>
	 * @param airCondition
	 * @return
	 */
	public Integer addAirCondition(AirCondition airCondition);
	
	/**
	 * 
	 * <p>Title: bindingCondition</p>
	 * <p>Description: 空调绑定业主</p>
	 * @param airCondition
	 * @return
	 */
	public BindingVO bindingCondition(BindingVO airCondition,Integer appUserId);
	
	/**
	 * 
	 * <p>Title: getLastOperRecord</p>
	 * <p>Description: 查询用户的最后一跳指令</p>
	 * @param appUserId
	 * @return
	 */
	public OperationRecord getLastOperRecord(Integer appUserId);
	
	/**
	 * 
	 * <p>Title: releaseBindingByAppUser</p>
	 * <p>Description: App用户解除绑定</p>
	 * @param appuserAir
	 * @return
	 */
	public Integer releaseBindingByAppUser(AppUserAir appuserAir);
	
	/**
	 * 
	 * <p>Title: releaseBindingByAppUser</p>
	 * <p>Description: Crm管理员解除用户绑定</p>
	 * @param appuserAir
	 * @return
	 */
	public Integer releaseBindingByCrmUser(AppUserAir appuserAir) throws MqttPersistenceException, MqttException ;
	
	/**
	 * 
	 * <p>Title: getConditionInfoByMac</p>
	 * <p>Description: 获取空调的指令状态 </p>
	 * @param mac
	 * @return
	 */
	public ConditionInfo getConditionInfoByMac(String mac, Integer appUserId);

	/**
	 * 
	 * <p>Title: queryBindingInfoByMac</p>
	 * <p>Description: 获取空调的绑定信息 </p>
	 * @param mac
	 * @return
	 */
	public List<AppUserAirVo> queryBindingInfoByMac(String mac);

	/**
	 * 
	 * <p>Title: queryAllAirMac</p>
	 * <p>Description: 获取所有空调的mac地址 </p>
	 * @return
	 */
	public List<String> queryAllAirMac();

	/**
	 * 从excel导入空调批量插入
	 * @param in
	 * @param multipartFile
	 * @param adminId
	 * @return
	 */
	public Boolean addAirConditionFromExcel(InputStream in, MultipartFile multipartFile, int adminId) throws Exception ;
	/**
	 * 完成安装
	 * @param appUserId
	 * @param mac
	 * @return
	 */
	public String finshInstall(Integer appUserId,String mac);
	
	public AirCondition selectAirConditionByMacAndUserId(String mac,Integer appusersId);
	public CrmUser getUserByUserNameAndRole(Map map);
}
