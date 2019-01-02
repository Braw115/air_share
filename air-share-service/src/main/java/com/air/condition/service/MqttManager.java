package com.air.condition.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Author:huhy
 * @DATE:Created on 2017/12/1 14:29
 * @Modified By:
 * @Class Description:
 */
@Component
@Scope("singleton")
public class MqttManager {

	@Resource
	private MqttCallback mqttPushCallBackService;
	// tcp://MQTT安装的服务器地址:MQTT定义的端口号
	public static final String HOST = "tcp://47.106.211.75:1883";
	// 定义MQTT的ID，可以在MQTT服务配置中指定
	private static final String clientid = "air_share_lxl";
	 //private static final String clientid = "air_share_myVM";
	//private static final String clientid = "air_share_online";

	private static MqttManager server = new MqttManager();
	private static MqttClient client;

	/**
	 * 构造函数
	 * 
	 * @throws MqttException
	 */
	private MqttManager() {
	}

	public static MqttManager getInstance() throws MqttException {
		if (server == null) {
			server = new MqttManager();
		}
		return server;
	}

	public MqttClient getClient() {
		return this.client;
	}

	/**
	 * 用来连接服务器
	 */
	@PostConstruct
	public void initialize() {
		try {
			System.out.println("连接mqtt");
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(true);
			options.setUserName("admin");
			options.setPassword("admin".toCharArray());
			// 设置超时时间
			options.setConnectionTimeout(10);
			// 设置会话心跳时间
			options.setKeepAliveInterval(20);

			client.setCallback(mqttPushCallBackService);
			// SSLSocketFactory factory =
			// SSLUtil.getSSLSocktet("D:/cert/cacert.crt","D:/cert/client-cert.crt","D:/cert/client-key-pkcs8.pem","brt123");
			// options.setSocketFactory(factory);
			client.connect(options);
			 System.out.println("连接成功");
			int[] Qos = { 0 };
			String[] topic1 = { "web/srv" };
			String[] topic2 = { "push/srv" };
			String[] topic3 = { "web/error" };
			client.subscribe(topic1, Qos);
			client.subscribe(topic2, Qos);
			client.subscribe(topic3, Qos);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	/**
	 * 消息推送
	 *
	 * @param topic
	 * @param message
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void publish(String topic, String message, int Qos, boolean retained)
			throws MqttPersistenceException, MqttException {
		MqttTopic topic1 = client.getTopic(topic);
		MqttMessage message1 = new MqttMessage();
		message1.setQos(Qos);
		message1.setRetained(retained);
		message1.setPayload(message.getBytes());
		MqttDeliveryToken token = topic1.publish(message1);

		token.waitForCompletion();
		System.out.println("message is published completely! " + token.isComplete());

	}
}
