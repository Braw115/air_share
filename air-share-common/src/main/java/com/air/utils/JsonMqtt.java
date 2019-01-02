package com.air.utils;


public class JsonMqtt {
    
	private static String success ="ok";
    private static String error ="err";
    private static String Server = "Server";
   
    private Object obj;
    
    public static mqtt success(Object obj) {
    	mqtt  mq = new mqtt();
    	mq.setObj(obj);
    	return mq;
    }

	public static mqtt success() {
		mqtt  mq = new mqtt();
		mq.setMessage(success);
		return mq;
	}

	public static mqtt failure() {
		mqtt  mq = new mqtt();
		mq.setMessage(error);
		return mq;
	}
	
	public static mqtt server() {
		mqtt  mq = new mqtt();
		mq.setMessage(Server);
		return mq;
	}
	
	public static String getSuccess() {
		return success;
	}

	public static void setSuccess(String success) {
		JsonMqtt.success = success;
	}
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
