package com.air.constant;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisAPI {

	public JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * set key and value to redis
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, String value) {
		try {
			Jedis jedis = jedisPool.getResource();
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * set key and value to redis
	 * 
	 * @param key
	 * @param seconds
	 *            ��Ч��
	 * @param value
	 * @return
	 */
	public boolean set(String key, int seconds, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key, seconds, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedisPool, jedis);
		}
		return false;
	}

	/**
	 * �ж�ĳ��key�Ƿ����
	 * 
	 * @param key
	 * @return
	 */
	public boolean exist(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedisPool, jedis);
		}
		return false;
	}

	/**
	 * ���������ӳ�
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
			System.out.println("redis��ȡ���ݣ�" + value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ���������ӳ�
			returnResource(jedisPool, jedis);
		}

		return value;
	}

	/**
	 * ��ѯkey����Ч��,�� key ������ʱ������ -2 �� �� key ���ڵ�û������ʣ������ʱ��ʱ������ -1 �� ��������Ϊ��λ������ key
	 * ��ʣ������ʱ�䡣 ע�⣺�� Redis 2.8 ��ǰ���� key �����ڣ����� key û������ʣ������ʱ��ʱ��������� -1 ��
	 * 
	 * @param key
	 * @return ʣ�������
	 */
	public Long ttl(String key) {
		try {
			Jedis jedis = jedisPool.getResource();
			return jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (long) -2;
	}

	/**
	 * ɾ��
	 * 
	 * @param key
	 */
	public void delete(String key) {
		try {
			Jedis jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Long lpush(String key,String[] values) {
		Jedis jedis = jedisPool.getResource();
		Long size  = jedis .lpush(key, values);
		return size;
	}
	
}
