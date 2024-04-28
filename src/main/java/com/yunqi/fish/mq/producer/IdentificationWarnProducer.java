package com.yunqi.fish.mq.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.yunqi.fish.common.constant.RedisKeyConstant.IDENTIFICATION_WARN_STREAM_TOPIC_KEY;


/**
 * 算法调用超时告警消息队列生产者
 */
@Component
@RequiredArgsConstructor
public class IdentificationWarnProducer {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 算法调用超时告警
     * @param producerMap
     */
    public void send(Map<String,String> producerMap) {
        stringRedisTemplate.opsForStream().add(IDENTIFICATION_WARN_STREAM_TOPIC_KEY, producerMap);
    }
}
