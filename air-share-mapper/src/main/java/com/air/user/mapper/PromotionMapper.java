package com.air.user.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Order;
import com.air.pojo.entity.RedBoxActive;
import com.air.pojo.entity.RedBoxNote;
import com.air.pojo.entity.RedBoxRate;
import com.air.pojo.entity.VoucherActive;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.air.pojo.vo.RedBoxActiveVo;
import com.air.pojo.vo.VoucherActiveVo;

public interface PromotionMapper {

	/**
	 * 添加红包活动
	 * @param redBoxActive
	 * @return
	 */
	Boolean insertRedBoxActive(RedBoxActive redBoxActive);

	Boolean updateRedBoxActiveStatus(@Param("redBoxActiveIdlist")List<Integer> redBoxActiveIdlist,@Param("status")Boolean status);

	Boolean updateRedBoxActive(RedBoxActive redBoxActive);

	List<RedBoxActive> selectRedBoxActiveByStatus(@Param("status")String status);

	List<RedBoxActiveVo> selectAllRedBoxActive(@Param("name")String name);

	RedBoxActive selectRedBoxActiveById(@Param("redBoxActiveId")Integer redBoxActiveId);

	List<RedBoxActiveVo> selectRedBoxActiveByName(@Param("name")String name);

	Boolean insertRedBoxNote(RedBoxNote redBoxNote);
	
	Integer selectTotalCount();

	Boolean insertVoucherActive(VoucherActive voucherActive);

	Boolean updateVoucherActiveStatus(@Param("voucherActiveIdlist")List<Integer> voucherActiveIdlist,@Param("status")Boolean status);
	
	Boolean updateVoucherActive(VoucherActive voucherActive);

	Integer selectVouchersTotalCount(@Param("voucherActiveId")Integer voucherActiveId);

	Boolean insertRedBoxRates(@Param("redBoxRates")List<RedBoxRate> redBoxRates);

	Boolean updateRedBoxRate(@Param("redBoxRates")List<RedBoxRate> redBoxRates);

	List<RedBoxRate> selectRedBoxRateByRedBoxActiveId(RedBoxRate redBoxRate);

	List<RedBoxActive> selectAllRedBoxActiveByOrder(Order order);

	/**
	 * 根据用户id和红包活动id查询用户红包记录
	 * @param redBoxNote
	 * @return
	 */
	RedBoxNote selectRedBoxNoteByRedBoxNote(RedBoxNote redBoxNote);

	/**
	 * 根据条件查询是否满足领取代金券
	 * @param order
	 * @return
	 */
	List<VoucherActive> selectVoucherActiveByOrder(Order order);

	/**
	 * 根据红包活动id统计发放的红包总金额
	 * @param redBoxActiveId
	 * @return
	 */
	BigDecimal selectSendTotalValue(@Param("redBoxActiveId")Integer redBoxActiveId);

	/**
	 * 用户获取代金券之后添加
	 * @param voucher
	 * @return
	 */
	Boolean insertVouchers(Vouchers voucher);

	/**
	 * 通过名称查询可以领取红包的红包活动
	 * @param activeName
	 * @param date
	 * @return
	 */
	List<RedBoxActive> selectRedBoxActiveByNameOk(@Param("activeName")String activeName,@Param("date")String date);

	/**
	 * 根据代金券活动状态分页查询红包活动信息
	 * 可以分页查询所有代金券折扣券活动信息
	 * 可以通过名称模糊查询代金券折扣券活动信息
	 * 根据代金券折扣券活动id查询代金券折扣券活动信息
	 * @param voucherActiveVo
	 * @return
	 */
	List<VoucherActive> selectVoucherActiveByVo(VoucherActiveVo voucherActiveVo);

	/**
	 * 用户查询可领取的优惠券活动
	 * @param time
	 * @return
	 */
	List<BoxAndVoucherVo> selectVoucherActives(@Param("time")String time);

	/**
	 * 添加红包活动
	 * @param redBoxActiveVo
	 * @return
	 */
	Boolean insertRedBoxActive(RedBoxActiveVo redBoxActiveVo);

	/**
	 * 根据红包活动id删除红包概率区间
	 * @param redBoxActiveId
	 * @return
	 */
	Boolean deleteRedBoxRates(@Param("redBoxActiveId")Integer redBoxActiveId);

	/**
	 * 查询红包活动
	 * @param redBoxActiveVo
	 * @return
	 */
	List<RedBoxActiveVo> selectRedBoxActive(RedBoxActiveVo redBoxActiveVo);

	/**
	 * 根据红包活动id红包活动
	 * @param redBoxActiveId
	 * @return
	 */
	Boolean deleteRedBoxActive(Integer redBoxActiveId);

	/**
	 * 删除优惠券活动
	 * @param voucherActiveId
	 * @return
	 */
	Boolean deleteVoucherActive(Integer voucherActiveId);
	
	/**
	 * 查询代金券，折扣券 
	 * @param voucher
	 * @return
	 */
	List<Vouchers> selectVoucher(Vouchers voucher);

	/**
	 * 查询当前可以参加的红包活动
	 * @param nowTime
	 * @return
	 */
	List<BoxAndVoucherVo> selectRedBoxs(String nowTime);

	/**
	 * 查询当前用户可以参加未领取的红包活动
	 * @param nowTime
	 * @param appusersId
	 * @return
	 */
	List<BoxAndVoucherVo> selectRedBox(@Param("nowTime")String nowTime,@Param("appusersId") Integer appusersId);

	/**
	 * 查询当前用户可以参加的且未领取的优惠券活动
	 * @param nowTime
	 * @param appusersId
	 * @return
	 */
	List<BoxAndVoucherVo> selectVoucherActive(@Param("nowTime")String nowTime, @Param("appusersId")Integer appusersId);

	/**
	 * 查询过期的优惠券
	 * @param voucher
	 * @return
	 */
	List<Vouchers> selectVoucherTimeout(@Param("appUserId")Integer appUserId);

	/**
	 * 批量修改优惠券状态
	 * @param status 
	 * @param idList
	 * @return
	 */
	Boolean updateVoucherStatus(@Param("status")String status, @Param("idList")List<Integer> idList);

	
}
