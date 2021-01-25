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
	 * 每个缓存生效时间30分钟
	 */
	public static final long CACHE_HOLD_TIME_2M = 30 * 60 * 1000L;

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

	/**
	 * 删除所有缓存
	 */
	public static void removeAll() {
		CACHE_MAP.clear();
	}

	/**
	 * 删除某个缓存
	 * @param cacheName
	 */
	public static void remove(String cacheName) {
		CACHE_MAP.remove(cacheName);
		CACHE_MAP.remove(cacheName + "_HoldTime");
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
			return false;
		}
		return true;
	}

	public static Map<String, Object> getCacheMap() {
		return CACHE_MAP;
	}

	public static void main(String[] args) {
			Thread aa = new Thread(() -> {
				LocalCache.put("vote_token_127.0.0.1_useParentItem_1576033013706","vote_token_127.0.0.1_useParentItem_1576033013706");
				LocalCache.remove("vote_token_127.0.0.1_useParentItem_1576033013706");
				System.out.println("CacheName = [" + LocalCache.checkCacheName("vote_token_127.0.0.1_useParentItem_1576033013706") + "]");
				System.out.println("LocalCache = [" + LocalCache.get("vote_token_127.0.0.1_useParentItem_1576033013706") + "]");
			});
			Thread bb = new Thread(() -> {
				System.out.println("CacheName = [" + LocalCache.checkCacheName("vote_token_127.0.0.1_useParentItem_1576033013706") + "]");
				System.out.println("LocalCache = [" + LocalCache.get("vote_token_127.0.0.1_useParentItem_1576033013706") + "]");
			});
			aa.start();
			bb.start();
	}
}
