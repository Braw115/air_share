package com.air.aircondition.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AirUseInfo;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.ConditionInfo;
import com.air.pojo.entity.Series;
import com.air.pojo.vo.AirConditionVO;
import com.air.pojo.vo.AppUserAirVo;
import com.air.pojo.vo.SeriesVO;

@Repository
public interface AirConditionMapper {
	
	public List<AirConditionVO> selectAllAirCondition(AirConditionVO airCondition);
	
	/**
	 * <p>Title: selectAirConditionById</p>
	 * <p>Description: 根据空调ID查找某个空调的信息</p>
	 * @param airConditionId
	 * @return
	 */
	public AirCondition selectAirConditionById(String mac);
	
	
	/**
	 * <p>Title: selectAirConditionById</p>
	 * <p>Description: 根据空调mac和用户id查找空调的信息</p>
	 * @param airConditionId
	 * @return
	 */
	public AirCondition selectAirConditionByMacAndUserId(@Param("mac")String mac,@Param("appusersId")Integer appusersId);
	
	/**
	 * 
	 * <p>Title: selectAirConditionByUserId</p>
	 * <p>Description: 根据App用户ID查找绑定的空调</p>
	 * @param appUserId
	 * @return
	 */
	public List<AppUserAir> selectAirConditionByUserId(Integer appusersId);

	/**
	 * <p>Description:根据appuserId和mac查询绑定记录</p>
	 * @param appuserAir
	 * @param mac
	 * @return
	 */
	public AppUserAir selectAppUserAirByUseIdAndMac(@Param("appusersId")Integer appusersId,@Param("mac")String mac);
	
	/**
	 * 
	 * <p>Title: updateAirConditionNote</p>
	 * <p>Description:根据App用户ID 空调MAC修改空调备注 </p>
	 * @param appusersId
	 * @return
	 */
	public Integer updateAirConditionNote(AppUserAir appuserAir);
	
	/**
	 * 
	 * <p>Title: updateAirCondition</p>
	 * <p>Description: 修改空调信息</p>
	 * @param airCondition
	 * @return
	 */
	public Integer updateAirCondition(AirCondition airCondition);
	
	/**
	 * 
	 * <p>Title: selectAirConditionByIdAndUser</p>
	 * <p>Description:根据空调MAC和用户ID查询绑定信息 </p>
	 * @param mac
	 * @param appusersId
	 * @return
	 */
	public AppUserAir selectAirConditionByIdAndUser(@Param("mac")String mac, @Param("appusersId")Integer appusersId);
	
	
	/**
	 * 
	 * <p>Title: addAppUserAir</p>
	 * <p>Description: APP用户绑定空调 </p>
	 * @param appuserAir
	 * @return
	 */
	public Integer addAppUserAir(AppUserAir appuserAir);
	
	/**
	 * 
	 * <p>Title: addAirCondition</p>
	 * <p>Description: 添加一台新空调</p>
	 * @param airCondition
	 * @return
	 */
	public Integer addAirCondition(AirCondition airCondition);
	
	/**
	 * 
	 * <p>Title: selectSeriesList</p>
	 * <p>Description: 查询空调系列列表 </p>
	 * @param series
	 * @return
	 */
	public List<Series> selectSeriesList(SeriesVO series);
	
	/**
	 * 
	 * <p>Title: deleteAirCondition</p>
	 * <p>Description: 删除空调根据空调ID</p>
	 * @param mac
	 * @return
	 */
	public Integer deleteAirCondition(@Param("mac")String mac);
	
	/**
	 * 
	 * <p>Title: deleteUserAirByUserAndAir</p>
	 * <p>Description:根据用户ID和Mac删除中间表记录 </p>
	 * @param appuserAir
	 * @return
	 */
	public Integer deleteUserAirByUserAndAir(AppUserAir appuserAir);
	
	/**
	 * 
	 * <p>Title: getConditionInfoByMac</p>
	 * <p>Description: 根据MAC获取空调信息</p>
	 * @param mac
	 * @return
	 */
	public ConditionInfo getConditionInfoByMac(String mac);
	
	/**
	 * 
	 * <p>Title: addConditionInfo</p>
	 * <p>Description: 添加空调信息</p>
	 * @param conditionInfo
	 * @return
	 */
	public Integer addConditionInfo(ConditionInfo conditionInfo);
	
	/**
	 * 
	 * <p>Title: updateConditionInfoByMac</p>
	 * <p>Description: 修改空调信息</p>
	 * @param conditionInfo
	 * @return
	 */
	public Integer updateConditionInfoByMac(ConditionInfo conditionInfo);
	
	/**
	 * 
	 * <p>Title: deleteAirConditionBySeries</p>
	 * <p>Description: 根据系列删除空调</p>
	 * @param seriesId
	 * @return
	 */
	public Integer deleteAirConditionBySeries(Integer seriesId);

	/**
	 * 
	 * <p>Title: selectAirConditionTimeOut</p>
	 * <p>Description: 查询绑定事件达到或超过一个月的绑定数据</p>
	 * @return
	 */
	public List<AppUserAir> selectAppUserAirTimeOut();

	/**
	 * <p>Title: deleteAppUserAir</p>
	 * <p>Description: 批量删除达到或超过一个月的绑定数据</p>
	 * @param list
	 * @return
	 */
	public Boolean deleteAppUserAir(@Param("list")List<AppUserAir> list);

	/**
	 * <p>Title: queryBindingInfoByMac</p>
	 * <p>Description: 获取空调的绑定信息 </p>
	 * @param mac
	 * @return
	 */
	public List<AppUserAirVo> selectBindingInfoByMac(String mac);

	/**
	 * <p>Title: selectAllAirMac</p>
	 * <p>Description: 获取所有空调的mac地址 </p>
	 * @return
	 */
	public List<String> selectAllAirMac();

	/**
	 * 保存空调的实时信息
	 * @param info
	 * @return
	 */
	public Boolean insertAirUseInfo(AirUseInfo info);

	/**
	 * 
	 * <p>Title: deleteConditionInfo</p>
	 * <p>Description: 添加空调信息</p>
	 * @param conditionInfo
	 * @return
	 */
	public Boolean deleteConditionInfo(String mac);

	/**
	 * 批量添加空调信息
	 * @param listInfo
	 * @return
	 */
	public Boolean insertConditionInfoList(@Param("listInfo")List<ConditionInfo> listInfo);

	/**
	 * 批量添加空调
	 * @param listAir
	 * @return
	 */
	public Boolean insertAirConditionList(@Param("listAir")List<AirCondition> listAir);
	
}
