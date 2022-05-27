package com.hollykunge.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @deprecation 本地缓存类
 * @author zhhongyu
 * @since 2019-12-10
 */
public final class LocalCache {

	/**
	 * 预缓存信息
	 */
	private static final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<String, Object>();
	/**
	 * 失效的缓存集
	 */
	private static final Map<String, Object> FAIL_CACHE_MAP = new ConcurrentHashMap<String, Object>();

	/**
	 * 每个缓存生效时间30分钟
	 */
	public static final long CACHE_HOLD_TIME_2M = 30 * 60 * 1000L;
	/**
	 * 失效缓存失效时间40分钟，防止内存堆数据溢出
	 */
	public static final long FAIL_CACHE_HOLD_TIME = 40 * 60 * 1000L;

	/**
	 * 每个缓存生效时间1分钟
	 */
	public static final long CACHE_HOLD_TIME_1M = 60 * 1000L;

	/**
	 * 存放一个缓存对象，默认保存时间30分钟
	 * @param cacheName
	 * @param obj
	 */
	public static void put(String cacheName, Object obj) {
		put(cacheName, obj, CACHE_HOLD_TIME_2M);
	}

	/**
	 * 将失效的缓存，存放在失效缓存集合内存中
	 * @param cacheName
	 * @param obj
	 */
	public static void putFailCache(String cacheName, Object obj,long holdTime){
		FAIL_CACHE_MAP.put(cacheName,obj);
		FAIL_CACHE_MAP.put(cacheName + "_HoldTime", System.currentTimeMillis() + holdTime);
	}

	/**
	 * 存放一个缓存对象，保存时间为holdTime
	 * @param cacheName
	 * @param obj
	 * @param holdTime
	 */
	public static void put(String cacheName, Object obj, long holdTime) {
		CACHE_MAP.put(cacheName, obj);
		CACHE_MAP.put(cacheName + "_HoldTime", System.currentTimeMillis() + holdTime);//缓存失效时间
	}

	/**
	 * 取出一个缓存对象
	 * @param cacheName
	 * @return
	 */
	public static Object get(String cacheName) {
		if (checkCacheName(cacheName)) {
			return CACHE_MAP.get(cacheName);
		}
		return null;
	}
	public static Object getFailCache(String cacheName) {
		if (checkFailCacheName(cacheName)) {
			return FAIL_CACHE_MAP.get(cacheName);
		}
		return null;
	}

	/**
	 * 删除所有缓存
	 */
	public static void removeAll() {
		CACHE_MAP.clear();
		FAIL_CACHE_MAP.clear();
	}

	/**
	 * 删除某个缓存
	 * @param cacheName
	 */
	public static void remove(String cacheName) {
		CACHE_MAP.remove(cacheName);
		CACHE_MAP.remove(cacheName + "_HoldTime");
	}

	public static void removeFailCache(String cacheName) {
		FAIL_CACHE_MAP.remove(cacheName);
		FAIL_CACHE_MAP.remove(cacheName + "_HoldTime");
	}

	/**
	 * 检查缓存对象是否存在，
	 * 若不存在，则返回false
	 * 若存在，检查其是否已过有效期，如果已经过了则删除该缓存并返回false
	 * @param cacheName
	 * @return
	 */
	public static boolean checkCacheName(String cacheName) {
		Long cacheHoldTime = (Long) CACHE_MAP.get(cacheName + "_HoldTime");
		if (cacheHoldTime == null || cacheHoldTime == 0L) {
			return false;
		}
		if (cacheHoldTime < System.currentTimeMillis()) {
			remove(cacheName);
			//被动失效的缓存，放到失败缓存集合中
			putFailCache(cacheName,cacheName,FAIL_CACHE_HOLD_TIME);
			return false;
		}
		return true;
	}

	public static boolean checkFailCacheName(String cacheName) {
		Long cacheHoldTime = (Long) FAIL_CACHE_MAP.get(cacheName + "_HoldTime");
		if (cacheHoldTime == null || cacheHoldTime == 0L) {
			return false;
		}
		if (cacheHoldTime < System.currentTimeMillis()) {
			removeFailCache(cacheName);
			return false;
		}
		return true;
	}

	public static Map<String, Object> getCacheMap() {
		return CACHE_MAP;
	}

	public static Map<String, Object> getFailCacheMap() {
		return FAIL_CACHE_MAP;
	}
}
