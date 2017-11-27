package com.dicksoy.framework.redis;

public interface BaseRedisService {

	/**
	 * 加锁
	 * @param locaName 锁key
	 * @return 锁标识
	 */
	String lock(String lockName);
	
	/**
	 * 释放锁
	 * @param lockName 锁key
	 * @param identifier 释放锁的标识
	 * @return
	 */
	boolean releaseLock(String lockName, String identifier);
	
	/**
	 * 释放锁
	 * @param lockName 锁key
	 * @return
	 */
	boolean releaseLock(String lockName);
	
	/**
	 * 释放所有锁
	 * @param lockName 锁key
	 * @return
	 */
	boolean releaseAllLock();
}
