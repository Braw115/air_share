package com.air.redis;

import java.util.HashSet;
import java.util.Set;

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
	 * modify
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, String value) {
		Jedis jedis =null;
		try {
			 jedis = jedisPool.getResource();
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedisPool, jedis);
		}
		return false;
	}

	/**
	 * set key and value to redis
	 * 
	 * @param key
	 * @param seconds
	 *            有效期
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
	 * 判断某个key是否存在
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
	 * 返还到连接池
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
	 * 获取数据
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
			System.out.println("redis:" + value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 返还到连接池
			returnResource(jedisPool, jedis);
		}

		return value;
	}

	/**
	 * 查询key的有效期,当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key
	 * 的剩余生存时间。 注意：在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1 。
	 * modify--------
	 * @param key
	 * @return 剩余多少秒
	 */
	public Long ttl(String key) {
		Jedis jedis = null;
		try {
			 jedis = jedisPool.getResource();
			return jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedisPool, jedis);
		}
		return (long) -2;
	}

	/**
	 * 删除
	 * modify--------
	 * @param key
	 */
	public void delete(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			returnResource(jedisPool, jedis);
		}
	}
	
	/**
	 * 模糊查询keys
	 * modify--------
	 * @param compare
	 */
	public Set<String> keys(String compare) {
		Jedis jedis = null;
		try {
			 jedis = jedisPool.getResource();
			return jedis.keys(compare);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashSet<String>();
		}finally {
			returnResource(jedisPool, jedis);
		}
	}
	
}
