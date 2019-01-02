package com.air.pay.service;

import java.util.List;

import com.air.constant.Dto;
import com.air.pojo.entity.RedBoxActive;
import com.air.pojo.entity.RedBoxNote;
import com.air.pojo.entity.RedBoxRate;
import com.air.pojo.entity.VoucherActive;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.air.pojo.vo.RedBoxActiveVo;
import com.air.pojo.vo.VoucherActiveVo;

public interface PromotionService {

	/**
	 * 添加红包活动 
	 * @param redBoxActive
	 * @return
	 */
	Dto addRedBoxActive(RedBoxActive redBoxActive);

	/**
	 * 批量修改红包活动状态（发布或者下架）
	 * @param redBoxActiveIdlist
	 * @param status
	 * @return
	 */
	Dto modifyRedBoxActiveStatus(List<Integer> redBoxActiveIdlist, Boolean status);

	/**
	 * 修改红包活动信息
	 * @param redBoxActive
	 * @return
	 */
	Boolean modifyRedBoxActive(RedBoxActive redBoxActive);

	/**
	 * 根据代金券活动状态分页查询红包活动信息
	 * 可以分页查询所有代金券折扣券活动信息
	 * 可以通过名称模糊查询代金券折扣券活动信息 
	 * @param voucherActiveVo
	 * @return
	 */
	Dto queryVoucherActiveByPage(VoucherActiveVo voucherActiveVo);
	
	/**
	 * 分页查询获取所有红包活动和对应的区间比例
	 * @param curPage
	 * @param pageSize
	 * @param name 
	 * @return
	 */
	Dto queryAllRedBoxActive(Integer curPage, Integer pageSize, String name);
	
	/**
	 * 根据红包活动id查询红包活动信息
	 * @param redBoxActiveId
	 * @return
	 */
	Dto queryRedBoxActiveById(Integer redBoxActiveId);

	/**
	 * 根据红包活动名字模糊查询红包活动信息
	 * @param pageSize 
	 * @param curPage 
	 * @param name
	 * @return
	 */
	Dto queryRedBoxActiveByName(Integer curPage, Integer pageSize, String name);
	
	/**
	 * 添加领取红包记录
	 * @param redBoxNote
	 * @return
	 */
	Dto getRedBoxActiveByName(RedBoxNote redBoxNote);

	/**
	 * 添加代金券折扣券活动
	 * @param voucherActive
	 * @return
	 */
	Dto addVoucherActive(VoucherActive voucherActive);

	/**
	 * 批量修改代金券折扣券活动状态（发布或者下架）
	 * @param voucherActiveIdlist
	 * @param status
	 * @return
	 */
	Dto modifyVoucherActiveStatus(List<Integer>voucherActivelist, Boolean status);

	/**
	 * 修改代金券折扣券活动信息
	 * @param voucherActive
	 * @return
	 */
	Dto modifyVoucherActive(VoucherActive voucherActive);

	/**
	 * 添加红包活动获奖区间和比例信息
	 * @param redBoxRates
	 * @return
	 */
	Dto addRedBoxRates(List<RedBoxRate> redBoxRates);

	/**
	 * 修改红包活动获奖区间和比例信息
	 * @param redBoxRates
	 * @return
	 */
	Dto modifyRedBoxRates(List<RedBoxRate> redBoxRates);

	/**
	 * 根据红包活动id获取红包活动获奖区间和比例信息
	 * @param redBoxActiveId
	 * @return
	 */
	Dto queryRedBoxRateByRedBoxActiveId(Integer redBoxActiveId);

	/**
	 * 获取红包
	 * @param appusersId
	 * @param redBoxActiveId 
	 * @return
	 */
	Dto getRedBox(Integer appusersId, Integer redBoxActiveId);

	/**
	 * 用户获取代金券或折扣券
	 * @param appusersId
	 * @param voucherActiveId
	 * @param type
	 * @return
	 */
	Dto addVoucherForUser(Vouchers voucher);

	/**
	 * 碳排放量达标获取红包
	 * @param appUserId
	 * @param clevel
	 * @param activeName
	 * @return
	 */
	Dto queryRedBoxByCarbon(Integer appUserId, String activeName);

//	/**
//	 * 用户获取获取优惠券
//	 * @param appUserId
//	 * @param time
//	 * @return
//	 */
//	Dto queryVoucherActives(String time);

	/**
	 * 根据代金券折扣券活动id查询代金券折扣券活动信息
	 * @param voucherActiveVo
	 * @return
	 */
	Dto queryVoucherActive(VoucherActiveVo voucherActiveVo);

	/**
	 * 根据红包活动状态分页查询红包活动信息
	 * @param curPage
	 * @param pageSize
	 * @param status
	 * @return
	 */
	Dto queryRedBoxActiveByStatus(Integer curPage, Integer pageSize, String status);

	/**
	 * 添加红包活动 
	 * @param redBoxActiveVo
	 * @return
	 */
	Boolean addRedBoxActive(RedBoxActiveVo redBoxActiveVo);

	/**
	 * 根据活动id删除区间概率
	 * @param redBoxActiveId
	 * @return
	 */
	Boolean delRedBoxRates(Integer redBoxActiveId);

	/**
	 * 查询
	 * @param redBoxActiveVo
	 * @return
	 */
	Dto queryRedBoxActive(RedBoxActiveVo redBoxActiveVo);

	/**
	 * 分页查询红包活动信息
	 * @param redBoxActiveVo
	 * @return
	 */
	Dto queryRedBoxActiveByPage(RedBoxActiveVo redBoxActiveVo);

	/**
	 * 根据id删除红包活动信息
	 * @param redBoxActiveId
	 * @return
	 */
	Dto delRedBoxActive(Integer redBoxActiveId);

	/**
	 * 删除优惠券活动
	 * @param voucherActiveId
	 * @return
	 */
	Dto delVoucherActive(Integer voucherActiveId);

	/**
	 * 
	 * @param appUserId
	 * @return
	 */
	Dto queryVouchers(Integer appUserId);

	/**
	 * 查询当前参加的红包活动
	 * @param nowTime
	 * @return
	 */
	List<BoxAndVoucherVo> queryRedBoxs(String nowTime);

	/**
	 * 查询当前可以参加的优惠券活动
	 * @param nowTime
	 * @return
	 */
	List<BoxAndVoucherVo> queryVoucherActive(String nowTime);

	/**
	 * 查询当前用户可以参加的且未领取的优惠券活动
	 * @param nowTime
	 * @param appusersId
	 * @return
	 */
	List<BoxAndVoucherVo> queryRedBoxs(String nowTime, Integer appusersId);

	/**
	 * 查询当前用户可以参加未领取的红包活动
	 * @param nowTime
	 * @param appusersId
	 * @return
	 */
	List<BoxAndVoucherVo> queryVoucherActive(String nowTime, Integer appusersId);

	/**
	 * 用户获取优惠券
	 * @param appusersId 
	 * @param boxAndVoucherVo
	 * @return
	 */
	Dto addVoucherForAppUser(Integer appusersId, BoxAndVoucherVo boxAndVoucherVo);

	/**
	 * 用户获取红包
	 * @param appusersId 
	 * @param boxAndVoucherVo
	 * @return
	 */
	Dto addRedBoxForAppUser(Integer appusersId, BoxAndVoucherVo boxAndVoucherVo);

	/**
	 * 根据id查询优惠券活动，根据用户id查询是否领取
	 * @param id
	 * @param appusersId 
	 * @return
	 */
	Dto queryVoucherActiveOrNot(Integer id, Integer appusersId);

	/**
	 * 根据id查询红包活动，根据用户id查询是否领取
	 * @param id
	 * @param appusersId 
	 * @return
	 */
	Dto queryRedBoxActiveOrNot(Integer id, Integer appusersId);

	/**
	 * 查询碳指标活动是否发布
	 * @param appUserId
	 * @param activeName
	 * @return
	 */
	Dto queryCarbonRedBox(Integer appUserId, String activeName);

}
