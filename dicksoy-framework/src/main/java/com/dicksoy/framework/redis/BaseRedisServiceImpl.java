package com.dicksoy.framework.redis;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

public class BaseRedisServiceImpl implements BaseRedisService {

	private static final Logger LOG = LoggerFactory.getLogger(BaseRedisServiceImpl.class);
	/**
	 * 锁超时时间
	 */
	private final static long ACCQUIRE_LOCK_TIMEOUT = 10 * 1000;
	/**
	 * 锁失效时间（秒）
	 * 上锁后超过此时间自动释放锁
	 */
	private final static int EXPIRE_TIME_SECOND = 4;
	/**
	 * 已经被设置的key
	 */
	private final ConcurrentMap<String, String> settedKeys;

	@Resource
	private JedisPool jedisPool;

	public BaseRedisServiceImpl() {
		this.settedKeys = new ConcurrentHashMap<String, String>();
		LOG.info("DistributedLockServiceImpl...init");
	}

	@Override
	public String lock(String lockName) {
		validateRedisKey(lockName);
		Jedis jedis = null;
		String retIdentifier = null;
		try {
			// 获取连接
			jedis = jedisPool.getResource();
			// 获取锁的超时时间，超过这个时间则放弃获取锁
			long timeout = currentTimeMillisFromRedis() + ACCQUIRE_LOCK_TIMEOUT;
			// 随机生成value值
			String identifier = UUID.randomUUID().toString();
			while (currentTimeMillisFromRedis() < timeout) {
				if (jedis.setnx(lockName, identifier) == 1) {
					settedKeys.put(lockName, identifier);
					jedis.expire(lockName, EXPIRE_TIME_SECOND);
					// 返回value值，用于释放锁时间确认
					retIdentifier = identifier;
					return retIdentifier;
				}
				// 返回-1代表key没有设置超时时间，为key设置一个超时时间
				if (jedis.ttl(lockName) == -1) {
					settedKeys.put(lockName, identifier);
					jedis.expire(lockName, EXPIRE_TIME_SECOND);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// 中断当前线程
					Thread.currentThread().interrupt();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(jedis);
		}
		return retIdentifier;
	}

	@Override
	public boolean releaseLock(String lockName, String identifier) {
		Jedis jedis = null;
		boolean retFalg = false;
		try {
			jedis = jedisPool.getResource();
			while (true) {
				// 监视lock，准备开启事务
				jedis.watch(lockName);
				// 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
				if (identifier.equals(jedis.get(lockName))) {
					Transaction transaction = jedis.multi();
					transaction.del(lockName);
					settedKeys.remove(lockName, identifier);
					List<Object> results = transaction.exec();
					if (results == null) {
						continue;
					}
					retFalg = true;
				}
				jedis.unwatch();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(jedis);
		}
		return retFalg;
	}

	@Override
	public boolean releaseLock(String lockName) {
		String identifier = settedKeys.get(lockName);
		return releaseLock(lockName, identifier);
	}

	/**
	 * 释放所有锁
	 * @param lockName 锁key
	 * @return
	 */
	@Override
	public boolean releaseAllLock() {
		Jedis jedis = null;
		boolean retFalg = false;
		try {
			jedis = jedisPool.getResource();
			Iterator<String> iter = settedKeys.keySet().iterator();
			while (iter.hasNext()) {
				// 监视lock，准备开启事务
				String key = iter.next();
				jedis.watch(key);
				Transaction transaction = jedis.multi();
				transaction.del(key);
				settedKeys.remove(key);
				List<Object> results = transaction.exec();
				if (results == null) {
					continue;
				}
				retFalg = true;
				jedis.unwatch();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeJedis(jedis);
		}
		return retFalg;
	}

	private Long currentTimeMillisFromRedis() throws Exception {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return Long.parseLong(jedis.time().get(0)) * 1000;
		} catch (Exception e) {
			closeJedis(jedis);
			throw e;
		} finally {
			closeJedis(jedis);
		}
	}

	/**
	 * 验证key是否有效
	 * @param redisKey
	 */
	private void validateRedisKey(String redisKey) {
		if (null == redisKey || "".equals(redisKey.trim())) {
			throw new IllegalArgumentException("ValidateKey Fail...");
		}
	}

	private void closeJedis(Jedis jedis) {
		if (null != jedis) {
			jedis.close();
		}
	}
}
