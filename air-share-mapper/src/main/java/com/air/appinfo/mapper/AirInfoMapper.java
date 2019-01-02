package com.air.appinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.AppInfo;

public interface AirInfoMapper {

	List<AppInfo> selectContentBytype(@Param("type")String type);

	Boolean insertConfig(AppInfo appInfo);

	Boolean updateConfig(AppInfo appInfo);

	List<AppInfo> selectConfig();

	Boolean deleteConfig(@Param("appinfoId")Integer appinfoId);

	/**
	 * 通过id获取支付配置信息
	 * @return
	 */
	AppInfo selectConfigById(Integer appInfoId);

	/**
	 * 通过appInfo获取支付配置信息
	 * @return
	 */
	List<AppInfo> selectConfigByAppInfo(AppInfo appInfo);

}
