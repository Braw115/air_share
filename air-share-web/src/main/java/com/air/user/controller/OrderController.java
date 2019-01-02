package com.air.user.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.python.antlr.PythonParser.return_stmt_return;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.UserSectionService;
import com.air.pojo.entity.Order;
import com.air.pojo.entity.RechargeType;
import com.air.pojo.vo.ActiveTypeVo;
import com.air.pojo.vo.OrderVo;
import com.air.user.service.OrderService;
import com.air.utils.DateFormats;
import com.air.utils.EmptyUtils;

/**
 * 订单
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private UserSectionService userSectionService;
	
//	/**
//	 * 扫码之后生成订单之前查看此空调是否有相关促销活动(有系列区别的促销活动)
//	 * @param token
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value=("/getActiveType"),method=RequestMethod.GET)
//	public Dto getActiveType(HttpServletRequest req, ActiveTypeVo activeTypeVo) {
//		activeTypeVo.setShelves(true);
//		activeTypeVo.setNowTime(DateFormats.getDateFormat());
//		return userSectionService.queryActiveTypeByMAC(activeTypeVo);
//	} 
	/**
	 * 扫码之后生成订单之前查看此空调是否有相关促销活动(无系列区别的促销活动，在统一的原始价格下进行促销)
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getActiveType"),method=RequestMethod.GET)
	public Dto getActiveType(HttpServletRequest req, ActiveTypeVo activeTypeVo) {
		activeTypeVo.setShelves(true);
		activeTypeVo.setNowTime(DateFormats.getDateFormat());
		return userSectionService.queryActiveType(activeTypeVo);
	} 
	
//	/**
//	 * 
//	 * @param req
//	 * @param activeTypeVo
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value=("/getActiveTypeAndRechargeType"),method=RequestMethod.GET)
//	public Dto getActiveTypeAndRechargeType(HttpServletRequest req, ActiveTypeVo activeTypeVo) {
//		
//		return userSectionService.queryActiveTypeByMAC(activeTypeVo);
//	} 
//	public Dto getRechargeType(HttpServletRequest req,String airmac) {
//	return userSectionService.queryRecharge(airmac);
	/**
	 * 查询包年包时价格（包年包时价格统一）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getRechargeType"),method=RequestMethod.GET)
	public Dto getRechargeType(HttpServletRequest req){
		return userSectionService.queryRecharge();
	} 
	
	/**
	 * 查询未使用且未过期的代金券和折扣券信息  
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getVoucherByAppuserId"),method=RequestMethod.GET)
	public Dto getVoucherByAppuserId(HttpServletRequest req) {
		
		return orderService.queryVoucherByAppuserId(TokenUtil.getAppUserId(req.getHeader("token")));
	} 
	
	/**
	 * 添加订单
	 */
	@ResponseBody
	@RequestMapping(value=("/addOrder"),method=RequestMethod.POST)
	public Dto addOrder(@RequestBody Order order,HttpServletRequest req) {
		if (EmptyUtils.isEmpty(order)) {
			return DtoUtil.returnFail("订单参数为空", ErrorCode.RESP_ERROR);
		}
		order.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		order.setIsRead(true);
		
		if (order.getActiveId()!=null) {
			return orderService.addActiveOrder(order);
		}
		
		if ("cost".equals(order.getPaytype())) {
			return orderService.addOrderForYear(order);
		}
		return orderService.addOrderForValue(order);
	} 
	
	/**
	 * crm查询订单
	 * 也可以根据某些字段来查询
	 * 用户 phone 支付状态 支付方式 订单类别（消费，充值）
	 * @param orderVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getOrderCrm"),method=RequestMethod.GET)
	public Dto getOrderCrm(OrderVo orderVo) {
		if (EmptyUtils.isEmpty(orderVo.getCurPage())&&EmptyUtils.isEmpty(orderVo.getPageSize())) {
			return orderService.queryOrder(orderVo);
		}
		return orderService.queryOrderByPage(orderVo);
		
	} 
	
	/**
	 * app端查看订单
	 * 根据用户id和订单类型查询订单
	 * 或根据用户id查询所有订单
	 * @param orderVo
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/getOrderApp"),method=RequestMethod.GET)
	public Dto getOrderApp(OrderVo orderVo,HttpServletRequest req) {
		orderVo.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		if (EmptyUtils.isEmpty(orderVo.getPageSize())||EmptyUtils.isEmpty(orderVo.getCurPage())) {
			return DtoUtil.returnFail("分页参数为空", ErrorCode.RESP_ERROR);
		}
		if ("value".equals(orderVo.getPaytype())) {
			return orderService.queryOrder(orderVo);
		}
		return orderService.queryOrderByPage(orderVo);
	} 
	
	/**
	 * 删除订单
	 */
	@ResponseBody
	@RequestMapping(value=("/delOrder"),method=RequestMethod.DELETE)
	public Dto getOrder(@RequestBody List<Integer> orderIdList ) {
		if (EmptyUtils.isEmpty(orderIdList)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		return orderService.delOrder(orderIdList);
	} 
	
	/**
	 * 余额支付
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("/payOrderByBalance"),method=RequestMethod.POST)
	public Dto payOrderByBalance(@RequestBody Order order) {
		if (EmptyUtils.isEmpty(order.getOrderno())) {
			return DtoUtil.returnFail("订单不存在", ErrorCode.RESP_ERROR);
		}
		return orderService.modifyOrderByBalance(order);
	} 
	
//	@ResponseBody
//	@RequestMapping(value=("/payOrder"),method=RequestMethod.POST)
//	public Dto payOrder() {
//		Map<String, String> preyId = WeiChartUtil.getPreyId("123123123", "1", "hello");
//		Set<String> keySet = preyId.keySet();
//		Iterator<String> iterator = keySet.iterator();
//		while(iterator.hasNext()) {
//			String key = iterator.next();
//			String value = preyId.get(key);
//			System.out.println("key="+key+"value="+value);
//		}
//		return DtoUtil.returnSuccess("data", preyId);
//	} 
	
	/**
	 * 活动详情页跳转到付款页面
	 * 活动价格返回给前端
	 * @param activeTypeVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value=("getPriceForAvtive"),method=RequestMethod.GET)
	public Dto getPriceForAvtive(Integer activeId) {
		if (EmptyUtils.isEmpty(activeId)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		return userSectionService.queryRechargeByActive(activeId); 
	}
	
	/**
	 * 包年订单，充值到空调里
	 * @param order
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="addOrderForYear",method=RequestMethod.POST)
	public Dto addOrderForYear(@RequestBody Order order,HttpServletRequest req) {
		
		if (EmptyUtils.isEmpty(order.getAirmac())||EmptyUtils.isEmpty(order.getNum())) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		if (order.getActiveId()!=null) {
			return orderService.addActiveOrder(order);
		}
		
		order.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		
		return orderService.addOrderForYear(order);
	}
	
	/**
	 * 查询未被读取的订单并且修改读取状态
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getCostOrder",method=RequestMethod.GET)
	public Dto getCostOrder(HttpServletRequest req) {
		
		return orderService.queryCostOrder(TokenUtil.getAppUserId(req.getHeader("token")));
	}
	
}