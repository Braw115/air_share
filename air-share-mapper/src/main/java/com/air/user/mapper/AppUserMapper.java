package com.air.user.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.AppUser;
import com.air.pojo.vo.AppUserVo;

public interface AppUserMapper {

	List<AppUser> selectBySql(HashMap<String, String> map);

	AppUser selectByPhone(@Param("phone")String phone);

	Boolean insertAppUser(AppUser user);

	Boolean updateAppUser(AppUser appUser);
	
	AppUser selectById(@Param("appusersId")Integer appusersId);

	List<AppUser> selectTechnicianByReviewStatus(AppUser user);

	AppUser selectAppUserByZfbUserId(@Param("aliUserId")String aliUserId);

	/**
	 * 查询用户信息
	 * @param appUser
	 * @return
	 */
	AppUser selectAppUser(AppUser appUser);

	/**
	 * 查询技术员
	 * @param appUserVo
	 * @return
	 */
	List<AppUser> selectAppUserInfo(AppUserVo appUserVo);
	
	/**
	 * crm端删除用户时修改角色为合作商的crm用户下的技术员信息
	 * @param crmuserId
	 * @return
	 */
	Boolean updateAppUserPartner(Integer crmuserId);
	
}
