package com.air.shiro;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.air.constant.ErrorCode;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;


public class MyExceptionResolver implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		FastJsonJsonView view = new FastJsonJsonView();  
        Map<String, Object> attributes = new HashMap<String, Object>();  
        if (ex instanceof UnauthenticatedException) {  
        	attributes.put("status", 401);
            attributes.put("errorCode", ErrorCode.RESP_ERROR);  
            attributes.put("msg", "当前未登录，请登录后再执行操作");  
        } else if (ex instanceof UnauthorizedException) {  
        	attributes.put("status", 401);
            attributes.put("errorCode", ErrorCode.RESP_ERROR);  
            attributes.put("msg", "用户无权限");  
        } else {  
        	attributes.put("status", 500);
            attributes.put("errorCode",ErrorCode.RESP_ERROR);  
            attributes.put("msg", ex.getMessage());  
        }  
  
        view.setAttributesMap(attributes);  
        mv.setView(view);  
        return mv;  
	}
}
