package com.air.appinfo.service;

import java.util.List;

import com.air.pojo.entity.AppInfo;
import com.github.pagehelper.PageInfo;

public interface AirInfoService {

	List<AppInfo> queryContentBytype(String type);

	Boolean addConfig(AppInfo appInfo);

	Boolean modifyConfig(AppInfo appInfo);

	PageInfo<AppInfo> queryAllConfig(Integer curPage, Integer pageSize);

	Boolean delConfig(Integer appinfoId);

	/**
	 * 通过id获取支付配置信息
	 * @return
	 */
	AppInfo queryConfigById(Integer appInfoId);

	/**
	 * type或content模糊查询获取支付配置信息
	 * @return
	 */
	PageInfo<AppInfo> queryConfig(AppInfo appInfo,Integer curPage, Integer pageSize);

}
