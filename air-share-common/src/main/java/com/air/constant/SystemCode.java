package com.air.constant;

import java.util.List;

public class SystemCode {
	/* 技术人员审核相关 */
	/**
	 * 标题
	 */
	public static final String REVIEW_TITLE="技术人员审核信息"; 	
	
	/**
	 * 待处理
	 */
	public static final String REVIEW_SUCCESS="您已经通过技术人员的审核,初始密码为"; 	// 审核通过
	
	/**
	 * 审核未通过
	 */
	public static final String REVIEW_ERROR="您未通过技术人员的审核,请重新认证"; 	// 审核未通过
	
	/* 维修故障相关 */
	/**
	 * 待处理
	 */
	public static final String REPAIR_PENDING="pending"; 	// 待处理
	
	/**
	 * 已指派
	 */
	public static final String REPAIR_ASSIGNED="assigned";	// 已指派
	
	/**
	 * 处理中
	 */
	public static final String REPAIR_PROCESSING="processing"; 	// 处理中
	
	/**
	 * 审批中
	 */
	public static final String REPAIR_CKECKING="checking"; 	// 审批中
	
	/**
	 * 已处理
	 */
	public static final String REPAIR_PROCESSED="processed"; 	// 已处理
	
	/**
	 * 已评价
	 */
	public static final String REPAIR_EVALUATED="evaluated";	// 已评价
	
	/**
	 * 额定功率 1122W P(单位：W)=U(单位：V)I(单位：A) 
	 */
	public static final Integer RATED_POWER = 800; 
	
	/**
	 * 电压更新时间20*60 20分钟
	 */
	public static final Integer VOLTAGE_UPDATE_TIME = 300;
	
	public static final String SUCCESS="ok";  //ReusltJson ����״̬ ok,��ʾ�ɹ�
	
	public static final String ERROR ="err";  //ReusltJson ����״̬ err,��ʾʧ��
	
	 public static final String CURRENT_USER = "user";
	 
	 public static final Integer PAGE_RESULT= 1;
	 
	 public static final String USER_SUCCESS="user";
	 
	 public static final Integer USER_STATUSZERO =0;  //root
	  
	 public static final Integer USER_STATUSONE =1;  //����
	 
	 public static final Integer USER_STATUSTWO =2; //����
	 
	 public static final Integer FILET_INTERCEPTOR=0; //������   
	 
	 public static final List<String>  AGGREAGTELIST = null;  //�ռ���
	 
	 public static final Integer ZERO =0;  //��ʶ0
	 
	 public static final Integer ADD_UPDATE_FAILE = 0;	//修改/添加失败
	 
	 /*MQTT Qos��ʶ��*/
	 public static final Integer MQTT_QOS_ZERO = 0;
	 public static final Integer MQTT_QOS_ONE = 1;
	 public static final Integer MQTT_QOS_TWO = 2;
	 
	 /*MQTT ����*/
	 public static final String MQTT_TOPIC_ONE = "web/srv";  //��������������
	 public static final String MQTT_TOPIC_TWO = "push/srv";  //������������Ϣ����
	 
	 /*�߼��ж�����*/
	 public static final Integer ALL_ZERO =0;
	 
	 /*空调控制参数*/
	 public static final String AIR_CONDITION_ORDER_RESTART = "restart";	//重启
	 public static final String AIR_CONDITION_ORDER_START = "start";	//启动空调
	 public static final String AIR_CONDITION_ORDER_STOP = "stop";	//关闭空调
	 public static final String AIR_CONDITION_ORDER_MODEL = "model";	//模式
	 public static final String AIR_CONDITION_ORDER_WIND_SPEED = "windSpeed";	//风速
	 public static final String AIR_CONDITION_ORDER_SWING = "swing";	//风向
	 public static final String AIR_CONDITION_ORDER_DRY_HOT = "dryHot";	//干燥/辅热
	 public static final String AIR_CONDITION_ORDER_SLEEP = "sleep";	//睡眠
	 public static final String AIR_CONDITION_ORDER_STRONG = "strong";	//强力
	 public static final String AIR_CONDITION_ORDER_TIMING = "timing";	//定时
	 public static final String AIR_CONDITION_ORDER_TEMP = "temp";	//温度
	 
	 /**
	  * 模式: 自感(auto)->制冷(cold)->除湿(deh)->送风(wind)->制热(hot) 0制冷 1制热 2除湿 3送风 4自动
	  */
	 public static final String[] AIR_CONDITION_MODEL = {"cold", "hot", "deh", "wind", "auto"};
	 
	 
	 /**
	  * 模式: 制冷(cold)->除湿(deh)->送风(wind)->制热(hot)->自感(auto)
	  */
	 //public static final String[] AIR_CONDITION_MODEL = {"cold", "deh", "wind","hot","auto"};
	 /**
	  * 风量: 自动(auto)->低速(low)->中速(med)->高速(high)
	  */
	 public static final String[] AIR_CONDITION_WIND_SPEED = {"auto", "low", "med", "high"};//风速
	 /**
	  * 风向:不摇摆(close)->上下摇摆(ud)->左右摇摆(lr)
	  */
	 public static final String[] AIR_CONDITION_SWING = {"close", "ud"};	//风向
	 /**
	  * 干燥/辅热: 干燥(dry)->辅热(hot)
	  */
	 public static final String[] AIR_CONDITION_DRY_HOT = {"dry", "hot"};	//干燥/辅热
	 /**
	  * 睡眠: 关(close)->开(open)
	  */
	 public static final String[] AIR_CONDITION_SLEEP = {"close", "open"}; //睡眠
	 /**
	  * 强力:关(close)->开(open)
	  */
	 public static final String[] AIR_CONDITION_STRONG = {"close", "open"};	//强力
	 /**
	  * 定时:根据空调的开关机状态 定时开机(open)/定时关机(close)
	  */
	 public static final String[] AIR_CONDITION_TIMING = {"close", "open"};	//定时
	 
	 /*派单相关*/
	 public static final String REPAIR_TITLE="派单信息";
	 public static final String REPAIR_CONTENT="收到一个派单信息,请到我的工单查看详情";
	 
	 /*工单审核相关*/
	 public static final String REPAIR_APPROVAL_TITLE="工单审核信息";
	
	 /*解绑相关*/
	 public static final String RELEASE_TITLE="解绑提醒";
	 
	 
	 public static final Float OperationRange = 20f;

	 /**
	  * 固件升级接收数据流成功
	  */
	 public static final int TRANSPORT_SUCCESS = 9999;	//成功
	 
	 /**
	  * 固件升级接收数据流失败
	  */
	 public static final int TRANSPORT_FAIL= 9998;	//失败
	 
	 /**
	  * 固件升级接收数据流大小失败
	  */
//	 public static final int TRANSPORT_SIZE= 9997;	//失败
	 
	 /**
	  * 文件存储位置
	  */
	 public static final String FILEPATH = "C:\\Users\\wzq\\Desktop\\bin\\ceshi\\";
		
	 /**
	  * 读取的大小
	  */
	 public static final int READARRAYSIZEPERREAD = 64;
	 
	 /**/
}
