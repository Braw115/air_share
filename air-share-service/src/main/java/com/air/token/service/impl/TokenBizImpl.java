package com.air.token.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.air.constant.MD5;
import com.air.constant.UserAgentUtil;
import com.air.exception.TokenValidationFailedException;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CrmUser;
import com.air.redis.RedisAPI;
import com.air.token.service.TokenBiz;
import com.alibaba.fastjson.JSON;

import cz.mallat.uasparser.UserAgentInfo;
@Service
public class TokenBizImpl implements TokenBiz{

	private Logger logger = Logger.getLogger(TokenBizImpl.class);
	
	/**
	 * 调用RedisAPI
	 */
	@Resource
	private RedisAPI redisAPI;	
	public void setRedisAPI(RedisAPI redisAPI) {
		this.redisAPI = redisAPI;
	}
	
	private int expire = SESSION_TIMEOUT;// 2h
	private String tokenPrefix = "token:";//统一加入 token前缀标识
	
	public String generateToken(String agent, AppUser user) {
		try {
			UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
					agent);
			StringBuilder sb = new StringBuilder();
			sb.append(tokenPrefix);//统一前缀
			if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
				if (UserAgentUtil.CheckAgent(agent)) {	//判断是否为移动设备
					sb.append("MOBILE-");	//移动设备
				} else {
					sb.append("PC-");	//PC
				}
			} else if (userAgentInfo.getDeviceType()
					.equals("Personal computer")) {
				sb.append("PC-");
			} else
				sb.append("MOBILE-");
//			sb.append(user.getUserCode() + "-");	//未加密用户名称
			/*sb.append(MD5.getMd5(user.getUserCode(),32) + "-");	//加密用户名称
*/			sb.append(user.getAppusersId() + "-");
			sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
					+ "-");
			sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码
			sb.append("-9");
			
			return sb.toString();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void save(String token, AppUser user) {
		if (token.startsWith(tokenPrefix+"PC-"))
			redisAPI.set(token, expire, JSON.toJSONString(user));
		else
			redisAPI.set(token, JSON.toJSONString(user));// 手机认证信息永不失效
	}
	public AppUser loadApp(String token) {
		return JSON.parseObject(redisAPI.get(token), AppUser.class);	
	}
	public void delete(String token) {
		if (redisAPI.exist(token))
			redisAPI.delete(token);
	}
	
	private boolean exists(String token) {
		return redisAPI.exist(token);
	}
	
	public String replaceTokenApp(String agent, String token) throws TokenValidationFailedException {
		// 验证旧token是否有效
		if (!exists(token)) {// token不存在
			throw new TokenValidationFailedException("未知的token或 token已过期");// 终止置换
		}
		Date TokenGenTime;// token生成时间
		try {
			String[] tokenDetails = token.split("-");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			TokenGenTime = formatter.parse(tokenDetails[3]);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e);
			throw new TokenValidationFailedException("token格式错误:" + token);
		}

		long passed = Calendar.getInstance().getTimeInMillis()
				- TokenGenTime.getTime();// token已产生时间
		if (passed < REPLACEMENT_PROTECTION_TIMEOUT * 1000) {// 置换保护期内
			throw new TokenValidationFailedException("token处于置换保护期内，剩余"
					+ (REPLACEMENT_PROTECTION_TIMEOUT * 1000 - passed) / 1000
					+ "(s),禁止置换");
		}
		// 置换token
		String newToken = "";
		AppUser user = this.loadApp(token);
		long ttl = redisAPI.ttl(token);// token有效期（剩余秒数 ）
		if (ttl > 0 || ttl == -1) {// 兼容手机与PC端的token在有效期
			newToken = this.generateToken(agent, user);
			this.save(newToken, user);// 缓存新token
			redisAPI.set(token, this.REPLACEMENT_DELAY,
					JSON.toJSONString(user));// 2分钟后旧token过期，注意手机端由永久有效变为2分钟（REPLACEMENT_DELAY默认值）后失效
		} else {// 其它未考虑情况，不予置换
			throw new TokenValidationFailedException("当前token的过期时间异常,禁止置换");
		}
		return newToken;
	}
	
	public boolean validate(String agent, String token) {
		if (!exists(token)) {// token不存在
			return false;
		}
		try {
			Date TokenGenTime;// token生成时间
			String agentMD5;
			String[] tokenDetails = token.split("-");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			TokenGenTime = formatter.parse(tokenDetails[2]);				//原版本：TokenGenTime = formatter.parse(tokenDetails[3]);
			long passed = Calendar.getInstance().getTimeInMillis()
					- TokenGenTime.getTime();
			if(passed>this.SESSION_TIMEOUT*1000)
					return false;
			agentMD5 = tokenDetails[3];						//	原版本：agentMD5 = tokenDetails[3];
			if(MD5.getMd5(agent, 6).equals(agentMD5))
				return true;
		} catch (ParseException e) {
			return false;
		}
		return false;
	}
	@Override
	public String generateToken(String agent, CrmUser crmuser) {
		try {
			UserAgentInfo userAgentInfo = UserAgentUtil.getUasParser().parse(
					agent);
			StringBuilder sb = new StringBuilder();
			sb.append(tokenPrefix);//统一前缀
			if (userAgentInfo.getDeviceType().equals(UserAgentInfo.UNKNOWN)) {
				if (UserAgentUtil.CheckAgent(agent)) {	//判断是否为移动设备
					sb.append("MOBILE-");	//移动设备
				} else {
					sb.append("PC-");	//PC
				}
			} else if (userAgentInfo.getDeviceType()
					.equals("Personal computer")) {
				sb.append("PC-");
			} else
				sb.append("MOBILE-");
			sb.append(crmuser.getCrmuserId() + "-");
			sb.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
					+ "-");
			sb.append(MD5.getMd5(agent, 6));// 识别客户端的简化实现——6位MD5码
			sb.append("-9");
			
			return sb.toString();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void save(String token, CrmUser crmuser) {
		if (token.startsWith(tokenPrefix+"PC-"))
			redisAPI.set(token, expire, JSON.toJSONString(crmuser));
		else
			redisAPI.set(token, JSON.toJSONString(crmuser));// 手机认证信息永不失效
		
	}

	@Override
	public CrmUser loadCrm(String token) {
		return JSON.parseObject(redisAPI.get(token), CrmUser.class);	
	}
	@Override
	public String replaceTokenCrm(String agent, String token) throws TokenValidationFailedException {
		// 验证旧token是否有效
				if (!exists(token)) {// token不存在
					throw new TokenValidationFailedException("未知的token或 token已过期");// 终止置换
				}
				Date TokenGenTime;// token生成时间
				try {
					String[] tokenDetails = token.split("-");
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
					TokenGenTime = formatter.parse(tokenDetails[3]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error(e);
					throw new TokenValidationFailedException("token格式错误:" + token);
				}

				long passed = Calendar.getInstance().getTimeInMillis()
						- TokenGenTime.getTime();// token已产生时间
				if (passed < REPLACEMENT_PROTECTION_TIMEOUT * 1000) {// 置换保护期内
					throw new TokenValidationFailedException("token处于置换保护期内，剩余"
							+ (REPLACEMENT_PROTECTION_TIMEOUT * 1000 - passed) / 1000
							+ "(s),禁止置换");
				}
				// 置换token
				String newToken = "";
				CrmUser user = this.loadCrm(token);
				long ttl = redisAPI.ttl(token);// token有效期（剩余秒数 ）
				if (ttl > 0 || ttl == -1) {// 兼容手机与PC端的token在有效期
					newToken = this.generateToken(agent, user);
					this.save(newToken, user);// 缓存新token
					redisAPI.set(token, this.REPLACEMENT_DELAY,
							JSON.toJSONString(user));// 2分钟后旧token过期，注意手机端由永久有效变为2分钟（REPLACEMENT_DELAY默认值）后失效
				} else {// 其它未考虑情况，不予置换
					throw new TokenValidationFailedException("当前token的过期时间异常,禁止置换");
				}
				return newToken;
	}
	
	
}
