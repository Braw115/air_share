package com.air.crmuser.service;

import java.util.List;

import com.air.constant.Dto;
import com.air.pojo.entity.ActiveType;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.vo.ActiveTypeVo;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.github.pagehelper.PageInfo;

public interface UserSectionService {

	Dto addActiveType(ActiveTypeVo activeTypeVo);

//	PageInfo<ActiveType> queryActiveType(Integer curPage, Integer pageSize);

	/**
	 * 根据发布与否查询活动信息
	 * @param curPage
	 * @param pageSize
	 * @param type
	 * @return
	 */
//	PageInfo<ActiveType> queryActiveTypeByType(Integer curPage, Integer pageSize,Boolean shelves);

	Dto modifyActiveType(ActiveTypeVo activeTypeVo);

	Dto modifyActiveTypePic(ActiveTypeVo activeTypeVo);

	Dto delActiveType(List<Integer> activeIdList);

	Dto modifyActiveTypeShelves(List<Integer> activeIdList, Boolean shelves);

	Dto queryAppUserInfo(Integer curPage, Integer pageSize);

	Dto queryAppUserPerInfoById(Integer appuserId);

	Dto queryAppUserPerInfo(Integer curPage, Integer pageSize, String nickname);

	Dto queryAllPromotionStatistics(Integer curPage, Integer pageSize);

	Dto queryPromotionStatistics(Integer curPage, Integer pageSize, String name);

	Dto queryPromotionStatisticsById(Integer activeId);

	/**
	 * 根据id获取活动信息
	 * @param activeId
	 * @return
	 */
	Dto queryActiveTypeById(Integer activeId);

	/**
	 * 查询所有购买类型
	 * @return
	 */
	Dto queryRecharge(String airmac);

	/**
	 * 根据mac地址查询是否有发布的活动
	 * @param activeTypeVo
	 * @return
	 */
	Dto queryActiveTypeByMAC(ActiveTypeVo activeTypeVo);

	
	/**
	 * 分页查询
	 * @param activeTypeVo
	 * @return
	 */
	Dto queryActiveTypeByPage(ActiveTypeVo activeTypeVo);

	/**
	 * 查询某条数据
	 * @param activeTypeVo
	 * @return
	 */
	Dto queryActiveType(ActiveTypeVo activeTypeVo);

	/**
	 * 查询包年包时价格（全部统一价格）
	 * @return
	 */
	Dto queryRecharge();

	/**
	 * 修改包年包时价格
	 * @param rechargeType
	 * @return
	 */
	Dto modifyRechargeType(RechargeType rechargeType);

	/**
	 * 根据id查询促销活动，根据用户id查询是否参加
	 * @param id
	 * @param appusersId 
	 * @return
	 */
	Dto queryActiveOrNot(Integer id, Integer appusersId);

	/**
	 * 查询可以参加的促销活动
	 * @param nowTime
	 * @return
	 */
	List<BoxAndVoucherVo> queryActive(String nowTime);

	/**
	 * 计算活动的包年包时的价格
	 * @param activeTypeVo
	 * @return
	 */
	Dto queryRechargeByActive(Integer activeId);
}
