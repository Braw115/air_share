package com.air.crmuser.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.crmuser.service.UserSectionService;
import com.air.pojo.entity.ActiveType;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.vo.ActiveTypeVo;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageInfo;

/**
 * 用户板块
 *
 */

@Controller
@RequestMapping("/usersection")
public class UserSectionController {
	
	@Resource
	private UserSectionService userSectionService;

	/**
	 * 添加促销
	 * @param activeTypeVo
	 * @return
	 */
	@record(businessLogic="添加促销活动")
	@ResponseBody
	@RequestMapping(value="/addActiveType",method=RequestMethod.POST)
	@RequiresPermissions(value= {"promotionManage"})
	public Dto addActiveType( ActiveTypeVo activeTypeVo){
		
		if (EmptyUtils.isEmpty(activeTypeVo)) {
			return DtoUtil.returnFail("数据为空", ErrorCode.RESP_ERROR);
		}
		if ("".equals(activeTypeVo.getBeginTime())||"".equals(activeTypeVo.getEndTime())) {
			return DtoUtil.returnFail("开始时间或结束时间未选择", ErrorCode.RESP_ERROR);
		}
		if (activeTypeVo.getBeginTime()==null||activeTypeVo.getEndTime()==null) {
			return DtoUtil.returnFail("开始时间或结束时间未选择", ErrorCode.RESP_ERROR);
		}
//		if (activeTypeVo.getHourPrice()==null) {
//			activeTypeVo.setHourPrice(new BigDecimal(0));
//		}
		if (activeTypeVo.getYearPrice()==null) {
			activeTypeVo.setYearPrice(new BigDecimal(0));
		}
		return userSectionService.addActiveType(activeTypeVo);
		
	}
	
	/**
	 * 查询所有促销活动(已发布的和未发布的)
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value="/getAllActiveType")
//	@RequestMapping(value= {"admin"})
//	public Dto getAllActiveType(Integer curPage,Integer pageSize){
//		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)) {
//			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
//		}
//		PageInfo<ActiveType> activetype = userSectionService.queryActiveType(curPage,pageSize);
//		return DtoUtil.returnSuccess("查询成功",activetype);
//	}
	
	/**
	 * 可以根据活动发布类型查询促销活动信息
	 * 可以查询所有促销活动(已发布的和未发布的)
	 * 可以根据活动发布类型查询促销活动信息
	 * 可以根据id获取活动信息
	 * @param activeTypeVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getActiveType")
	@RequiresPermissions(value= {"promotionManage"})
	public Dto getActiveType(ActiveTypeVo activeTypeVo){
		if (EmptyUtils.isEmpty(activeTypeVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isNotEmpty(activeTypeVo.getCurPage())&&EmptyUtils.isNotEmpty(activeTypeVo.getCurPage())) {
			return userSectionService.queryActiveTypeByPage(activeTypeVo);
		}
		return userSectionService.queryActiveType(activeTypeVo);
	}
	
	
	
	/**
	 * 根据活动发布类型查询促销活动信息
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value="/getActiveTypeByType")
////	@RequestMapping(value= {"admin"})
//	public Dto getActiveTypeByType(Integer curPage,Integer pageSize,Boolean shelves){
//		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)) {
//			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
//		}
//		PageInfo<ActiveType> activetype = userSectionService.queryActiveTypeByType(curPage,pageSize,shelves);
//		return DtoUtil.returnSuccess("查询成功",activetype);
//	}
	
	/**
	 * 根据id获取活动信息
	 * @param activeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getActiveTypeById",method=RequestMethod.GET)
	@RequiresPermissions(value= {"promotionManage"})
	public Dto getActiveTypeById(Integer activeId){
		if (EmptyUtils.isEmpty(activeId)) {
			return DtoUtil.returnFail("数据为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.queryActiveTypeById(activeId);
	}
	
//	/**
//	 * 修改活动图片
//	 * @param activeTypeVo
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/modifyActiveTypePic",method=RequestMethod.POST)
//	@RequestMapping(value= {"admin"})
//	public Dto modifyActiveTypePic(ActiveTypeVo activeTypeVo){
//		if (EmptyUtils.isEmpty(activeTypeVo)) {
//			return DtoUtil.returnFail("数据为空", ErrorCode.RESP_ERROR);
//		}
//		return userSectionService.modifyActiveTypePic(activeTypeVo);
//	}
	
	/**
	 * 修改活动信息
	 * @param activeTypeVo
	 * @return
	 */
	@record(businessLogic="修改促销活动")
	@ResponseBody
	@RequestMapping(value="/modifyActiveType",method=RequestMethod.POST)
	@RequiresPermissions(value= {"promotionManage"})
	public Dto modifyActiveType( ActiveTypeVo activeTypeVo){
		if (EmptyUtils.isEmpty(activeTypeVo)) {
			return DtoUtil.returnFail("数据为空", ErrorCode.RESP_ERROR);
		}
//		if (activeTypeVo.getHourPrice()==null) {
//			activeTypeVo.setHourPrice(new BigDecimal(0));
//		}
		if (activeTypeVo.getYearPrice()==null) {
			activeTypeVo.setYearPrice(new BigDecimal(0));
		}
		return userSectionService.modifyActiveType(activeTypeVo);
	}
	
	/**
	 * 发布 和下架
	 * @param activeIdList
	 * @return
	 */
	@record(businessLogic="发布或下架促销活动")
	@ResponseBody
	@RequestMapping(value="/modifyActiveTypeShelves",method=RequestMethod.POST)
	@RequiresPermissions(value= {"promotionManage"})
	public Dto modifyActiveTypeShelves(@RequestBody HashMap<String, Object> map){
		Boolean shelves = (Boolean)map.get("shelves");
		List<Integer> activeIdList = (List<Integer>)map.get("activeIdList");
		if (EmptyUtils.isEmpty(activeIdList)) {
			return DtoUtil.returnFail("数据为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.modifyActiveTypeShelves(activeIdList,shelves);
	}
	
	/**
	 * 删除促销活动信息
	 * @param activeId
	 * @return
	 */
	@record(businessLogic="删除促销活动")
	@ResponseBody
	@RequestMapping(value="/delActiveType",method=RequestMethod.POST,produces="application/json")
	@RequiresPermissions(value= {"promotionManage"})
	public Dto delActiveType( @RequestBody List<Integer> activeIdList ){
		if (EmptyUtils.isEmpty(activeIdList)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.delActiveType(activeIdList);
	}
	
	/**
	 *促销统计 ：促销设备名称、促销时间、金额使用折算金额、使用代金券金额、使用折扣券金额
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllPromotionStatistics",method=RequestMethod.GET)
	@RequiresPermissions(value= {"promotionCount"})
	public Dto getAllPromotionStatistics(Integer curPage,Integer pageSize){
		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.queryAllPromotionStatistics(curPage,pageSize);
	}
	
	/**
	 * 根据设备名字模糊查询
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPromotionStatistics",method=RequestMethod.GET)
	@RequiresPermissions(value= {"promotionManage"})
	public Dto getPromotionStatistics(Integer curPage,Integer pageSize,String name){
		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)&&EmptyUtils.isEmpty(name)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.queryPromotionStatistics(curPage,pageSize,name);
	}
	
	/**
	 * 根据促销活动id查询
	 * @param activeId
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPromotionStatisticsById",method=RequestMethod.GET)
	@RequiresPermissions(value= {"appUserManage"})
	public Dto getPromotionStatisticsById(Integer activeId){
		if (EmptyUtils.isEmpty(activeId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.queryPromotionStatisticsById(activeId);
	}
	
	/**
	 * 查询全部
	 * 会员姓名、手机号、微信昵称、头像、首次消费时间、消费次数、消费金额、优惠金额、累计领取代金券张数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAppUserInfo",method=RequestMethod.GET)
	@RequiresPermissions(value= {"appUserManage"})
	public Dto getAppUserInfo(Integer curPage,Integer pageSize){
		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return userSectionService.queryAppUserInfo(curPage,pageSize);
	}
	
	/**
	 * 获取个人 通过用户id
	 * 会员姓名、手机号、微信昵称、头像、首次消费时间、消费次数、消费金额、优惠金额、累计领取代金券张数 
	 * @param appuserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAppUserPerInfoById",method=RequestMethod.GET)
	@RequiresPermissions(value= {"appUserManage"})
	public Dto getAppUserPerInfoById(Integer appuserId){
		
		return userSectionService.queryAppUserPerInfoById(appuserId);
	}
	
	/**
	 * 通过昵称模糊查询获取
	 * 会员姓名、手机号、微信昵称、头像、首次消费时间、消费次数、消费金额、优惠金额、累计领取代金券张数 
	 * @param appuserId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAppUserPerInfo",method=RequestMethod.GET)
	@RequiresPermissions(value= {"appUserManage"})
	public Dto getAppUserPerInfo(Integer curPage,Integer pageSize,String nickname){
		return userSectionService.queryAppUserPerInfo(curPage,pageSize,nickname);
	}
	
	/**
	 * crm端查看包年包时价格
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getRechargeTypeCrm"),method=RequestMethod.GET)
	@RequiresPermissions(value= {"packYears"})
	public Dto getRechargeTypeCrm() {
		return userSectionService.queryRecharge();
	} 
	
	/**
	 * crm端修改包年包时价格
	 * @param rechargeType
	 * @return
	 */
	@record(businessLogic="修改包年包时价格")
	@ResponseBody
	@RequestMapping(value=("/modifyRechargeTypeCrm"),method=RequestMethod.POST)
	@RequiresPermissions(value= {"packYears"})
	public Dto modifyRechargeTypeCrm(@RequestBody RechargeType rechargeType) {
		return userSectionService.modifyRechargeType(rechargeType);
	} 
}
