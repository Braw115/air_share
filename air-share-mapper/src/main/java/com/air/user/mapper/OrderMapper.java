package com.air.user.mapper;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.Order;
import com.air.pojo.entity.Vouchers;
import com.air.pojo.vo.OrderVo;

public interface OrderMapper {

	/**
	 * 根据某些字段来查询
	 * 用户 phone 支付状态 支付方式 订单类别（消费，充值）
	 * 用户id
	 * @param orderVo
	 * @return
	 */
	List<OrderVo> selectOrderVo(OrderVo orderVo);

	List<Vouchers> selectVoucherByAppuserId(Vouchers voucher);

	Boolean updateOrderById(Order order);

	Boolean updateVoucherById(Vouchers voucher);

	Vouchers selectVoucherById(@Param("voucherId")Integer voucherId);

	Boolean insertOrder(Order order);

	Boolean deleteOrder(@Param("orderIdList")List<Integer> orderIdList);

	List<Order> selectUserOrderByPaytype(Order order);

	List<Order> OrderByTelephone(@Param("telephone")String telephone);

	/**
	 * 订单号查询订单
	 * @param orderVo
	 * @return
	 */
	Order selectOrder(OrderVo orderVo);

	/**
	 * 查询充值订单
	 * @param orderVo
	 * @return
	 */
	List<OrderVo> selectValueOrderVo(OrderVo orderVo);

	/**
	 * 查询未被读取的订单
	 * @param appUserId
	 * @return
	 */
	Order selectCostOrder(Integer appUserId);

}
