package com.air.condition.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.air.constant.Dto;
import com.air.pojo.vo.ControlVO;

public interface ConditionMqttService {
	
	/**
	 * 
	 * <p>Title: rebootAirCondition</p>
	 * <p>Description: 设备重启</p>
	 * @param mac
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public void rebootAirCondition(String mac) throws MqttPersistenceException, MqttException;
	
	/**
	 * 
	 * <p>Title: controlAirCondition</p>
	 * <p>Description: 空调的控制</p>
	 * @param controlVO
	 */
	public Dto controlAirCondition(ControlVO controlVO, Integer appuserId) throws MqttPersistenceException, MqttException;
	
	/**
	 * 
	 * <p>Title: crmControlAirCondition</p>
	 * <p>Description: 后台空调的控制</p>
	 * @param controlVO
	 * @return
	 */
	public String crmControlAirCondition(ControlVO controlVO);
	
	/**
	 * 读取二进制文件分段传输
	 * @return
	 * @throws MqttPersistenceException
	 * @throws MqttException
	 */
	public byte[] readFileInBytesToString() throws MqttPersistenceException, MqttException;
}
