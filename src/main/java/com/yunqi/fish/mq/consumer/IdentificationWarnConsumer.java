package com.yunqi.fish.mq.consumer;

import com.yunqi.fish.exception.CustomException;
import com.yunqi.fish.message.mail.MailUtil;
import com.yunqi.fish.mq.idempotent.MessageQueueIdempotentHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 法调用超时告警消息队列消费者
 */
@Component
@RequiredArgsConstructor
public class IdentificationWarnConsumer implements StreamListener<String, MapRecord<String, String, String>> {

	private final StringRedisTemplate stringRedisTemplate;
	private final MailUtil mailUtil;
	private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

	private final Logger logger = LoggerFactory.getLogger(IdentificationWarnConsumer.class);

	/**
	 * 消费消息
	 *
	 * @param message
	 */
	@Override
	public void onMessage(MapRecord<String, String, String> message) {
		String stream = message.getStream();
		RecordId id = message.getId();
		if (!messageQueueIdempotentHandler.isMessageProcessed(id.toString())) {
			// 判断当前的这个消息流程是否执行完成
			if (messageQueueIdempotentHandler.isAccomplish(id.toString())) {
				return;
			}
			throw new CustomException(500, "消息未完成流程，需要消息队列重试");
		}

		try {
			sendingAlarmInformation(message.getValue());

			stringRedisTemplate.opsForStream().delete(Objects.requireNonNull(stream), id.getValue());
		} catch (Throwable ex) {
			//宕机了
			messageQueueIdempotentHandler.delMessageProcessed(id.toString());
			logger.error("算法超时告警消费异常", ex);
		}
		messageQueueIdempotentHandler.setAccomplish(id.toString());
	}

	/**
	 * 通过不同类型发送告警信息
	 * @param map
	 */
	private void sendingAlarmInformation(Map<String, String> map) {
		String type = map.get("type");
		String send = map.get("send");

		if (type.equals("mail")){
			String content = map.get("content");
			mailUtil.sendMail(send,content);
		}
	}


}
