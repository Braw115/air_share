package com.air.appinfo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.appinfo.mapper.AirInfoMapper;
import com.air.appinfo.service.AirInfoService;
import com.air.pojo.entity.AppInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class AirInfoServiceImpl implements AirInfoService{

	@Resource
	private AirInfoMapper airinfoMapper;
	
	@Override
	public List<AppInfo> queryContentBytype(String type) {
		AppInfo appInfo = new AppInfo();
		appInfo.setType(type);
		return airinfoMapper.selectConfigByAppInfo(appInfo);
	}

	@Override
	public Boolean addConfig(AppInfo appInfo) {
		return airinfoMapper.insertConfig(appInfo);
	}

	@Override
	public Boolean modifyConfig(AppInfo appInfo) {
		AppInfo info = airinfoMapper.selectConfigById(appInfo.getAppInfoId());
		if ("controlDistance".equals(info.getType()) && !StringUtils.isNumeric(appInfo.getContent())) {
			return false;
		}
		return airinfoMapper.updateConfig(appInfo);
	}

	@Override
	public PageInfo<AppInfo> queryAllConfig(Integer curPage, Integer pageSize) {
		PageHelper.startPage(curPage, pageSize);
		List<AppInfo> appInfoList = airinfoMapper.selectConfig();
		PageInfo<AppInfo> appInfo = new PageInfo<AppInfo>(appInfoList);
		return appInfo;
	}

	@Override
	public Boolean delConfig(Integer appinfoId) {
		return airinfoMapper.deleteConfig(appinfoId);
	}

	@Override
	public AppInfo queryConfigById(Integer appInfoId) {
		AppInfo appInfo = airinfoMapper.selectConfigById(appInfoId);
		return appInfo;
	}

	@Override
	public PageInfo<AppInfo> queryConfig(AppInfo appInfo,Integer curPage, Integer pageSize) {
		PageHelper.startPage(curPage, pageSize);
		List<AppInfo> appInfoList = airinfoMapper.selectConfigByAppInfo(appInfo);
		PageInfo<AppInfo> appInfoPage = new PageInfo<AppInfo>(appInfoList);
		return appInfoPage;
	}

	
}
