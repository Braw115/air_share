package com.air.filter;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.air.aspect.record;
import com.air.constant.DtoUtil;
import com.air.constant.TokenUtil;

import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Logger;
import com.air.redis.RedisAPI;
import com.air.token.service.TokenBiz;
import com.air.user.mapper.LoggerMapper;

import com.alibaba.fastjson.JSON;

public class LogInteceptor extends HandlerInterceptorAdapter {
    
//	@Resource
//	private JournalService  journalService;
	@Resource 
	private RedisAPI redisAPI;
	@Resource
	private LoggerMapper loggerMapper;
	
	private String tokenPrefix = "token:";//缁熶竴閿熸枻鎷烽敓鏂ゆ嫹 token鍓嶇紑閿熸枻鎷疯瘑
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object) 棰勯敓鏂ゆ嫹閿熸枻鎷�, 閿熸枻鎷烽敓鍙揪鎷烽敓鏂ゆ嫹閿熷彨锟�, 閿熸枻鎷峰叏閿熸枻鎷烽敓鐙＄鎷�.
	 */

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String id = request.getSession().getId();
		System.out.println("====================sessionId:"+id+"===========");
		System.out.println("token验证");
		String url = request.getRequestURI();
		// 登录,上传图片,获取验证码 等接口 跳过token验证
		if (url.indexOf("doLogin") >= 0 || url.indexOf("upload") >= 0 || url.indexOf("upload") >= 0
				|| url.indexOf("test") >= 0|| url.indexOf("sendMsg") >= 0|| url.indexOf("loginByMsg") >= 0|| url.indexOf("getAuthInfo") >= 0
				|| url.indexOf("loginByZFBcode") >= 0|| url.indexOf("loginbycode") >= 0|| url.indexOf("loginByZFBPhone") >= 0
				|| url.indexOf("loginbyphone") >= 0|| url.indexOf("getInfoBytype") >= 0|| url.indexOf("ZMRZCheck") >= 0
				|| url.indexOf("getAppUrl") >= 0|| url.indexOf("getUrl") >= 0|| url.indexOf("getAppAuthToken") >= 0|| url.indexOf("getPayNotify") >= 0|| url.indexOf("payNotifyUrl") >= 0) {
			return true;
		}
		String token = request.getHeader("token");
		if ("".equals(token)||token==null) {
			PrintWriter out = response.getWriter();
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			String json = JSON.toJSON(DtoUtil.returnFail("token为空", "400")).toString();
			response.setStatus(401);
			out.write(json);
			out.close();
			return false;
		}
		/**
		 * tokenUtil 验证 通过返回true 不通过执行下面代码
		 */
		System.err.println(token);
		boolean flag = TokenUtil.respReturn(token);
		if (flag) {
			String str = redisAPI.get(token);
			redisAPI.set(token, TokenBiz.SESSION_TIMEOUT, str);
			return true;
		}
		
		PrintWriter out = response.getWriter();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String json = JSON.toJSON(DtoUtil.returnFail("校验失败，请重新登陆", "401")).toString();
		response.setStatus(401);
		out.write(json);
		out.close();
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.Object,
	 * org.springframework.web.servlet.ModelAndView) 閿熸枻鎷烽敓鎴揪鎷烽敓鏂ゆ嫹, 閿熸枻鎷烽敓鏂ゆ嫹閿熷彨浼欐嫹閿熸枻鎷烽敓鐫潻鎷稭odelAndView
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerMethod hm = (HandlerMethod) handler;
		record record = hm.getMethodAnnotation(record.class);
		if (record != null) {
			String token = request.getHeader("token");
			CrmUser crmUser = TokenUtil.getCrmUser(token);
			String userName = crmUser.getUsername();
			Integer userId = crmUser.getCrmuserId();
			Logger logger = new Logger();
			logger.setIp(CusAccessObjectUtil.getIpAddress(request));
			logger.setUsername(userName);
			logger.setUserId(userId);
			String businessLogic = record.businessLogic();// 閿熺煫纰夋嫹閿熺殕璁规嫹閿熸枻鎷锋敞閿熸枻鎷烽敓鍙鎷烽敓琛楄鎷峰�� 涓氶敓鏂ゆ嫹閿熺纭锋嫹
			logger.setContent(businessLogic);
			String str = "操作内容:" + businessLogic;
			System.out.println("----------------------" + str);
			System.out.println("访问的ip----------" + CusAccessObjectUtil.getIpAddress(request));
			System.out.println("访问的人:*-------------" + userName);
			try {
				
				loggerMapper.addLogger(logger);
			}catch (Exception e) {
				System.out.println("*******异常:"+e);
			}
			
			// 鎵ч敓鏂ゆ嫹閿熸枻鎷峰織閿熸枻鎷峰綍
			/*Journal journal = new  Journal(); 
			journal.setIp(CusAccessObjectUtil.getIpAddress(request));
			journal.setCreate_time(DateFormats.getDateFormat());
			journal.setUserName(userName);
			journal.setAction(actionType);
			journal.setDescribe(businessLogic);
			saveLog(journal);*/
		}
		/*System.out.println("请求先进入此方法.......");
		HandlerMethod hm = (HandlerMethod) handler;// 閿熸枻鎷烽敓鏂ゆ嫹寮鸿浆閿熸枻鎷烽敓鏂ゆ嫹
		record record = hm.getMethodAnnotation(record.class);// 閿熺煫纰夋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯繙閿熸枻鎷烽敓闃额澁鎷烽敓鏂ゆ嫹閿熸枻鎷�//閫氶敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
		if (record != null) {
			String userName =request.getHeader("username");
			String username = (String) request.getSession().getAttribute("username");
			String actionType = record.actionType();// 閿熺煫纰夋嫹閿熺殕璁规嫹閿熸枻鎷锋敞閿熸枻鎷烽敓鍙鎷烽敓琛楄鎷峰�� 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
			String businessLogic = record.businessLogic();// 閿熺煫纰夋嫹閿熺殕璁规嫹閿熸枻鎷锋敞閿熸枻鎷烽敓鍙鎷烽敓琛楄鎷峰�� 涓氶敓鏂ゆ嫹閿熺纭锋嫹
			String str = "操作类型:" + actionType + "  操作内容:" + businessLogic;
			System.out.println("----------------------" + str);
			System.out.println("访问的ip----------" + CusAccessObjectUtil.getIpAddress(request)); 
			System.out.println("访问的人:*-------------" + userName);
			// 鎵ч敓鏂ゆ嫹閿熸枻鎷峰織閿熸枻鎷峰綍
			Journal journal = new  Journal(); 
			journal.setIp(CusAccessObjectUtil.getIpAddress(request));
			journal.setCreate_time(DateFormats.getDateFormat());
			journal.setUserName(userName);
			journal.setAction(actionType);
			journal.setDescribe(businessLogic);
			saveLog(journal);
		}*/
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 
	 * <p>Title: saveLog</p>
	 * <p>Description: 保存日志</p>
	 * @param journal
	 */
	/*private void saveLog(Journal journal) {
		try {
			System.out.println(journal);
			// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�, 閿熸枻鎷烽敓鏂や紶閿熸枻鎷烽敓鏂ゆ嫹蹇楅敓鏂ゆ嫹閿熸枻鎷烽敓鎹峰尅鎷�, 閿熸枻鎷烽敓鏂ゆ嫹澶遍敓鏂ゆ嫹閿熸枻鎷穋atch閿熸枻鎷烽敓鏂ゆ嫹閿熻緝浣庣鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓琛楋拷
			if(journalService.addJournal(journal)) {
				System.out.println("==========================閿熸枻鎷峰織閿熸枻鎷峰綍閿熺即鐧告嫹!");
				return;
			}
			System.out.println("==========================閿熸枻鎷峰織閿熸枻鎷峰綍澶遍敓鏂ゆ嫹!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("=================="+e.getMessage());
		}
		return ;
	}
	
	private boolean exists(String token) {
		return redisAPI.exist(token);
	}*/

}
