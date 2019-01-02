package com.air.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *<p>Title: record</p>  
 *<p>Description:自定义注解 </p>  
 * @author Administrator
 * @date 2018年3月6日
 */
@Target(ElementType.METHOD)  //表明该注解对成员方法起作用  
@Retention(RetentionPolicy.RUNTIME) //在编译以后仍然起作用
@Documented  //支持JavaDoc文档注释
public @interface record {

	String actionType() default "默认动作类型"; //一般有增加,删除,修改,查询
	
	String businessLogic() default "默认业务逻辑";
}

