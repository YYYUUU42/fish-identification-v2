package com.yunqi.fish.common.constant;

/**
 * Redis 常量类
 */
public class RedisKeyConstant {
	/**
	 * 算法调用限流器
	 */
	public static final String REDIS_LIMITER_KEY = "fish:identification-limiter:";

	/**
	 * 算法调用时加锁
	 */
	public static final String IDENTIFICATION_REDISSON_LOCK = "fish:reidssion:identification-lock";

	/**
	 * 鱼类列表
	 */
	public static final String REDIS_FISH_LIST = "fish:fish-list";

	/**
	 * 定期删除文件时加锁
	 */
	public static final String DELETE_REDISSON_LOCK = "fish:redission:delete-lock";

	/**
	 * 已经调用过的算法图片
	 */
	public static final String REDIS_ALREADY_IDENTIFIED_IMAGE = "fish:image:";

	/**
	 * 算法超时告警消息保存队列 Topic 缓存标识
	 */
	public static final String IDENTIFICATION_WARN_STREAM_TOPIC_KEY = "fish:warn-stream";

	/**
	 * 算法超时告警消息保存队列 Group 缓存标识
	 */
	public static final String IDENTIFICATION_WARN_STREAM_GROUP_KEY = "fish:warn-stream:only-group";
}
