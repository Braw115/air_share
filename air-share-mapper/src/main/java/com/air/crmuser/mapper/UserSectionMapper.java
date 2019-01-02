package com.air.crmuser.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.ActiveType;
import com.air.pojo.entity.AppUserInfo;
import com.air.pojo.entity.PromotionInfo;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.vo.ActiveTypeVo;
import com.air.pojo.vo.BoxAndVoucherVo;

public interface UserSectionMapper {

	Boolean insertActiveType(ActiveType activeType);

	List<ActiveType> selectActiveType(ActiveTypeVo activeTypeVo);

	Boolean updateActiveType(ActiveType activeType);

	Boolean deleteActiveType(@Param("activeIdList")List<Integer> activeIdList);

	Boolean updateActiveTypeShelves(@Param("activeIdList") List<Integer> activeIdList,@Param("shelves") Boolean shelves);

	List<AppUserInfo> selectAppUserInfo(AppUserInfo appUserInfo);

	List<PromotionInfo> selectAllPromotionStatistics();

	List<PromotionInfo> selectPromotionStatistic(PromotionInfo pro);

	List<ActiveType> selectActiveTypeByAirMAC(ActiveTypeVo activeTypeVo);

	RechargeType selectRechargeTypeById(@Param("rechargeId")Integer rechargeId);

	/**
	 * 根据id获取活动信息
	 * @param activeId
	 * @return
	 */
	ActiveType selectActiveTypeById(@Param("activeId")Integer activeId);

	RechargeType selectRechargeType();
	
	/**
	 * 
	 */
	List<RechargeType> selectRechargeTypeByMAC(@Param("airmac")String airmac);

	/**
	 * 修改包年包时价格
	 * @param rechargeType
	 * @return
	 */
	Boolean updateRechargeType(RechargeType rechargeType);

	/**
	 * 查询可以参加的促销活动
	 * @param nowTime
	 * @return
	 */
	List<BoxAndVoucherVo> selectActives(String nowTime);
}
