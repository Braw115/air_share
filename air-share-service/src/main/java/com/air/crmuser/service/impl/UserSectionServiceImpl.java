package com.air.crmuser.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.aspectj.apache.bcel.generic.NEW;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.crmuser.mapper.UserSectionMapper;
import com.air.crmuser.service.UserSectionService;
import com.air.pojo.entity.ActiveType;
import com.air.pojo.entity.AppUserInfo;
import com.air.pojo.entity.Order;
import com.air.pojo.entity.PromotionInfo;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.vo.ActiveTypeVo;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.air.pojo.vo.OrderVo;
import com.air.user.mapper.OrderMapper;
import com.air.utils.EmptyUtils;
import com.air.utils.FileUploader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class UserSectionServiceImpl implements UserSectionService {

	@Resource
	private UserSectionMapper userSectionMapper;
	
	@Resource
	private OrderMapper orderMapper;

	private ArrayList<RechargeType> arrayList;
	
	@Override
	public Dto addActiveType(ActiveTypeVo activeTypeVo) {
 		String img = FileUploader.upload(activeTypeVo.getPicture()).getMsg();
 		String imgDetail = FileUploader.upload(activeTypeVo.getImgDetail()).getMsg();
 		String imgTurn = FileUploader.upload(activeTypeVo.getImgTurn()).getMsg();
		ActiveType activeType = new ActiveType();
		try {
			BeanUtilsBean.getInstance().getConvertUtils()
			.register(new SqlDateConverter(null), Date.class);
			BeanUtils.copyProperties(activeType, activeTypeVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.RESP_ERROR);
		}
		activeType.setImg(img);
		activeType.setImgDetail(imgDetail);
		activeType.setImgTurn(imgTurn);
		Boolean bool = userSectionMapper.insertActiveType(activeType);
		if (!bool) {
			return DtoUtil.returnFail("添加失败", ErrorCode.RESP_ERROR);
		}
		
		return DtoUtil.returnSuccess("添加成功",ErrorCode.RESP_SUCCESS);
	}

//	@Override
//	public PageInfo<ActiveType> queryActiveType(Integer curPage, Integer pageSize) {
//		PageHelper.startPage(curPage, pageSize);
//		List<ActiveType> activeTypeList = userSectionMapper.selectActiveType(new ActiveType());
//		PageInfo<ActiveType> activeType = new PageInfo<ActiveType>(activeTypeList);
//		return activeType;
//	}

//	@Override
//	public PageInfo<ActiveType> queryActiveTypeByType(Integer curPage, Integer pageSize,Boolean shelves) {
//		ActiveType active = new ActiveType();
//		active.setShelves(shelves);
//		PageHelper.startPage(curPage, pageSize);
//		List<ActiveType> activeTypeList = userSectionMapper.selectActiveType(active);
//		PageInfo<ActiveType> activeType = new PageInfo<ActiveType>(activeTypeList);
//		return activeType;
//	}

	@Override
	public Dto modifyActiveType(ActiveTypeVo activeTypeVo) {
		String img = null;
 		String imgDetail = null;
 		String imgTurn = null;
		if(activeTypeVo.getPicture()!=null) {
			img = FileUploader.upload(activeTypeVo.getPicture()).getMsg();
		}	
		if(activeTypeVo.getImgDetail()!=null) {
			imgDetail = FileUploader.upload(activeTypeVo.getImgDetail()).getMsg();
		}
		if(activeTypeVo.getImgTurn()!=null) {
			imgTurn = FileUploader.upload(activeTypeVo.getImgTurn()).getMsg();
		}
		ActiveType activeType = new ActiveType();
		activeType.setImg(img);
		try {
			BeanUtilsBean.getInstance().getConvertUtils()
			.register(new SqlDateConverter(null), Date.class);
			BeanUtils.copyProperties(activeType, activeTypeVo);
			activeType.setImgDetail(imgDetail);
			activeType.setImgTurn(imgTurn);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.RESP_ERROR);
		}
		Boolean bool = userSectionMapper.updateActiveType(activeType);
		if (!bool) {
			return DtoUtil.returnFail("修改促销活动失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("修改促销活动信息成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto modifyActiveTypePic(ActiveTypeVo activeTypeVo) {
		String img = FileUploader.upload(activeTypeVo.getPicture()).getMsg();
 		String imgDetail = FileUploader.upload(activeTypeVo.getImgDetail()).getMsg();
 		String imgTurn = FileUploader.upload(activeTypeVo.getImgTurn()).getMsg();
		ActiveType activeType = new ActiveType();
		activeType.setImg(img);
		try {
			BeanUtils.copyProperties(activeType, activeTypeVo);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return DtoUtil.returnFail(e.getMessage(), ErrorCode.RESP_ERROR);
		}
		
		activeType.setImgDetail(imgDetail);
		activeType.setImgTurn(imgTurn);
		Boolean bool = userSectionMapper.updateActiveType(activeType);
		if (!bool) {
			return DtoUtil.returnFail("修改图片失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto delActiveType(List<Integer> activeIdList) {
		Boolean bool = userSectionMapper.deleteActiveType(activeIdList);
		if (!bool) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功");
	}

	@Override
	public Dto modifyActiveTypeShelves(List<Integer> activeIdList, Boolean shelves) {
		Boolean bool = userSectionMapper.updateActiveTypeShelves(activeIdList,shelves);
		if (!bool) {
			return DtoUtil.returnFail("失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("成功");
	}

	@Override
	public Dto queryAppUserInfo(Integer curPage, Integer pageSize) {
		AppUserInfo appUserInfo = new AppUserInfo();
		PageHelper.startPage(curPage, pageSize);
		List<AppUserInfo> infoList = userSectionMapper.selectAppUserInfo(appUserInfo);
		PageInfo<AppUserInfo> info = new PageInfo<>(infoList);
		if (EmptyUtils.isEmpty(info)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", info);
	}

	@Override
	public Dto queryAppUserPerInfoById(Integer appusersId) {
		AppUserInfo appUserInfo = new AppUserInfo();
		appUserInfo.setAppusersId(appusersId);
		List<AppUserInfo> infoList = userSectionMapper.selectAppUserInfo(appUserInfo);
		if (EmptyUtils.isEmpty(infoList)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", infoList);
	}

	@Override
	public Dto queryAppUserPerInfo(Integer curPage, Integer pageSize, String nickname) {
		AppUserInfo appUserInfo = new AppUserInfo();
		appUserInfo.setNickname(nickname);
		PageHelper.startPage(curPage, pageSize);
		List<AppUserInfo> infoList = userSectionMapper.selectAppUserInfo(appUserInfo);
		PageInfo<AppUserInfo> info = new PageInfo<>(infoList);
		if (EmptyUtils.isEmpty(info)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", info);
	}

	/**
	 * 所有
	 * 促销统计 ：促销设备名称、促销时间、金额使用折算金额、使用代金券金额、使用折扣券金额
	 */
	@Override
	public Dto queryAllPromotionStatistics(Integer curPage, Integer pageSize) {
		PageHelper.startPage(curPage, pageSize);
		List<PromotionInfo> infoList = userSectionMapper.selectAllPromotionStatistics();
		PageInfo<PromotionInfo> info = new PageInfo<>(infoList);
		return DtoUtil.returnDataSuccess(info);
	}

	/**
	 * name 模糊查询
	 * 促销统计 ：促销设备名称、促销时间、金额使用折算金额、使用代金券金额、使用折扣券金额
	 */
	@Override
	public Dto queryPromotionStatistics(Integer curPage, Integer pageSize, String name) {
		PromotionInfo pro = new PromotionInfo();
		pro.setName(name);
		PageHelper.startPage(curPage, pageSize);
		List<PromotionInfo> infoList = userSectionMapper.selectPromotionStatistic(pro);
		PageInfo<PromotionInfo> info = new PageInfo<>(infoList);
		if (EmptyUtils.isEmpty(info)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", info);
	}

	/**
	 * activeId id查询
	 * 促销统计 ：促销设备名称、促销时间、金额使用折算金额、使用代金券金额、使用折扣券金额
	 */
	@Override
	public Dto queryPromotionStatisticsById(Integer activeId) {
		PromotionInfo pro = new PromotionInfo();
		pro.setActiveId(activeId);
		List<PromotionInfo> infoList = userSectionMapper.selectPromotionStatistic(pro);
		if (EmptyUtils.isEmpty(infoList)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", infoList);
	}

	@Override
	public Dto queryActiveTypeById(Integer activeId) {
		ActiveType activeType = userSectionMapper.selectActiveTypeById(activeId);
		if (EmptyUtils.isEmpty(activeType)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", activeType);
	}

	@Override
	public Dto queryRecharge(String airmac) {
		List<RechargeType>  dbRec = userSectionMapper.selectRechargeTypeByMAC(airmac);
		if (dbRec.isEmpty()) {
			return DtoUtil.returnFail("后台未添加数据请联系客服", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", dbRec);
	}

	@Override
	public Dto queryActiveTypeByMAC(ActiveTypeVo activeTypeVo) {
		List<ActiveType> byAirMAC = userSectionMapper.selectActiveTypeByAirMAC(activeTypeVo);
		if (byAirMAC.isEmpty()) {
			return DtoUtil.returnFail("没有促销活动", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", byAirMAC.get(0));
	}

	@Override
	public Dto queryActiveTypeByPage(ActiveTypeVo activeTypeVo) {
		PageHelper.startPage(activeTypeVo.getCurPage(), activeTypeVo.getPageSize());
		List<ActiveType> infoList = userSectionMapper.selectActiveType(activeTypeVo);
		PageInfo<ActiveType> info = new PageInfo<>(infoList);
		if (EmptyUtils.isEmpty(info)) {
			return DtoUtil.returnFail("无数据", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", info);
	}

	@Override
	public Dto queryActiveType(ActiveTypeVo activeTypeVo) {
		List<ActiveType> infoList = userSectionMapper.selectActiveType(activeTypeVo);
		int size = infoList.size();
		if (size<2) {
			return DtoUtil.returnDataSuccess(infoList); 
		}
		Random random = new Random();
		int index = random.nextInt(size-1);
		return DtoUtil.returnDataSuccess(infoList.get(index));
	}

	@Override
	public Dto queryRecharge() {
		RechargeType  rechargeType = userSectionMapper.selectRechargeType();
		if (EmptyUtils.isEmpty(rechargeType)) {
			return DtoUtil.returnFail("后台未添加数据请联系客服", ErrorCode.RESP_ERROR);
		}
		List<RechargeType> list = new ArrayList<>();
		list.add(rechargeType);
		return DtoUtil.returnDataSuccess(list);
	}

	@Override
	public Dto modifyRechargeType(RechargeType rechargeType) {
		Boolean bool = userSectionMapper.updateRechargeType(rechargeType);
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok");
	}

	@Override
	public Dto queryActiveOrNot(Integer id, Integer appusersId) {
		OrderVo orderVo = new OrderVo();
		orderVo.setActiveId(id);
		orderVo.setAppusersId(appusersId);
		List<OrderVo> dbList = orderMapper.selectOrderVo(orderVo);
		if (dbList.isEmpty()) {
			ActiveType dbActiceType = userSectionMapper.selectActiveTypeById(id);
			return DtoUtil.returnDataSuccess(dbActiceType);
		}	
		return DtoUtil.returnDataSuccess(dbList.get(0));
	}

	@Override
	public List<BoxAndVoucherVo> queryActive(String nowTime) {
		
		return userSectionMapper.selectActives(nowTime);
	}

//	@Override
//	public Dto queryRechargeByActive(Integer activeId) {
//		ActiveType dbActiveType = userSectionMapper.selectActiveTypeById(activeId);
//		List<RechargeType> dbList = userSectionMapper.selectRechargeType();
//		
//		if ("dis".equals(dbActiveType.getType())) {
//			for (RechargeType rechargeType : dbList) {
//				rechargeType.setPrice(rechargeType.getPrice().multiply(new BigDecimal(dbActiveType.getDiscount()).divide(new BigDecimal(100))));
//			}
//			return DtoUtil.returnDataSuccess(dbList);
//		}
//		for (RechargeType rechargeType : dbList) {
//			if ("year".equals(rechargeType.getTypeName())) {
//				rechargeType.setPrice(dbActiveType.getYearPrice());
//			}
////			if ("hour".equals(rechargeType.getTypeName())) {
////				rechargeType.setPrice(dbActiveType.getHourPrice());
////			}
//		}
//		return DtoUtil.returnDataSuccess(dbList);
//	}
	
	@Override
	public Dto queryRechargeByActive(Integer activeId) {
		ActiveType dbActiveType = userSectionMapper.selectActiveTypeById(activeId);
		RechargeType rechargeType = userSectionMapper.selectRechargeType();
		
		List<RechargeType> list = new ArrayList<RechargeType>();
		if ("dis".equals(dbActiveType.getType())) {
			rechargeType.setPrice(rechargeType.getPrice().multiply(new BigDecimal(dbActiveType.getDiscount()).divide(new BigDecimal(100),2, RoundingMode.HALF_UP)));
			list.add(rechargeType);
			return DtoUtil.returnDataSuccess(list);

		}
		rechargeType.setPrice(dbActiveType.getYearPrice());
		
		
		list.add(rechargeType);
		
		return DtoUtil.returnDataSuccess(list);
	}

}
