package com.air.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCreateLog {
	private final static Logger logger = LoggerFactory.getLogger(UserCreateLog.class);
	
	public static void info(String paramString, Object... paramVarArgs){
		logger.info(paramString, paramVarArgs);
	}
}
