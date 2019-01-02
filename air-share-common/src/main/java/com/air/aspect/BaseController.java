//package com.air.aspect;
//import org.apache.log4j.Logger;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.air.utils.Dto;
//import com.air.utils.DtoUtil;
//
//@ControllerAdvice
//public class BaseController {
//    
//	/** Log4j��־����(@author: rico) */
//	private static final Logger log = Logger.getLogger(ExceptionAspect.class);
//    
//	@ExceptionHandler(value = CustomException.class)
//	@ResponseBody
//	public Dto  handler(CustomException ex) {
//		log.error("ERROR........"+ex);
//		return DtoUtil.returnFail(ex.getMessage(),"60005");
//	}
//	
//	/*@ExceptionHandler(value = Exception.class)
//	@ResponseBody
//	public Dto handle(Exception ex) {     
//		log.error("ERROR........"+ex);
//        return DtoUtil.returnFail(ex.getMessage(),"80005");
//    }*/
//	
//}
