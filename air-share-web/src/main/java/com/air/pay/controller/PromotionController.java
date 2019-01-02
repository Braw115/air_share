package com.air.pay.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.UserSectionService;
import com.air.pay.service.PromotionService;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.RedBoxActive;
import com.air.pojo.entity.RedBoxRate;
import com.air.pojo.entity.VoucherActive;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.air.pojo.vo.RedBoxActiveVo;
import com.air.pojo.vo.VoucherActiveVo;
import com.air.user.service.AppUserService;
import com.air.user.service.OrderService;
import com.air.utils.DateFormats;
import com.air.utils.EmptyUtils;
import com.air.utils.FileUploader;
import com.alibaba.fastjson.JSONObject;

/**
 *发放红包，代金券折扣券活动 
 *
 */

@Controller
@RequestMapping("/Promotion")
public class PromotionController {

	@Resource
	private AppUserService appUserService;
	
	@Resource
	private PromotionService promotionService;
	
	@Resource
	private UserSectionService userSectionService;
	
	@Resource
	private OrderService orderService;

	/**
	 * 添加红包活动
	 * @param redBoxActive
	 * @return
	 */
	@record(businessLogic="添加红包活动 ")
	@ResponseBody
	@RequestMapping(value="/addRedBoxActive",method=RequestMethod.POST)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto addRedBoxActive( RedBoxActiveVo redBoxActiveVo) {
		
		if (EmptyUtils.isEmpty(redBoxActiveVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		if (redBoxActiveVo.getImgUrlList().isEmpty()) {
			return DtoUtil.returnFail("图片信息为空", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isEmpty(redBoxActiveVo.getRateListJson())) {
			return DtoUtil.returnFail("红包活动获奖区间和比例信息为空", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isEmpty(redBoxActiveVo.getBeginTime())||EmptyUtils.isEmpty(redBoxActiveVo.getEndTime())) {
			return DtoUtil.returnFail("红包活动开始和结束时间信息为空", ErrorCode.RESP_ERROR);
		}
		List<RedBoxRate> list = JSONObject.parseArray(redBoxActiveVo.getRateListJson(), RedBoxRate.class);
		redBoxActiveVo.setRateList(list);
		String imgUrl = FileUploader.upload(redBoxActiveVo.getImgUrlList()).getMsg();
		redBoxActiveVo.setImgUrl(imgUrl);
		Boolean bool = promotionService.addRedBoxActive(redBoxActiveVo);
		if (!bool) {
			return DtoUtil.returnFail("添加红包活动失败", ErrorCode.RESP_ERROR);
		}
		
		for (RedBoxRate redBoxRate : redBoxActiveVo.getRateList()) {
			redBoxRate.setRedBoxActiveId(redBoxActiveVo.getRedBoxActiveId());
		}
		return promotionService.addRedBoxRates(redBoxActiveVo.getRateList());
	}
	
	/**
	 * 添加红包活动获奖区间和比例信息
	 * @param redBoxRates
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addRedBoxRate",method=RequestMethod.POST)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto addRedBoxRate(@RequestBody List<RedBoxRate>  redBoxRates) {
		
		if (EmptyUtils.isEmpty(redBoxRates)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		return promotionService.addRedBoxRates(redBoxRates);
	}
	
	/**
	 * 修改红包活动信息
	 * @param redBoxActive
	 * @return
	 */
	@record(businessLogic="修改红包活动信息 ")
	@ResponseBody
	@RequestMapping(value="/modifyRedBoxActive",method=RequestMethod.POST)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto modifyRedBoxActive(RedBoxActiveVo redBoxActiveVo) {
		
		if (EmptyUtils.isEmpty(redBoxActiveVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		if (!(redBoxActiveVo.getImgUrlList()==null)) {
			String imgUrl = FileUploader.upload(redBoxActiveVo.getImgUrlList()).getMsg();
			redBoxActiveVo.setImgUrl(imgUrl);
		}
		
		List<RedBoxRate> list = JSONObject.parseArray(redBoxActiveVo.getRateListJson(), RedBoxRate.class);
		redBoxActiveVo.setRateList(list);
		for (RedBoxRate redBoxRate : redBoxActiveVo.getRateList()) {
			redBoxRate.setRedBoxActiveId(redBoxActiveVo.getRedBoxActiveId());
		}
		RedBoxActive redBoxActive = new RedBoxActive();
		BeanUtils.copyProperties(redBoxActiveVo, redBoxActive);
		Boolean boolBox = promotionService.modifyRedBoxActive(redBoxActive);
		
		if (!boolBox) {
			return DtoUtil.returnFail("修改红包活动信息失败", ErrorCode.RESP_ERROR);
		}
		Boolean boolDel = promotionService.delRedBoxRates(redBoxActiveVo.getRedBoxActiveId());
		if (!boolBox) {
			return DtoUtil.returnFail("修改红包活动信息失败", ErrorCode.RESP_ERROR);
		}
		
		return promotionService.addRedBoxRates(redBoxActiveVo.getRateList());
	}
	
	/**
	 * 发布和下架红包活动 
	 * @param redBoxActiveIdlist
	 * @param status
	 * @return
	 */
	@record(businessLogic="发布和下架红包活动 ")
	@ResponseBody
	@RequestMapping(value="/modifyRedBoxActiveStatus",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto modifyRedBoxActiveStatus(@RequestParam("redBoxActiveIdlist") List<Integer> redBoxActiveIdlist,Boolean status) {
		
		if (EmptyUtils.isEmpty(redBoxActiveIdlist)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.modifyRedBoxActiveStatus(redBoxActiveIdlist,status);
	}
	

	/**
	 * 根据红包活动状态分页查询红包活动信息
	 * @param curPage
	 * @param pageSize
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxActiveByStatus",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto getRedBoxActiveByStatus(Integer curPage , Integer pageSize,String status) {
		
		if (EmptyUtils.isEmpty(curPage)&&EmptyUtils.isEmpty(pageSize)&&EmptyUtils.isEmpty(status)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.queryRedBoxActiveByStatus(curPage,pageSize,status);
	}
	
	/**
	 * 通过名称模糊查询红包活动信息
	 * 分页查询所有红包活动信息和相应的区间比例
	 * 根据红包活动id查询红包活动信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxActive",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto getRedBoxActive(RedBoxActiveVo redBoxActiveVo) {
		
		if (EmptyUtils.isNotEmpty(redBoxActiveVo.getCurPage())&&EmptyUtils.isNotEmpty(redBoxActiveVo.getPageSize())) {
			//有分页条件进来
			return promotionService.queryRedBoxActiveByPage(redBoxActiveVo);
		}
		return promotionService.queryRedBoxActive(redBoxActiveVo);
	}
	
	/**
	 * 根据id删除红包活动信息
	 * @return
	 */
	@record(businessLogic="根据id删除红包活动信息 ")
	@ResponseBody
	@RequestMapping(value="/delRedBoxActive",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto delRedBoxActive(Integer redBoxActiveId) {
		if (EmptyUtils.isEmpty(redBoxActiveId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.delRedBoxActive(redBoxActiveId);
	}
	
	/**
	 * 根据红包活动id查询红包活动信息
	 * @param redBoxActiveId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxActiveById",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto getRedBoxActiveById(Integer redBoxActiveId) {
		
		if (EmptyUtils.isEmpty(redBoxActiveId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.queryRedBoxActiveById(redBoxActiveId);
	}
	
//	/**
//	 * 满足条件获取红包 
//	 * @param appusersId
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping("/getRedBox")
//	public Dto getRedBox(String token,Integer redBoxActiveId) {
//		if (!TokenUtil.respAppReturn(token)) {
//			return DtoUtil.returnFail("请重新登录", ErrorCode.RESP_ERROR);
//		}
//		return promotionService.getRedBox(TokenUtil.getAppUserId(token),redBoxActiveId);
//	}
	
	/**
	 * 碳指标红包活动查询（是否存在上架）
	 * @param appusersId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCarbonRedBox",method=RequestMethod.GET)
	public Dto getCarbonRedBox(HttpServletRequest request,String activeName) {
		return promotionService.queryCarbonRedBox(TokenUtil.getAppUserId(request.getHeader("token")),activeName);
	}
		
	/**
	 * 碳指标满足条件获取红包 
	 * @param appusersId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxByCarbon",method=RequestMethod.POST)
	public Dto getRedBox(HttpServletRequest request) {
		String activeName = "carbon";
		return promotionService.queryRedBoxByCarbon(TokenUtil.getAppUserId(request.getHeader("token")),activeName);
	}
	
	/**
	 * 获取红包和优惠券活动
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxAndVoucher",method=RequestMethod.GET)
	public Dto getRedBoxAndVoucher() {
		String nowTime = DateFormats.getDateFormat();
		
		List<BoxAndVoucherVo> boxList = promotionService.queryRedBoxs(nowTime);
		List<BoxAndVoucherVo> voucherList = promotionService.queryVoucherActive(nowTime);
		List<BoxAndVoucherVo> ActiveList = userSectionService.queryActive(nowTime);
		boxList.addAll(voucherList);
		boxList.addAll(ActiveList);
		
		return DtoUtil.returnDataSuccess(boxList);
	}
	
//	/**
//	 * 根据用户id获取红包和优惠券活动（用户可以参与的未领取的）
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value="/getRedBoxAndVoucherByPer",method=RequestMethod.GET)
//	public Dto getRedBoxAndVoucherByPer(HttpServletRequest request) {
//		String nowTime = DateFormats.getDateFormat();
//		
//		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
//		
//		List<BoxAndVoucherVo> boxList = promotionService.queryRedBoxs(nowTime,appusersId);
//		List<BoxAndVoucherVo> voucherList = promotionService.queryVoucherActive(nowTime,appusersId);
//		
//		boxList.addAll(voucherList);
//		
//		return DtoUtil.returnDataSuccess(boxList);
//	}
	
	/**
	 * 根据活动id，type获取红包或优惠券活动
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxAndVoucherInfo",method=RequestMethod.GET)
	public Dto getRedBoxAndVoucherInfo(HttpServletRequest request,Integer id,String type) {
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		
		// A 优惠券  B 红包
		if ("A".equals(type)) {
			return promotionService.queryVoucherActiveOrNot(id,appusersId);
		}
//		if ("".equals(type)) {
//			return userSectionService.queryActiveOrNot(id,appusersId);
//		}
		return promotionService.queryRedBoxActiveOrNot(id,appusersId);
	}
	
	/**
	 * 用户获取红包或者获取优惠券
	 * @param request
	 * @param boxAndVoucherVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addRedBoxOrVoucherForUser",method=RequestMethod.POST)
	public Dto addRedBoxOrVoucherForUser(HttpServletRequest request,@RequestBody BoxAndVoucherVo boxAndVoucherVo) {
		Integer appusersId = TokenUtil.getAppUserId(request.getHeader("token"));
		
		if (EmptyUtils.isEmpty(boxAndVoucherVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}

		// V 优惠券  B 红包  A 促销活动
		if ("A".equals(boxAndVoucherVo.getType())) {
			return promotionService.addVoucherForAppUser(appusersId,boxAndVoucherVo);
		}
		//根据活动生成订单
//		if ("A".equals(boxAndVoucherVo.getType())) {
//			return orderService.addActiveOrder(appusersId,boxAndVoucherVo);
//		}
		
		return promotionService.addRedBoxForAppUser(appusersId,boxAndVoucherVo);
	}
	
	/**
	 * 把红包转入余额 
	 * @param appusersId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/putRedBoxToBalance",method=RequestMethod.POST)
	public Dto putRedBoxToBalance(HttpServletRequest req) {
		AppUser appUser = new AppUser();
		appUser.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		return appUserService.modifyBalance(appUser);
	}
	
	/**
	 * 获取代金券折扣券
	 * @param appusersId
	 * @param voucherActiveId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addVouchers",method=RequestMethod.POST)
	public Dto addVouchers(HttpServletRequest req,Integer voucherActiveId) {
		Vouchers voucher =new Vouchers();
		voucher.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		if (EmptyUtils.isEmpty(voucherActiveId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		voucher.setVoucherActiveId(voucherActiveId);
		voucher.setType("vou");
		return promotionService.addVoucherForUser(voucher);
	}
	
	/**
	 * 查询用户优惠券
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getVouchers",method=RequestMethod.GET)
	public Dto getVouchers(HttpServletRequest request) {
		return promotionService.queryVouchers(TokenUtil.getAppUserId(request.getHeader("token")));
	}
	
	/**
	 * 添加代金券折扣券活动
	 * @param voucherActive
	 * @return
	 */
	@record(businessLogic="添加代金券折扣券活动")
	@ResponseBody
	@RequestMapping(value="/addVoucherActive",method=RequestMethod.POST)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto addVoucherActive( VoucherActiveVo voucherActiveVo) {
		
		if (EmptyUtils.isEmpty(voucherActiveVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		if (voucherActiveVo.getImgUrlList().isEmpty()) {
			return DtoUtil.returnFail("图片信息为空", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isEmpty(voucherActiveVo.getBeginTime())||EmptyUtils.isEmpty(voucherActiveVo.getEndTime())) {
			return DtoUtil.returnFail("优惠券活动开始和结束时间信息为空", ErrorCode.RESP_ERROR);
		}
		String imgUrl = FileUploader.upload(voucherActiveVo.getImgUrlList()).getMsg();
		VoucherActive voucherActive =new VoucherActive();
		BeanUtils.copyProperties(voucherActiveVo, voucherActive);
		voucherActive.setImgUrl(imgUrl);
		return promotionService.addVoucherActive(voucherActive);
	}
	
	/**
	 * 修改代金券折扣券活动信息
	 * @param redBoxActive
	 * @return
	 */
	@record(businessLogic="修改代金券折扣券活动信息")
	@ResponseBody
	@RequestMapping(value="/modifyVoucherActive",method=RequestMethod.POST)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto modifyVoucherActive( VoucherActiveVo voucherActiveVo) {
		
		if (EmptyUtils.isEmpty(voucherActiveVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		VoucherActive voucherActive =new VoucherActive();
		BeanUtils.copyProperties(voucherActiveVo, voucherActive);
		if (!(voucherActiveVo.getImgUrlList()==null)) {
			String imgUrl = FileUploader.upload(voucherActiveVo.getImgUrlList()).getMsg();
			voucherActive.setImgUrl(imgUrl);
		}
		
		return promotionService.modifyVoucherActive(voucherActive);
	}
	
	/**
	 * 批量发布和下架优惠券活动
	 * @param voucherActiveIdlist
	 * @param status
	 * @return
	 */
	@record(businessLogic="批量发布和下架优惠券活动 ")
	@ResponseBody
	@RequestMapping(value="/modifyVoucherActiveStatus",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto modifyVoucherActiveStatus(@RequestParam("voucherActiveIdlist")List<Integer> voucherActiveIdlist,Boolean status) {
		
		if (EmptyUtils.isEmpty(voucherActiveIdlist)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.modifyVoucherActiveStatus(voucherActiveIdlist,status);
	}
	

	/**
	 * 根据代金券活动状态分页查询红包活动信息
	 * 可以分页查询所有代金券折扣券活动信息
	 * 可以通过名称模糊查询代金券折扣券活动信息
	 * 根据代金券折扣券活动id查询代金券折扣券活动信息
	 * 可以发布和下架活动
	 * @param voucherActiveVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getVoucherActive",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto getVoucherActiveByStatus(VoucherActiveVo voucherActiveVo ) {
		if (EmptyUtils.isNotEmpty(voucherActiveVo.getCurPage())&&EmptyUtils.isNotEmpty(voucherActiveVo.getPageSize())) {
			return promotionService.queryVoucherActiveByPage(voucherActiveVo);
		}
		return promotionService.queryVoucherActive(voucherActiveVo);
	}
	
	/**
	 * 删除优惠券活动
	 * @param voucherActiveId
	 * @return
	 */
	@record(businessLogic="删除优惠券活动 ")
	@ResponseBody
	@RequestMapping(value="/delVoucherActive",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto delVoucherActive(Integer voucherActiveId) {
		if (EmptyUtils.isEmpty(voucherActiveId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.delVoucherActive(voucherActiveId);
	}
	
	/**
	 * 修改红包活动获奖区间和比例信息
	 * @param redBoxRates
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/modifyRedBoxRate",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto modifyRedBoxRate(@RequestBody List<RedBoxRate>  redBoxRates) {
		
		if (EmptyUtils.isEmpty(redBoxRates)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.modifyRedBoxRates(redBoxRates);
	}
	
	/**
	 * 获取红包活动获奖区间和比例信息
	 * @param redBoxActiveId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRedBoxRateBy",method=RequestMethod.GET)
	@RequiresPermissions(value= {"redPacketManage"})
	public Dto getRedBoxRateByRedBoxActiveId(Integer redBoxActiveId) {
		
		if (EmptyUtils.isEmpty(redBoxActiveId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return promotionService.queryRedBoxRateByRedBoxActiveId(redBoxActiveId);
	}
	
	
}
