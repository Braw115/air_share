package com.air.user.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.air.constant.Dto;
import com.air.pojo.entity.AppUser;
import com.air.pojo.vo.AppUserVo;
import com.air.pojo.vo.ItripTokenVO;

public interface AppUserService {

	
	List<AppUser> queryBySql(HashMap<String, String> hashMap);

	Boolean addWxappUser(String openid, String wxnickname, String headimgurl, String unionid, String phone, String sex);

	/**
	 * 通过手机查询用户
	 * @param phone
	 * @return
	 */
	AppUser queryByPhone(String phone);

	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	Boolean modifyByPrimaryKeySelective(AppUser user);

	Dto doLogin(String phone, String password);

	Boolean technicianRegister(AppUserVo appUserVo);

	Dto addPhoneUser(HttpServletRequest request,
			HttpSession session,String phone);

	ItripTokenVO getToken(HttpServletRequest request, HttpSession session, AppUser users);

	Dto modifyPhoneUser(AppUser appUser);

	Dto resetPwd(String phone, String oldPwd, String newPwd);

	Dto modifyPhoneForUser(Integer appusersId, String newPhone);

	Dto addAddressForUser(AppUser appUser);

	Dto queryTechnician(Integer curPage, Integer pageSize, String reviewStatus);

	AppUser queryByZfbUserId(String user_id);

	Dto addZfbAppUser(HttpServletRequest req, HttpSession session, String user_id, String avatar, String phone, String nick_name, String gender);

	/**
	 * 审核技术人员
	 * @param appUser
	 * @return
	 */
	Dto modifyFechnicianInfo(AppUser appUser);

	/**
	 * 根据用户id查询用户
	 * @param appusersId
	 * @return
	 */
	Dto queryAppUserById(Integer appusersId);

	/**
	 * 把红包转入余额
	 * @param appUser
	 * @return
	 */
	Dto modifyBalance(AppUser appUser);

	/**
	 * 修改用户
	 * @param appUser
	 * @return
	 */
	Dto modifyAppUser(AppUserVo appUserVo);

	/**
	 * 
	 * @param appUser
	 * @return
	 */
	Dto queryAppUser(AppUser appUser);

	/**
	 * 所有条件全包括查询app用户或者技术员
	 * @param appUserVo
	 * @return
	 */
	Dto queryAppUser(AppUserVo appUserVo);

	/**
	 * 分页
	 * @param appUserVo
	 * @return
	 */
	Dto queryAppUserByPage(AppUserVo appUserVo);

	/**
	 * Cm没修改技术员
	 * @param appUserVo
	 * @return
	 */
	Boolean modifyAppUserCrm(AppUserVo appUserVo);

	/**
	 * crm端删除用户时修改角色为合作商的crm用户下的技术员信息
	 * @param crmuserId
	 * @return
	 */
	Boolean modifyAppUserPartner(Integer crmuserId);


}
