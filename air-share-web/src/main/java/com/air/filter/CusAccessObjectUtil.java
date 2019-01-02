package com.air.filter;

import javax.servlet.http.HttpServletRequest;

public class CusAccessObjectUtil {

	/** 
	   * ��ȡ�û���ʵIP��ַ����ʹ��request.getRemoteAddr();��ԭ�����п����û�ʹ���˴��������ʽ������ʵIP��ַ, 
	   * 
	   * ���ǣ����ͨ���˶༶�������Ļ���X-Forwarded-For��ֵ����ֹһ��������һ��IPֵ�������ĸ������������û��˵���ʵIP�أ� 
	   * ����ȡX-Forwarded-For�е�һ����unknown����ЧIP�ַ����� 
	   * 
	   * �磺X-Forwarded-For��192.168.1.110, 192.168.1.120, 192.168.1.130, 
	   * 192.168.1.100 
	   * 
	   * �û���ʵIPΪ�� 192.168.1.110 
	   * 
	   * @param request 
	   * @return 
	   */ 
	  public static String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  } 
}
