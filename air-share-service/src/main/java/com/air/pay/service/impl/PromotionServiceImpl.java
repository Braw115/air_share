package com.air.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils.Null;
import org.python.antlr.PythonParser.return_stmt_return;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.aircondition.mapper.CarbonIndicatorsMapper;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.pay.service.PromotionService;
import com.air.pay.service.RedBoxUtil;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CarbonLevel;
import com.air.pojo.entity.RedBoxActive;
import com.air.pojo.entity.RedBoxNote;
import com.air.pojo.entity.RedBoxRate;
import com.air.pojo.entity.VoucherActive;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.BoxAndVoucherVo;
import com.air.pojo.vo.RedBoxActiveVo;
import com.air.pojo.vo.VoucherActiveVo;
import com.air.pojo.vo.VoucherVo;
import com.air.user.mapper.AppUserMapper;
import com.air.user.mapper.PromotionMapper;
import com.air.utils.DateFormats;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Resource
	private AppUserMapper appUserMapper;
	
	@Resource
	private PromotionMapper promotionMapper;

	@Resource
	private CarbonIndicatorsMapper carbonMapper;
	
	@Override
	public Dto addRedBoxActive(RedBoxActive redBoxActive) {
		Boolean bool = promotionMapper.insertRedBoxActive(redBoxActive);
		if (!bool) {
			return DtoUtil.returnFail("添加红包活动失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",redBoxActive);
	}

	@Override
	public Dto modifyRedBoxActiveStatus(List<Integer> redBoxActiveIdlist,Boolean status) {
		Boolean bool = promotionMapper.updateRedBoxActiveStatus(redBoxActiveIdlist,status);
		if (!bool) {
			return DtoUtil.returnFail("红包活动发布失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("红包活动发布成功");
	}

	@Override
	public Boolean  modifyRedBoxActive(RedBoxActive redBoxActive) {
		return promotionMapper.updateRedBoxActive(redBoxActive);
	}

	@Override
	public Dto queryRedBoxActiveByStatus(Integer curPage, Integer pageSize,String status) {
		PageHelper.startPage(curPage, pageSize);
		List<RedBoxActive> redBoxActiveList = promotionMapper.selectRedBoxActiveByStatus(status);
		if (EmptyUtils.isEmpty(redBoxActiveList)) {
			return DtoUtil.returnFail("无"+("yes".equals(status)?"已发布":"未发布")+"红包活动", ErrorCode.RESP_ERROR);
		}
		PageInfo<RedBoxActive> list = new PageInfo<RedBoxActive>(redBoxActiveList);
		return DtoUtil.returnSuccess("ok",list);
	}

	@Override
	public Dto queryAllRedBoxActive(Integer curPage, Integer pageSize, String name) {
		PageHelper.startPage(curPage, pageSize);
		List<RedBoxActiveVo> redBoxActiveList = promotionMapper.selectAllRedBoxActive(name);
		if (redBoxActiveList.isEmpty()) {
			return DtoUtil.returnFail("没有活动", ErrorCode.RESP_ERROR);
		}
		RedBoxRate rate = new RedBoxRate();
		for (RedBoxActiveVo redBoxActiveVo : redBoxActiveList) {
			rate.setRedBoxActiveId(redBoxActiveVo.getRedBoxActiveId());
			List<RedBoxRate> rateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(rate);
			redBoxActiveVo.setRateList(rateList);
		}
		PageInfo<RedBoxActiveVo> list = new PageInfo<RedBoxActiveVo>(redBoxActiveList);
		
		return DtoUtil.returnSuccess("ok",list);
	}

	@Override
	public Dto queryRedBoxActiveById(Integer redBoxActiveId) {
		RedBoxActive redBoxActive = promotionMapper.selectRedBoxActiveById(redBoxActiveId);
		if (EmptyUtils.isEmpty(redBoxActive)) {
			return DtoUtil.returnFail("无红包活动", ErrorCode.RESP_ERROR);
		}
		
		return DtoUtil.returnSuccess("ok",redBoxActive);
	}

	@Override
	public Dto queryRedBoxActiveByName(Integer curPage, Integer pageSize, String name) {
		PageHelper.startPage(curPage, pageSize);
		List<RedBoxActiveVo> redBoxActiveList = promotionMapper.selectRedBoxActiveByName(name);
		if (EmptyUtils.isEmpty(redBoxActiveList)) {
			return DtoUtil.returnFail("没有与此名称相关红包活动", ErrorCode.RESP_ERROR);
		}
		RedBoxRate rate = new RedBoxRate();
		for (RedBoxActiveVo redBoxActiveVo : redBoxActiveList) {
			rate.setRedBoxActiveId(redBoxActiveVo.getRedBoxActiveId());
			List<RedBoxRate> rateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(rate);
			redBoxActiveVo.setRateList(rateList);
		}
		PageInfo<RedBoxActiveVo> list = new PageInfo<RedBoxActiveVo>(redBoxActiveList);
		
		return DtoUtil.returnSuccess("ok",list);
	}

	@Override
	public Dto getRedBoxActiveByName(RedBoxNote redBoxNote) {
		Boolean bool = promotionMapper.insertRedBoxNote(redBoxNote);
		if (!bool) {
			return DtoUtil.returnFail("添加领取红包记录信息失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加领取红包记录信息成功");
	}

	@Override
	public Dto addVoucherActive(VoucherActive voucherActive) {
		Boolean bool = promotionMapper.insertVoucherActive(voucherActive);
		if (!bool) {
			return DtoUtil.returnFail("添加红包活动失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",voucherActive);
	}
	
	@Override
	public Dto modifyVoucherActiveStatus(List<Integer> voucherActiveIdlist,Boolean status) {
		Boolean bool = promotionMapper.updateVoucherActiveStatus(voucherActiveIdlist,status);
		if (!bool) {
			return DtoUtil.returnFail("优惠券活动发布失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("优惠券活动发布成功");
	}

	@Override
	public Dto modifyVoucherActive(VoucherActive voucherActive) {
		Boolean bool = promotionMapper.updateVoucherActive(voucherActive);
		if (!bool) {
			return DtoUtil.returnFail("失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("成功");
	}

	@Override
	public Dto queryVoucherActiveByPage(VoucherActiveVo voucherActiveVo) {
		PageHelper.startPage(voucherActiveVo.getCurPage(), voucherActiveVo.getPageSize());
		List<VoucherActive> voucherActiveList = promotionMapper.selectVoucherActiveByVo(voucherActiveVo);
		for(VoucherActive voucherActive:voucherActiveList) {
			voucherActive.setBeginTime(voucherActive.getBeginTime().replace(".0",""));
			voucherActive.setEndTime(voucherActive.getEndTime().replace(".0",""));
		}
		PageInfo<VoucherActive> list = new PageInfo<VoucherActive>(voucherActiveList);
		
		return DtoUtil.returnSuccess("ok",list);
	}

	@Override
	public Dto addRedBoxRates(List<RedBoxRate> redBoxRates) {
		Boolean bool = promotionMapper.insertRedBoxRates(redBoxRates);
		if (!bool) {
			return DtoUtil.returnFail("添加红包活动获奖区间和比例信息失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("成功");
	}

	@Override
	public Dto modifyRedBoxRates(List<RedBoxRate> redBoxRates) {
		Boolean bool = promotionMapper.updateRedBoxRate(redBoxRates);
		if (!bool) {
			return DtoUtil.returnFail("修改红包活动获奖区间和比例信息失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("修改红包活动获奖区间和比例信息成功");
	}

	@Override
	public Dto queryRedBoxRateByRedBoxActiveId(Integer redBoxActiveId) {
		RedBoxRate redBoxRate = new RedBoxRate();
		redBoxRate.setRedBoxActiveId(redBoxActiveId);
		List<RedBoxRate> rateBoxRateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(redBoxRate);
		if (EmptyUtils.isEmpty(rateBoxRateList)) {
			return DtoUtil.returnFail("查红包活动获奖区间和比例信息失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("查红包活动获奖区间和比例信息成功",rateBoxRateList);
	}

	@Override
	public Dto getRedBox(Integer appusersId,Integer redBoxActiveId) {
		RedBoxNote redBoxNote = new RedBoxNote();
		redBoxNote.setAppusersId(appusersId);
		redBoxNote.setRedBoxActiveId(redBoxActiveId);
		
		RedBoxRate redBoxRate = new RedBoxRate();
		redBoxRate.setRedBoxActiveId(redBoxActiveId);
		List<RedBoxRate> rateBoxRateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(redBoxRate);
		if (EmptyUtils.isEmpty(rateBoxRateList)) {
			return DtoUtil.returnFail("查红包活动还没有设置区间和比例信息", ErrorCode.RESP_ERROR);
		}
		RedBoxNote rateBoxNote= promotionMapper.selectRedBoxNoteByRedBoxNote(redBoxNote);
		if (EmptyUtils.isNotEmpty(rateBoxNote)) {
			return DtoUtil.returnFail("你已经获取过该活动的红包", ErrorCode.RESP_ERROR);
		}
		
		BigDecimal value = RedBoxUtil.getValue(rateBoxRateList);
		if (EmptyUtils.isEmpty(value)) {
			return DtoUtil.returnFail("获取红包失败", ErrorCode.RESP_ERROR);
		}
		RedBoxActive dbRedBoxActive = promotionMapper.selectRedBoxActiveById(redBoxActiveId);
		BigDecimal sendTotalValue = promotionMapper.selectSendTotalValue(redBoxActiveId);
		//获取红包后与之前发放统计的红包总金额相加与活动总金额比较
		BigDecimal nowValue = dbRedBoxActive.getTotalValue().subtract(sendTotalValue);
		if (nowValue.intValue()<0) {
			return DtoUtil.returnFail("领取红包失败", ErrorCode.RESP_ERROR);
		}
		if (nowValue.intValue()<=value.intValue()) {
			value = nowValue;
		}
		AppUser updateUser = appUserMapper.selectById(appusersId);
		
		updateUser.setRedBoxValue(updateUser.getRedBoxValue().add(value));
		Boolean bool = appUserMapper.updateAppUser(updateUser);
		if (!bool) {
			return DtoUtil.returnFail("修改用户红包失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", value);
	}

	@Override
	public Dto addVoucherForUser(Vouchers voucher) {
		
		List<Vouchers> dbList = promotionMapper.selectVoucher(voucher);
		if (!dbList.isEmpty()) {
			return DtoUtil.returnFail("您已获得该活动发放的优惠券", ErrorCode.RESP_ERROR);
		}
		Integer totalCount = promotionMapper.selectVouchersTotalCount(voucher.getVoucherActiveId());
		VoucherActiveVo voucherActiveVo = new VoucherActiveVo();
		voucherActiveVo.setVoucherActiveId(voucher.getVoucherActiveId());
		VoucherActive dbVoucherActive = promotionMapper.selectVoucherActiveByVo(voucherActiveVo).get(0);
		if (totalCount>=dbVoucherActive.getTotalCount()) {
			return DtoUtil.returnFail("获取优惠券失败", ErrorCode.RESP_ERROR);
		}
		String type = voucher.getType();//优惠券类型
		voucher.setStatus("no");
		voucher.setMinimum(dbVoucherActive.getVoucherMinLevel());
		voucher.setValidTime(dbVoucherActive.getValidTime());
		if ("vou".equals(type)) {
			voucher.setFacevalue(dbVoucherActive.getFaceValue());
		}else if ("dis".equals(type)) {
			voucher.setDiscountRatio(dbVoucherActive.getDiscount());
		}
		Boolean bool =promotionMapper.insertVouchers(voucher);
		if (!bool) {
			return DtoUtil.returnFail("获取优惠券失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("获取优惠券成功");
	}

	@Override
	public Dto queryRedBoxByCarbon(Integer appUserId, String activeName) {
		AppUser dbAppUser = appUserMapper.selectById(appUserId);
		CarbonLevel carbonLevel = carbonMapper.selectLevelByCarbon(dbAppUser.getCarbon());
	
		if (EmptyUtils.isEmpty(carbonLevel)||carbonLevel.getCarbonLevel()<3) {
			return DtoUtil.returnFail("没有达到领取红包的条件", ErrorCode.RESP_ERROR);
		}
		String str = DateFormats.getDateFormat();
		System.out.println(str);
		List<RedBoxActive> dbRedBoxActive = promotionMapper.selectRedBoxActiveByNameOk(activeName,str);
		if (dbRedBoxActive.isEmpty()) {
			return DtoUtil.returnFail("没有红包活动", ErrorCode.RESP_ERROR);
		}
		Integer redBoxActiveId = dbRedBoxActive.get(0).getRedBoxActiveId();
		RedBoxRate Rote = new RedBoxRate();
		RedBoxNote note = new RedBoxNote();
		note.setAppusersId(appUserId);
		note.setRedBoxActiveId(redBoxActiveId);
		Rote.setRedBoxActiveId(redBoxActiveId);
		RedBoxNote dbNote = promotionMapper.selectRedBoxNoteByRedBoxNote(note);
		if (EmptyUtils.isNotEmpty(dbNote)) {
			return DtoUtil.returnFail("您已经领取此次活动的红包", ErrorCode.RESP_ERROR);
		}
		List<RedBoxRate> dbRateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(Rote);
		if (dbRateList.isEmpty()) {
			return DtoUtil.returnFail("红包活动没有设置领取概率", ErrorCode.RESP_ERROR);
		}
		BigDecimal value = RedBoxUtil.getValue(dbRateList);
		BigDecimal sendTotalValue = promotionMapper.selectSendTotalValue(redBoxActiveId);
		if (EmptyUtils.isEmpty(sendTotalValue)) {
			sendTotalValue =new BigDecimal(0);
		}
		System.out.println(sendTotalValue);
		//获取红包后与之前发放统计的红包总金额相加与活动总金额比较
		BigDecimal nowValue = dbRedBoxActive.get(0).getTotalValue().subtract(sendTotalValue);
		if (nowValue.intValue()<0) {
			return DtoUtil.returnFail("领取红包失败", ErrorCode.RESP_ERROR);
		}
		if (nowValue.intValue()<=value.intValue()) {
			value = nowValue;
		}
		AppUser updateUser = appUserMapper.selectById(appUserId);
		
		updateUser.setRedBoxValue(updateUser.getRedBoxValue().add(value));
		Boolean bool = appUserMapper.updateAppUser(updateUser);
		note.setValue(value);
		promotionMapper.insertRedBoxNote(note);
		if (!bool) {
			return DtoUtil.returnFail("修改用户红包失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", value);
	}

	@Override
	public Dto queryVoucherActive(VoucherActiveVo voucherActiveVo) {
		List<VoucherActive> voucherActive = promotionMapper.selectVoucherActiveByVo(voucherActiveVo);
		return DtoUtil.returnSuccess("ok", voucherActive);
	}

//	@Override
//	public Dto queryVoucherActives(String time) {
//		List<VoucherActive> voucherActive = promotionMapper.selectVoucherActive(time);
//		if (voucherActive.isEmpty()) {
//			return DtoUtil.returnFail("没有活动", ErrorCode.RESP_ERROR);
//		}
//		return DtoUtil.returnSuccess("ok", voucherActive.get(0));
//	}

	@Override
	public Boolean addRedBoxActive(RedBoxActiveVo redBoxActiveVo) {
		return promotionMapper.insertRedBoxActive(redBoxActiveVo);
	}

	@Override
	public Boolean delRedBoxRates(Integer redBoxActiveId) {
		
		return promotionMapper.deleteRedBoxRates(redBoxActiveId);
	}

	@Override
	public Dto queryRedBoxActive(RedBoxActiveVo redBoxActiveVo) {
		
		List<RedBoxActiveVo> redBoxActiveVoList = promotionMapper.selectRedBoxActive(redBoxActiveVo);
		RedBoxRate rate = new RedBoxRate();
		for (RedBoxActiveVo redBoxActivevo : redBoxActiveVoList) {
			rate.setRedBoxActiveId(redBoxActiveVo.getRedBoxActiveId());
			List<RedBoxRate> rateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(rate);
			redBoxActivevo.setRateList(rateList);
		}
		return DtoUtil.returnSuccess("ok", redBoxActiveVoList);
	}

	@Override
	public Dto queryRedBoxActiveByPage(RedBoxActiveVo redBoxActiveVo) {
		PageHelper.startPage(redBoxActiveVo.getCurPage(), redBoxActiveVo.getPageSize());
		List<RedBoxActiveVo> redBoxActiveVoList = promotionMapper.selectRedBoxActive(redBoxActiveVo);
		RedBoxRate rate = new RedBoxRate();
		for (RedBoxActiveVo redBoxActive : redBoxActiveVoList) {
			rate.setRedBoxActiveId(redBoxActive.getRedBoxActiveId());
			redBoxActive.setBeginTime(redBoxActive.getBeginTime().replace(".0",""));
			redBoxActive.setEndTime(redBoxActive.getEndTime().replace(".0",""));
			List<RedBoxRate> rateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(rate);
			redBoxActive.setRateList(rateList);
		}
		PageInfo<RedBoxActiveVo> list = new PageInfo<RedBoxActiveVo>(redBoxActiveVoList);
		
		
		return DtoUtil.returnSuccess("ok",list);
	}

	@Override
	public Dto delRedBoxActive(Integer redBoxActiveId) {
		Boolean bool = promotionMapper.deleteRedBoxActive(redBoxActiveId);
		Boolean boolRate = promotionMapper.deleteRedBoxRates(redBoxActiveId);
		if (!bool) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		if (!boolRate) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功");
	}

	@Override
	public Dto delVoucherActive(Integer voucherActiveId) {
		Boolean bool = promotionMapper.deleteVoucherActive(voucherActiveId);
		if (!bool) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功");
	}

	@Override
	public Dto queryVouchers(Integer appUserId) {
		Vouchers voucher = new Vouchers();
		voucher.setAppusersId(appUserId);
		
		List<Vouchers> voucherList = promotionMapper.selectVoucher(voucher);
		
		if (voucherList.isEmpty()) {
			return DtoUtil.returnDataSuccess(voucherList);
		}
		
		List<Vouchers> vouOutTimeList = promotionMapper.selectVoucherTimeout(appUserId);
		
		if (vouOutTimeList.isEmpty()) {
			List<VoucherVo> respList = getRespVouchers(voucherList);
			return DtoUtil.returnDataSuccess(respList);
		}
		
		List<Integer> idList = new ArrayList<>();
		for (Vouchers vouchers : vouOutTimeList) {
			vouchers.setStatus("out");
			idList.add(vouchers.getVoucherId());
		}
		Boolean bool = promotionMapper.updateVoucherStatus("out",idList);
		
		voucherList = promotionMapper.selectVoucher(voucher);
		
		List<VoucherVo> respList = getRespVouchers(voucherList);
		
		return DtoUtil.returnSuccess("ok",respList);
	}

	@Override
	public List<BoxAndVoucherVo> queryRedBoxs(String nowTime) {
		return promotionMapper.selectRedBoxs(nowTime);
	}

	@Override
	public List<BoxAndVoucherVo> queryVoucherActive(String nowTime) {
		return promotionMapper.selectVoucherActives(nowTime);
	}

	@Override
	public List<BoxAndVoucherVo> queryRedBoxs(String nowTime, Integer appusersId) {
		return promotionMapper.selectRedBox(nowTime,appusersId);
	}

	@Override
	public List<BoxAndVoucherVo> queryVoucherActive(String nowTime, Integer appusersId) {
		return promotionMapper.selectVoucherActive(nowTime,appusersId);
	}

	@Override
	public Dto addVoucherForAppUser(Integer appusersId,BoxAndVoucherVo boxAndVoucherVo) {
		Vouchers voucher = new Vouchers();
		voucher.setVoucherActiveId(boxAndVoucherVo.getId());;
		voucher.setAppusersId(appusersId);
		List<Vouchers> dbVoucherList = promotionMapper.selectVoucher(voucher);
		if (!dbVoucherList.isEmpty()) {
			return DtoUtil.returnFail("已领取", ErrorCode.RESP_ERROR, dbVoucherList.get(0));
		}
		
		Integer totalCount = promotionMapper.selectVouchersTotalCount(boxAndVoucherVo.getId());
		
		VoucherActiveVo voucherActiveVo = new VoucherActiveVo();
		voucherActiveVo.setVoucherActiveId(boxAndVoucherVo.getId());
		VoucherActive voucherActive = promotionMapper.selectVoucherActiveByVo(voucherActiveVo).get(0);
		
		if (totalCount >= voucherActive.getTotalCount()) {
			return DtoUtil.returnFail("优惠券已被全部领取", ErrorCode.RESP_ERROR);
		}
		
		Vouchers newVoucher = new Vouchers();
		newVoucher.setAppusersId(appusersId);
		newVoucher.setVoucherActiveId(boxAndVoucherVo.getId());
		newVoucher.setFacevalue(voucherActive.getFaceValue());
		newVoucher.setStatus("no");
		newVoucher.setValidTime(voucherActive.getValidTime());
		newVoucher.setType("vou");
		newVoucher.setMinimum(voucherActive.getVoucherMinLevel());
		
		Boolean bool = promotionMapper.insertVouchers(newVoucher);
		
		
		if (!bool) {
			return DtoUtil.returnFail("获取优惠券失败", ErrorCode.RESP_ERROR);
		}
		List<Vouchers> list = promotionMapper.selectVoucher(newVoucher);
		VoucherVo vo = new VoucherVo();
		BeanUtils.copyProperties(list.get(0), vo);
		vo.setBeginTime(DateFormats.getDateFormat(list.get(0).getCreated()));
		vo.setEndTime(DateFormats.addDate(list.get(0).getCreated(), list.get(0).getValidTime()));
		return DtoUtil.returnSuccess("获取优惠券成功",vo);
	}

	@Override
	public Dto addRedBoxForAppUser(Integer appusersId,BoxAndVoucherVo boxAndVoucherVo) {
		
		RedBoxActive dbRedBoxActive = promotionMapper.selectRedBoxActiveById(boxAndVoucherVo.getId());
		
		RedBoxRate rote = new RedBoxRate();
		RedBoxNote note = new RedBoxNote();
		
		note.setAppusersId(appusersId);
		note.setRedBoxActiveId(boxAndVoucherVo.getId());
		rote.setRedBoxActiveId(boxAndVoucherVo.getId());
		
		List<RedBoxRate> dbRateList = promotionMapper.selectRedBoxRateByRedBoxActiveId(rote);
		if (dbRateList.isEmpty()) {
			return DtoUtil.returnFail("红包活动没有设置领取概率", ErrorCode.RESP_ERROR);
		}
		
		RedBoxNote dbNote = promotionMapper.selectRedBoxNoteByRedBoxNote(note);
		if (EmptyUtils.isNotEmpty(dbNote)) {
			return DtoUtil.returnFail("已领取", ErrorCode.RESP_ERROR,dbNote);
		}
		
		BigDecimal value = RedBoxUtil.getValue(dbRateList);
		BigDecimal sendTotalValue = promotionMapper.selectSendTotalValue(boxAndVoucherVo.getId());
		
		if (EmptyUtils.isEmpty(sendTotalValue)) {
			sendTotalValue =new BigDecimal(0);
		}
		System.out.println(sendTotalValue);
		//获取红包后与之前发放统计的红包总金额相加与活动总金额比较
		BigDecimal nowValue = dbRedBoxActive.getTotalValue().subtract(sendTotalValue);
		if (nowValue.intValue()<0) {
			return DtoUtil.returnFail("领取红包失败", ErrorCode.RESP_ERROR);
		}
		if (nowValue.intValue()<=value.intValue()) {
			value = nowValue;
		}
		AppUser updateUser = appUserMapper.selectById(appusersId);
		if (EmptyUtils.isEmpty(updateUser.getRedBoxValue())) {
			updateUser.setRedBoxValue(new BigDecimal(0));
		}
		updateUser.setRedBoxValue(updateUser.getRedBoxValue().add(value));
		Boolean bool = appUserMapper.updateAppUser(updateUser);
		note.setValue(value);
		Boolean boolNote = promotionMapper.insertRedBoxNote(note);
		if (!bool) {
			return DtoUtil.returnFail("获取红包失败", ErrorCode.RESP_ERROR);
		}
		if (!boolNote) {
			return DtoUtil.returnFail("获取红包失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok", value);
	}

	@Override
	public Dto queryVoucherActiveOrNot(Integer id, Integer appusersId) {
		Vouchers voucher = new Vouchers();
		voucher.setAppusersId(appusersId);
		voucher.setVoucherActiveId(id);
		List<Vouchers> dbList = promotionMapper.selectVoucher(voucher);
		if (dbList.isEmpty()) {
			return DtoUtil.returnDataSuccess(null);
		}
		VoucherVo vo = new VoucherVo();
		BeanUtils.copyProperties(dbList.get(0), vo);
		vo.setBeginTime(DateFormats.getDateFormat(dbList.get(0).getCreated()));
		vo.setEndTime(DateFormats.addDate(dbList.get(0).getCreated(), dbList.get(0).getValidTime()));
		return DtoUtil.returnDataSuccess(vo);
	}

	@Override
	public Dto queryRedBoxActiveOrNot(Integer id, Integer appusersId) {
		
		RedBoxNote redBoxNote = new RedBoxNote();
		redBoxNote.setAppusersId(appusersId);
		redBoxNote.setRedBoxActiveId(id);
		RedBoxNote dbNote = promotionMapper.selectRedBoxNoteByRedBoxNote(redBoxNote );
		return DtoUtil.returnDataSuccess(dbNote);
	}

	@Override
	public Dto queryCarbonRedBox(Integer appUserId, String activeName) {
		List<RedBoxActive> dbCarbonList = promotionMapper.selectRedBoxActiveByNameOk(activeName,DateFormats.getDateFormat());
		if (dbCarbonList.isEmpty()) {
			return DtoUtil.returnDataSuccess(false);
		}
		/** 用户是否领取过 */
		RedBoxNote redBoxNote = new RedBoxNote();
		redBoxNote.setAppusersId(appUserId);
		redBoxNote.setRedBoxActiveId(dbCarbonList.get(0).getRedBoxActiveId());
		RedBoxNote dbNote = promotionMapper.selectRedBoxNoteByRedBoxNote(redBoxNote);
		if (EmptyUtils.isEmpty(dbNote)) {
			return DtoUtil.returnDataSuccess(true);
		}
		return DtoUtil.returnDataSuccess(false);
	}


	public List<VoucherVo> getRespVouchers(List<Vouchers> voucherList){
		List<VoucherVo> respList = new ArrayList<>();
		for (Vouchers vouchers : voucherList) {
			VoucherVo vo = new VoucherVo();
			BeanUtils.copyProperties(vouchers, vo);
			vo.setBeginTime(DateFormats.getDateFormat(vouchers.getCreated()));
			vo.setEndTime(DateFormats.addDate(vouchers.getCreated(), vouchers.getValidTime()));
			respList.add(vo);
		}
		return respList;
	}

}
