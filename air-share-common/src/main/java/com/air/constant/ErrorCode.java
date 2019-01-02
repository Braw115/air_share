package com.air.constant;

public class ErrorCode {

	/*��֤ģ�������-start*/
	public final static String AUTH_UNKNOWN="30000";
	public final static String AUTH_USER_ALREADY_EXISTS="30001";//�û��Ѵ���
	public final static String AUTH_AUTHENTICATION_FAILED="30002";//��֤ʧ��
	public final static String AUTH_PARAMETER_ERROR="30003";//�û�������������Ϊ��
	public final static String AUTH_ACTIVATE_FAILED="30004";//�ʼ�ע�ᣬ����ʧ��
	public final static String AUTH_REPLACEMENT_FAILED="30005";//�û�tokenʧ��
	public final static String AUTH_TOKEN_INVALID="30006";//token��Ч
	public static final String AUTH_ILLEGAL_USERCODE = "30007";//�Ƿ����û���
	
	
	
	/*��ɾ�Ĳ�ģ�������*/  
	public final static String PEOPLE_EXCEPTION ="50000"; 
	public final static String PEOPLE_FAIL ="50001";  //ʧ��״̬��
	public final static String PEOPLE_NULL ="50002";  //����Ϊ��״̬��
	public final static String PEOPLE_LISTNULL = "5003";
	
	
	/*MQTT�ϱ��쳣״̬*/
	public static final String  MQTT_SUCCESS ="ok";
	public static final String  MQTT_ERROR = "err";
	public static final String  MQTT_SERVER = "Server"; 
	
	public static final String  RESP_SUCCESS ="ok";
	public static final String  RESP_ERROR = "err";
	
	public static final Integer  ONE_HOUR = 3600;
}
