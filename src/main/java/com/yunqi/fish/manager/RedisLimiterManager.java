package com.yunqi.fish.manager;


import com.yunqi.fish.common.AppHttpCodeEnum;
import com.yunqi.fish.exception.CustomException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 限流器
 */
@Component
public class RedisLimiterManager {

	@Resource
	private RedissonClient redissonClient;

	/**
	 * 限流操作
	 *
	 * @param key 区分不同的限流器，比如不同的用户 openid 应该分别统计
	 */
	public void doRateLimit(String key) {
		// 创建一个名称为 user_limiter 的限流器，一秒访问一次
		RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
		rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);

		// 每当一个操作来了后，请求一个令牌
		boolean canOp = rateLimiter.tryAcquire(1);
		if (!canOp) {
			throw new CustomException(AppHttpCodeEnum.TOO_MANY_REQUEST);
		}
	}
}
