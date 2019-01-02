package com.air.base.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.air.pojo.vo.RespVo;
import com.alibaba.fastjson.JSON;


public class BaseController {
	private static final Log log = LogFactory.getLog(BaseController.class);
	@Autowired
	protected Properties properties;

	@Context
	protected HttpServletRequest httpServletRequest;

	@Context
	protected HttpServletResponse httpServletResponse;

	protected void outResponse(Object object) {
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("application/json; charset=utf-8");
		OutputStream responseout = null;
		try {
			responseout = httpServletResponse.getOutputStream();
			String result = JSON.toJSONString(object);
			log.info("response is: " + result);
			responseout.write(result.getBytes("UTF-8"));
		} catch (Exception e) {
			log.info("系统繁忙", e);
		} finally {
			if (responseout != null) {
				try {
					responseout.flush();
					responseout.close();
				} catch (IOException e) {
					log.error("关闭流失败");
				}
			}
		}

	}
	
	
	protected RespVo okRespVo(){
		RespVo resp = new RespVo(true, "ok", null);
		return resp;
	}
	
	protected RespVo errRespVo(String msg){
		RespVo resp = new RespVo(false, msg, null);
		return resp;
	}
}
