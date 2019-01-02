//package com.air.aspect;
//import org.apache.log4j.Logger;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import com.air.utils.Dto;
//import com.air.utils.DtoUtil;
//
//
///**
// * Title: ȫ���쳣�������� Description: ���� @ControllerAdvice + @ExceptionHandler
// * ��ϴ���Controller��RuntimeException�쳣
// * 
// * @author rico
// * @created 2017��7��4�� ����4:29:07
// */
//@ControllerAdvice
//public class ExceptionAspect {
//
//	/** Log4j��־����(@author: rico) */
//	private static final Logger log = Logger.getLogger(ExceptionAspect.class);
//    
//	/**
//	 * 405 - Method Not Allowed��HttpRequestMethodNotSupportedException
//	 * ��ServletException������,��ҪServlet API֧��
//	 */
//	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//	@ResponseBody
//	public Dto handleHttpRequestMethodNotSupportedException(
//			HttpRequestMethodNotSupportedException e) {
//		log.error("request_method_not_supported...", e);
//		return DtoUtil.returnFail("request_method_not_supported","405");
//	}
//	
//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	@ResponseBody
//	public JsonResponse handleHttpMessageNotReadableException(
//			HttpMessageNotReadableException e) {
//		log.error("could_not_read_json...", e);
//		return new JsonResponse().failure("could_not_read_json");
//	}
//	
//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler({MethodArgumentNotValidException.class})
//	public JsonResponse handleValidationException(MethodArgumentNotValidException e) {
//		log.error("parameter_validation_exception...", e);
//		return new JsonResponse().failure("parameter_validation_exception");
//	}
//	
//	/**
//	 * 415 - Unsupported Media Type��HttpMediaTypeNotSupportedException
//	 * ��ServletException������,��ҪServlet API֧��
//	 */
//	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
//	public JsonResponse handleHttpMediaTypeNotSupportedException(Exception e) {
//		log.error("content_type_not_supported...", e);
//		return new JsonResponse().failure("content_type_not_supported");
//	}
//
//	/**
//	 * 500 - Internal Server Error
//	 */
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(TokenException.class)
//	public JsonResponse handleTokenException(Exception e) {
//		log.error("Token is invaild...", e);
//		return new JsonResponse().failure("Token is invaild");
//	}
//
//	/**
//	 * 500 - Internal Server Error
//	 */
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(Exception.class)
//	public JsonResponse handleException(Exception e) {
//		log.error("Internal Server Error...", e);
//		return new JsonResponse().failure("Internal Server Error");
//	}
//	
//
//}
