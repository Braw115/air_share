package com.air.user.service;

import java.util.List;

import com.air.constant.Dto;
import com.air.pojo.entity.Order;
import com.air.pojo.vo.OrderVo;

public interface OrderService {

	Dto addOrder(Order order);
	
	Dto addOrderForValue(Order order);

	Dto queryVoucherByAppuserId(Integer appUserId);

	Dto modifyOrderInfo(Order order);

	Dto modifyOrderByBalance(Order order);

	/**
	 * 删除订单
	 * @param orderIdList
	 * @return
	 */
	Dto delOrder(List<Integer> orderIdList);

	/**
	 * 可以分页查询订单
	 * 也可根据订单属性查询
	 * @param orderVo
	 * @return
	 */
	Dto queryOrder(OrderVo orderVo);

	/**
	 * @param orderVo
	 * @return
	 */
	Order queryOrderByNo(String out_trade_no);

	/**
	 * 分页
	 * @param orderVo
	 * @return
	 */
	Dto queryOrderByPage(OrderVo orderVo);

	/**
	 * 根据促销活动生成订单
	 * @param order
	 * @return
	 */
	Dto addActiveOrder(Order order);

	/**
	 * 包年订单，充值到空调里
	 * @param order
	 * @param req
	 * @return
	 */
	Dto addOrderForYear(Order order);

	/**
	 * 查询未被读取的订单并且修改读取状态
	 * @param appUserId
	 * @return
	 */
	Dto queryCostOrder(Integer appUserId);

}
