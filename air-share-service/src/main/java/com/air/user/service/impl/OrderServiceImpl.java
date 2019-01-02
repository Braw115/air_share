package com.air.user.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.aircondition.mapper.AirConditionMapper;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.GenerateNum;
import com.air.crmuser.mapper.UserSectionMapper;
import com.air.pojo.entity.ActiveType;
import com.air.pojo.entity.AirCondition;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.AppUserAir;
import com.air.pojo.entity.Order;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.entity.VoucherActive;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.OrderVo;
import com.air.user.mapper.AppUserMapper;
import com.air.user.mapper.OrderMapper;
import com.air.user.mapper.PromotionMapper;
import com.air.user.service.OrderService;
import com.air.utils.DateFormats;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Resource
	private OrderMapper orderMapper;
	
	@Resource
	private AppUserMapper appUserMapper;
	
	@Resource
	private AirConditionMapper airConditionMapper;
	
	@Resource
	private UserSectionMapper userSectionMapper;
	
	@Resource
	private PromotionMapper promotionMapper;
	
	/**
	 * 添加订单
	 */
	@Override
	public Dto addOrder(Order order) {
		RechargeType rechargeType = userSectionMapper.selectRechargeTypeById(order.getRechargeId());
		/** 有促销活动*/
//		ActiveType dbActiveType =null;
//		if (EmptyUtils.isNotEmpty(order.getActiveId())) {
//			dbActiveType = userSectionMapper.selectActiveTypeById(order.getActiveId());
//			rechargeType = getRealPrice(rechargeType,dbActiveType);
//		}
		AppUser dbAppUser = appUserMapper.selectById(order.getAppusersId());
		order.setTelephone(dbAppUser.getTelephone());
		Boolean flag = isOrNotSuccess(rechargeType,dbAppUser);
		if (!flag) {
			return DtoUtil.returnFail("生成订单失败，您还有剩余时间未使用，您选择的类型与您自身使用时间类型不符", ErrorCode.RESP_ERROR);
		}
		
//		List<ActiveType> activeTypes = userSectionMapper.selectActiveTypeByAirMAC(order.getAirmac());
		//理论价格
		BigDecimal theoprice = rechargeType.getPrice().multiply(new BigDecimal(order.getNum()));
		order.setTheoprice(theoprice);
//		if (EmptyUtils.isNotEmpty(dbActiveType)&&EmptyUtils.isNotEmpty(dbActiveType.getDiscount())) {
//			//折扣促销，原价格乘折扣
//			rechargeType.setPrice((rechargeType.getPrice().multiply(new BigDecimal(dbActiveType.getDiscount())).subtract(new BigDecimal(100))));			
//		}

		BigDecimal realfee = theoprice;//实际所付价格
		//活动进行区域判断 是否发布且截止时间未过的第一条数据为准
//		if (EmptyUtils.isNotEmpty(activeTypes)) {
//			ActiveType activeType = activeTypes.get(0);
//			realfee=activeType.getPrice().multiply(new BigDecimal(activeType.getDiscount()*order.getNum()));
//		}
		if (EmptyUtils.isNotEmpty(order.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			if ("yes".equals(voucher.getStatus())) {
				return DtoUtil.returnFail("代金券已使用", ErrorCode.RESP_ERROR);
			}
			// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
			//int c = a.compareTo(b); // 结果 C = 1
			if (1==(voucher.getMinimum()).compareTo(theoprice)) {
				return DtoUtil.returnFail("代金券不满足条件", ErrorCode.RESP_ERROR);
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"vou".equals(voucher.getType())) {
				realfee=realfee.subtract(voucher.getFacevalue());
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"dis".equals(voucher.getType())) {
				realfee=realfee.multiply(new BigDecimal(voucher.getDiscountRatio())).divide(new BigDecimal(100),2);
			}
//			voucher.setStatus("yes");
//			Boolean boolVoucher = orderMapper.updateVoucherById(voucher);
//			if (!boolVoucher) {
//				return DtoUtil.returnFail("修改用户代金券状态失败", ErrorCode.RESP_ERROR);
//			}
		}
		order.setRealfee(realfee);
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaystatus("no");
		Boolean boolOrder = orderMapper.insertOrder(order);
		
		if (!boolOrder) {
			return DtoUtil.returnFail("添加订单失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加订单成功", order);
	}

//	private RechargeType getRealPrice(RechargeType rechargeType, ActiveType dbActiveType) {
//		
//		if (EmptyUtils.isNotEmpty(dbActiveType.getDiscount())) {
//			rechargeType.setPrice((rechargeType.getPrice().divide(new BigDecimal(100),2).multiply(new BigDecimal(dbActiveType.getDiscount()))));
//			return rechargeType;
//		}
//		if ("year".equals(rechargeType.getTypeName())) {
//			rechargeType.setPrice(rechargeType.getPrice().subtract(dbActiveType.getYearPrice()));
//		}if ("hour".equals(rechargeType.getTypeName())) {
//			rechargeType.setPrice(rechargeType.getPrice().subtract(dbActiveType.getHourPrice()));
//		}
//		return rechargeType;
//	}

	/**
	 * 订单充值类型（年或小时）与用户原有的相比，进行判断是否生成订单
	 * @param rechargeType
	 * @param dbAppUser
	 * @return
	 */
	private Boolean isOrNotSuccess(RechargeType rechargeType, AppUser dbAppUser) {
		if ("".equals(dbAppUser.getModel())||dbAppUser.getModel()==null) {
			return true;
		}
		if (dbAppUser.getModel().equals(rechargeType.getTypeName())) {
			return true;
		}
		if ("hour".equals(dbAppUser.getModel())) {
			return dbAppUser.getUseTime()<=0;
		}
		if ("year".equals(dbAppUser.getModel())) {
			return dbAppUser.getUseTime()<=System.currentTimeMillis();
		}
		return false;
	}

	@Override
	public Dto queryOrder(OrderVo orderVo) {
		PageHelper.startPage(orderVo.getCurPage(), orderVo.getPageSize());
		List<OrderVo> orderList = orderMapper.selectValueOrderVo(orderVo);
		PageInfo<OrderVo> orderInfo = new PageInfo<>(orderList);
		
		return DtoUtil.returnSuccess("ok",orderInfo);
	}

	@Override
	public Dto queryVoucherByAppuserId(Integer appUserId) {
		Calendar calendar = Calendar.getInstance();
		Vouchers voucher = new Vouchers();
		voucher.setStatus("no");
		voucher.setAppusersId(appUserId);
		List<Vouchers> vouchersList = orderMapper.selectVoucherByAppuserId(voucher);
		if (vouchersList.isEmpty()) {
			return DtoUtil.returnSuccess("ok", vouchersList);
		}
		List<Vouchers> vouchersListVo = null;
		for (Vouchers vouchers : vouchersList) {
			calendar.setTime(voucher.getCreated());
			calendar.add(Calendar.DAY_OF_YEAR, voucher.getValidTime());
			if (calendar.before(new Date())) {
				vouchersListVo.add(voucher);
			}
		}
		if (vouchersListVo.isEmpty()) {
			return DtoUtil.returnFail("无可以用优惠券", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", vouchersListVo);
	}

	
	/**
	 * 付款成功后修改订单
	 */
	@Override
	public Dto modifyOrderInfo(Order order) {
		order.setPaystatus("yes");
		String date = DateFormats.getDateFormat();
		order.setPaytime(date);
		Boolean boolOrder = orderMapper.updateOrderById(order);
		
		if (EmptyUtils.isNotEmpty(order.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			voucher.setStatus("yes");
			Boolean boolVoucher = orderMapper.updateVoucherById(voucher);
			if (!boolVoucher) {
				return DtoUtil.returnFail("修改用户代金券状态失败", ErrorCode.RESP_ERROR);
			}
		}
		if (!boolOrder) {
			return DtoUtil.returnFail("修改订单失败", ErrorCode.RESP_ERROR);
		}
		List<VoucherActive> voucherActiveList = null;
		if ("cost".equals(order.getPaytype())) {//直接购买时间类型订单（需要修改空调使用时间）
//			return modifyAppUser(order);
			return modifyAirCondition(order);
			
		}else {//充值类订单
			AppUser appUser = appUserMapper.selectByPhone(order.getTelephone());
			//修改用户余额
			if (EmptyUtils.isEmpty(appUser.getBalance())) {
				appUser.setBalance(new BigDecimal(0));
			}
			appUser.setBalance(appUser.getBalance().add(order.getTheoprice()));
			Boolean boolAppUser = appUserMapper.updateAppUser(appUser);
			if (!boolAppUser) {
				return DtoUtil.returnFail("修改用户余额失败", ErrorCode.RESP_ERROR);
			}
		}
		
		return DtoUtil.returnSuccess("ok");
	}

	/**
	 * 修改空调使用时间
	 * @param order
	 * @return
	 */
	private Dto modifyAirCondition(Order order) {
//之前的使用时间代码
//		AirCondition airCondition = airConditionMapper.selectAirConditionById(order.getAirmac());
//		
//		Date date = new Date();
//		Calendar cal = Calendar.getInstance();
//	 	cal.setTime(date);//设置初始时间
//	 	long begin = cal.getTimeInMillis();
//	 	
//	 	if (null!=airCondition.getTime()&&airCondition.getTime()>begin) {
//	 		cal.setTimeInMillis(airCondition.getTime());
//		}	 	
//	 	
//	 	cal.add(Calendar.YEAR, order.getNum());
//	 	airCondition.setTime(cal.getTimeInMillis());
//	 	Integer integer = airConditionMapper.updateAirCondition(airCondition);
//	 	
//	 	if (integer>0) {
//			return DtoUtil.returnSuccess("修改空调使用时间成功");
//		}
		
		
		
		AppUserAir appUserAir = airConditionMapper.selectAppUserAirByUseIdAndMac(order.getAppusersId(), order.getAirmac());
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	 	cal.setTime(date);//设置初始时间
	 	long begin = cal.getTimeInMillis();
	 	
	 	if (null!=appUserAir.getTime()&&appUserAir.getTime()>begin) {
	 		cal.setTimeInMillis(appUserAir.getTime());
		}	 	
	 	
	 	cal.add(Calendar.YEAR, order.getNum());
	 	appUserAir.setTime(cal.getTimeInMillis());
	 	appUserAir.setModel("year");
	 	Integer integer = airConditionMapper.updateAirConditionNote(appUserAir);
	 	
	 	if (integer>0) {
			return DtoUtil.returnSuccess("修改空调使用时间成功");
		}

		return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
	}

	/**
	 * 修改用户
	 * @param order
	 * @return
	 */
	private Dto modifyAppUser(Order order) {
		AppUser updateAppUser = appUserMapper.selectById(order.getAppusersId());
		RechargeType dbRechargeType = userSectionMapper.selectRechargeTypeById(order.getRechargeId());
	
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
	 	cal.setTime(date);//设置初始时间
	 	long begin = cal.getTimeInMillis();
	 	
	 	String dbModel = updateAppUser.getModel();
	 	updateAppUser.setModel(dbRechargeType.getTypeName());
		
	 	if ("".equals(dbModel)||dbModel==null) {
			updateAppUser = modifyAppUserModelAnd(updateAppUser,dbRechargeType,order,cal);
		}//包年
	 	else if ("year".equals(updateAppUser.getModel())&&updateAppUser.getUseTime()<=begin) {
			updateAppUser = modifyAppUserModelAnd(updateAppUser,dbRechargeType,order,cal);
		}else if ("year".equals(updateAppUser.getModel())&&updateAppUser.getUseTime()>begin) {
			//在原来未使用完的时间上加上新购买的
			Date dbDate = DateFormats.transForDate(updateAppUser.getUseTime());
			cal.setTime(dbDate);
			cal.add(Calendar.YEAR, order.getNum());
			updateAppUser.setUseTime(cal.getTimeInMillis());
		}
		//按小时消费
		else if ("hour".equals(dbRechargeType.getTypeName())&&updateAppUser.getUseTime()<=0) {
			updateAppUser = modifyAppUserModelAnd(updateAppUser,dbRechargeType,order,cal);
		}else {
			updateAppUser.setUseTime(updateAppUser.getUseTime()+(long)(order.getNum()*ErrorCode.ONE_HOUR));
		}
		
		
		Boolean bool = appUserMapper.updateAppUser(updateAppUser);
		
		if (!bool) {
			return DtoUtil.returnFail("修改用户使用时间失败", ErrorCode.RESP_ERROR);
		}
//		List<RedBoxActive> redBoxActiveList = promotionMapper.selectAllRedBoxActiveByOrder(order);
//		if (EmptyUtils.isEmpty(redBoxActiveList)) {
//			return DtoUtil.returnSuccess("修改用户使用时间成功");
//		}
		
//		return DtoUtil.returnSuccess("修改用户使用时间成功", redBoxActiveList.get(0));
		return DtoUtil.returnSuccess("修改用户使用时间成功");
	}
	
	/**
	 * 修改用户使用时间
	 * @param updateAppUser
	 * @param dbRechargeType
	 * @param order
	 * @return
	 */
	private AppUser modifyAppUserModelAnd(AppUser updateAppUser, RechargeType dbRechargeType,Order order,Calendar cal) {
		if ("hour".equals(dbRechargeType.getTypeName())) {
			updateAppUser.setUseTime((long)(order.getNum()*ErrorCode.ONE_HOUR));
		}else if("year".equals(updateAppUser.getModel())) {
			cal.add(Calendar.YEAR, order.getNum());//增加购买的年数
			updateAppUser.setUseTime(cal.getTimeInMillis());
		}
		return updateAppUser;
	}


	/**
	 * 通过订单号获取订单
	 */
	@Override
	public Order queryOrderByNo(String orderno) { 
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderno(orderno);
		return orderMapper.selectOrder(orderVo);
	}

	/**
	 * 余额支付
	 */
	@Override
	public Dto modifyOrderByBalance(Order order) {
		order.setPaymethod("余额");
		AppUser appUser = appUserMapper.selectById(order.getAppusersId());
		
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderno(order.getOrderno());
		Order dbOrder = orderMapper.selectOrder(orderVo);
		
		if ("yes".equals(dbOrder.getPaystatus())) {
			return DtoUtil.returnFail("订单已支付", ErrorCode.RESP_ERROR);
		}
		if ("".equals(appUser.getBalance())||appUser.getBalance()==null) {
			appUser.setBalance(new BigDecimal(0));
		}
		// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
		//int c = a.compareTo(b); // 结果 C = 1
		if (1==order.getRealfee().compareTo(appUser.getBalance())) {
			return DtoUtil.returnFail("余额不足", ErrorCode.RESP_ERROR);
		}
		
		if (EmptyUtils.isNotEmpty(dbOrder.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			voucher.setStatus("yes");
			Boolean boolVoucher = orderMapper.updateVoucherById(voucher);
			if (!boolVoucher) {
				return DtoUtil.returnFail("修改用户代金券状态失败", ErrorCode.RESP_ERROR);
			}
		}
		//修改用户余额
		appUser.setBalance(appUser.getBalance().subtract(order.getRealfee()));
		Boolean boolAppUser = appUserMapper.updateAppUser(appUser);
		if (!boolAppUser) {
			return DtoUtil.returnFail("修改用户余额失败", ErrorCode.RESP_ERROR);
		}
		
		return modifyOrderInfo(order);
		
	}
	
	/*public Dto modifyOrderByBalance(Order order) {
		order.setPaymethod("余额");
		AppUser appUser = appUserMapper.selectById(order.getAppusersId());
		
		if (EmptyUtils.isEmpty(appUser)) {
			return DtoUtil.returnFail("订单用户不存在", ErrorCode.RESP_ERROR);
		}
		
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderno(order.getOrderno());
		Order dbOrder = orderMapper.selectOrder(orderVo);
		
		if ("yes".equals(dbOrder.getPaystatus())) {
			return DtoUtil.returnFail("订单已支付", ErrorCode.RESP_ERROR);
		}
		if ("".equals(appUser.getBalance())||appUser.getBalance()==null) {
			appUser.setBalance(new BigDecimal(0));
		}
		// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
		//int c = a.compareTo(b); // 结果 C = 1
		if (1==order.getRealfee().compareTo(appUser.getBalance())) {
			return DtoUtil.returnFail("余额不足", ErrorCode.RESP_ERROR);
		}
		
		if (EmptyUtils.isNotEmpty(dbOrder.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			voucher.setStatus("yes");
			Boolean boolVoucher = orderMapper.updateVoucherById(voucher);
			if (!boolVoucher) {
				return DtoUtil.returnFail("修改用户代金券状态失败", ErrorCode.RESP_ERROR);
			}
		}
		//修改用户余额
		appUser.setBalance(appUser.getBalance().subtract(order.getRealfee()));
		Boolean boolAppUser = appUserMapper.updateAppUser(appUser);
		if (!boolAppUser) {
			return DtoUtil.returnFail("修改用户余额失败", ErrorCode.RESP_ERROR);
		}
		
		return modifyOrderInfo(order);
		
	}*/
	
//	public Dto modifyAirCondition(Order order) {
//		AirCondition airCondition = new AirCondition();
//		airCondition.setRechargeId(order.getRechargeId());
//		airCondition.setBalance(order.getNum());
//		Integer updateAirCondition = airConditionMapper.updateAirCondition(airCondition);
//		if (updateAirCondition!=1) {
//			return DtoUtil.returnFail("修改空调使用时间失败", ErrorCode.RESP_ERROR);
//		}
//		
//		return DtoUtil.returnSuccess("success");
//	}

	@Override
	public Dto addOrderForValue(Order order) {
		AppUser dbAppUser = appUserMapper.selectById(order.getAppusersId());
		order.setTelephone(dbAppUser.getTelephone());
		BigDecimal realfee = order.getTheoprice();//实际所付价格
		//活动进行区域判断 是否发布且截止时间未过的第一条数据为准
//		if (EmptyUtils.isNotEmpty(activeTypes)) {
//			ActiveType activeType = activeTypes.get(0);
//			realfee=activeType.getPrice().multiply(new BigDecimal(activeType.getDiscount()*order.getNum()));
//		}
		if (EmptyUtils.isNotEmpty(order.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			if ("vou".equals(voucher.getType())) {
				realfee=realfee.subtract(voucher.getFacevalue());
			}
			if ("dis".equals(voucher.getType())) {
				realfee=realfee.multiply(new BigDecimal(voucher.getDiscountRatio())).divide(new BigDecimal(100),2);
			}
			voucher.setStatus("yes");
			Boolean boolVoucher = orderMapper.updateVoucherById(voucher);
			if (!boolVoucher) {
				return DtoUtil.returnFail("修改用户代金券状态失败", ErrorCode.RESP_ERROR);
			}

		}
		
		order.setRealfee(realfee);
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaystatus("no");
		Boolean boolOrder = orderMapper.insertOrder(order);
		
		if (!boolOrder) {
			return DtoUtil.returnFail("添加订单失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加充值订单成功", order);
	}

	@Override
	public Dto delOrder(List<Integer> orderIdList) {
		Boolean boolOrder = orderMapper.deleteOrder(orderIdList);
		if (!boolOrder) {
			return DtoUtil.returnFail("删除订单成功", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除订单成功");
	}

	@Override
	public Dto queryOrderByPage(OrderVo orderVo) {
		PageHelper.startPage(orderVo.getCurPage(), orderVo.getPageSize());
		List<OrderVo> orderList = orderMapper.selectOrderVo(orderVo);
		PageInfo<OrderVo> orderInfo = new PageInfo<>(orderList);
		if (EmptyUtils.isEmpty(orderInfo)) {
			return DtoUtil.returnFail("无订单可供查询", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok",orderInfo);
	}

	/*@Override
	public Dto addActiveOrder(Order order) {
		RechargeType rechargeType = userSectionMapper.selectRechargeTypeById(order.getRechargeId());
		ActiveType dbActiveType = userSectionMapper.selectActiveTypeById(order.getActiveId());
		
		AppUser dbAppUser = appUserMapper.selectById(order.getAppusersId());
		order.setTelephone(dbAppUser.getTelephone());
	
		Boolean flag = isOrNotSuccess(rechargeType,dbAppUser);
		if (!flag) {
			return DtoUtil.returnFail("生成订单失败，您还有剩余时间未使用，您选择的类型与您自身使用时间类型不符", ErrorCode.RESP_ERROR);
		}
		BigDecimal theoprice = rechargeType.getPrice().multiply(new BigDecimal(order.getNum()));
		order.setTheoprice(theoprice);
		BigDecimal realfee ;
		if ("year".equals(rechargeType.getTypeName())) {
			realfee = getPrice(dbActiveType,rechargeType,order);
		}else {
			realfee = getPrice(dbActiveType,rechargeType,order);
		}
		order.setRealfee(realfee);
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaystatus("no");
		Boolean boolOrder = orderMapper.insertOrder(order);
		
		if (!boolOrder) {
			return DtoUtil.returnFail("添加订单失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加订单成功", order);
	}*/
	
	public Dto addActiveOrder(Order order) {
		RechargeType rechargeType = userSectionMapper.selectRechargeTypeById(order.getRechargeId());
		AirCondition condition = airConditionMapper.selectAirConditionById(order.getAirmac());
		ActiveType dbActiveType = userSectionMapper.selectActiveTypeById(order.getActiveId());
		
		AppUser dbAppUser = appUserMapper.selectById(order.getAppusersId());
		if (EmptyUtils.isEmpty(condition)) {
			return DtoUtil.returnFail("该空调已不存在", ErrorCode.RESP_ERROR);
		}
		
		if (!(0==condition.getUseStatus())) {
			return DtoUtil.returnFail("进行消费前,请先关闭空调或者确认空调是否在线", ErrorCode.RESP_ERROR);
		}
		
		if (EmptyUtils.isEmpty(dbActiveType)) {
			return DtoUtil.returnFail("该促销活动已下架", ErrorCode.RESP_ERROR);
		}
		
		order.setTelephone(dbAppUser.getTelephone());
	
		BigDecimal theoprice = rechargeType.getPrice().multiply(new BigDecimal(order.getNum()));
		order.setTheoprice(theoprice);
		BigDecimal realfee ;
		if ("dis".equals(dbActiveType.getType())) {
			realfee =  (rechargeType.getPrice().multiply(new BigDecimal(dbActiveType.getDiscount())).divide(new BigDecimal(100),2)).multiply(new BigDecimal(order.getNum()));
		}else {
			realfee = dbActiveType.getYearPrice().multiply(new BigDecimal(order.getNum()));
		}
		
		if (EmptyUtils.isNotEmpty(order.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			if ("yes".equals(voucher.getStatus())) {
				return DtoUtil.returnFail("代金券已使用", ErrorCode.RESP_ERROR);
			}
			// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
			//int c = a.compareTo(b); // 结果 C = 1
			if (1==(voucher.getMinimum()).compareTo(realfee)) {
				return DtoUtil.returnFail("代金券不满足条件", ErrorCode.RESP_ERROR);
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"vou".equals(voucher.getType())) {
				realfee=realfee.subtract(voucher.getFacevalue());
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"dis".equals(voucher.getType())) {
				realfee=realfee.multiply(new BigDecimal(voucher.getDiscountRatio())).divide(new BigDecimal(100),2);
			}
		}
		
		order.setRealfee(realfee);
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaystatus("no");
		Boolean boolOrder = orderMapper.insertOrder(order);
		
		if (!boolOrder) {
			return DtoUtil.returnFail("添加订单失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加订单成功", order);
	}
	
//	public BigDecimal getPrice(ActiveType dbActiveType,RechargeType rechargeType,Order order) {
//		BigDecimal realfee;
//		if ("dis".equals(dbActiveType.getType())) {
//			return (rechargeType.getPrice().multiply(new BigDecimal(dbActiveType.getDiscount())).divide(new BigDecimal(100),2)).multiply(new BigDecimal(order.getNum()));
//		}
//		if ("year".equals(rechargeType.getTypeName())) {
//			return  dbActiveType.getYearPrice().multiply(new BigDecimal(order.getNum()));
//		}else {
//			return  dbActiveType.getHourPrice().multiply(new BigDecimal(order.getNum()));
//		}
//	}

	@Override
	public Dto addOrderForYear(Order order) {
		RechargeType rechargeType = userSectionMapper.selectRechargeTypeById(order.getRechargeId());
		AppUser dbAppUser = appUserMapper.selectById(order.getAppusersId());
		order.setTelephone(dbAppUser.getTelephone());
		
		AirCondition airCondition = airConditionMapper.selectAirConditionById(order.getAirmac());
		
		if (EmptyUtils.isEmpty(airCondition)) {
			return DtoUtil.returnFail("该空调已不存在", ErrorCode.RESP_ERROR);
		}
		
		if (!(0==airCondition.getUseStatus())) {
			return DtoUtil.returnFail("进行消费前,请先关闭空调或确认空调是否在线", ErrorCode.RESP_ERROR);
		}
		
		BigDecimal theoprice =rechargeType.getPrice().multiply(new BigDecimal(order.getNum()));
		order.setTheoprice(theoprice);

		BigDecimal realfee = theoprice;//实际所付价格
		if (EmptyUtils.isNotEmpty(order.getVoucherId())) {
			Vouchers voucher= orderMapper.selectVoucherById(order.getVoucherId());
			if ("yes".equals(voucher.getStatus())) {
				return DtoUtil.returnFail("代金券已使用", ErrorCode.RESP_ERROR);
			}
			// 结果 : 1 表示 大于; 0 表示 等于; -1 表示 小于 .
			//int c = a.compareTo(b); // 结果 C = 1
			if (1==(voucher.getMinimum()).compareTo(theoprice)) {
				return DtoUtil.returnFail("代金券不满足条件", ErrorCode.RESP_ERROR);
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"vou".equals(voucher.getType())) {
				realfee=realfee.subtract(voucher.getFacevalue());
			}
			if (EmptyUtils.isNotEmpty(voucher)&&"dis".equals(voucher.getType())) {
				realfee=realfee.multiply(new BigDecimal(voucher.getDiscountRatio())).divide(new BigDecimal(100),2);
			}
		}
		order.setRealfee(realfee);
		order.setOrderno(GenerateNum.getInstance().GenerateOrder());
		order.setPaystatus("no");
		Boolean boolOrder = orderMapper.insertOrder(order);
		
		if (!boolOrder) {
			return DtoUtil.returnFail("添加订单失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加订单成功", order);
	}

	@Override
	public Dto queryCostOrder(Integer appUserId) {
		Order order = orderMapper.selectCostOrder(appUserId);
		OrderVo orderVo = new OrderVo();
		if (EmptyUtils.isNotEmpty(order)) {
			order.setIsRead(true);
			orderMapper.updateOrderById(order);
			BeanUtils.copyProperties(order, orderVo);
			AirCondition condition = airConditionMapper.selectAirConditionById(order.getAirmac());
			orderVo.setPrice(condition.getHour());
			return DtoUtil.returnDataSuccess(orderVo);
		}else {
			return DtoUtil.returnSuccess();
		}
		
//		AirCondition condition = airConditionMapper.selectAirConditionById(order.getAirmac());
//		orderVo.setPrice(condition.getHour());
		
	}

}
