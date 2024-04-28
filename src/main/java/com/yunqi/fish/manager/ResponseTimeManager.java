package com.yunqi.fish.manager;

import com.yunqi.fish.message.mail.MailUtil;
import com.yunqi.fish.message.sms.SmsUtil;
import com.yunqi.fish.model.entity.ClassAndMethod;
import com.yunqi.fish.mq.producer.IdentificationWarnProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.yunqi.fish.common.MethodEnum.IDENTIFICATION_INVOKE;
import static com.yunqi.fish.common.MethodEnum.TEST;

/**
 * 根据接口的响应时间做出相对应措施
 */
@Component
@RequiredArgsConstructor
public class ResponseTimeManager {

	private final IdentificationWarnProducer identificationWarnProducer;
	private final MailUtil mailUtil;
	private final SmsUtil smsUtil;

	private final Logger logger = LoggerFactory.getLogger(ResponseTimeManager.class);

	/**
	 * 算法调用接口的超时设置为 10 秒
	 */
	private static final Long IDENTIFICATION_TIMEOUT = 10 * 1000L;

	/**
	 * 超时处理
	 *
	 * @param response
	 */
	public void timeoutProcessing(ClassAndMethod response) {
		String send = "1531296605@qq.com";
		String content = "";

		//算法调用
		if (response.getClassName().equals(IDENTIFICATION_INVOKE.getClassName())
				&& response.getMethodName().equals(IDENTIFICATION_INVOKE.getMethodName())
				&& response.getResponseTime() >= IDENTIFICATION_TIMEOUT) {

			content = "鱼类识别 算法接口调用超时";
			HashMap<String, String> map = new HashMap<>();
			map.put("type","mail");
			map.put("send",send);
			map.put("content",content);
			identificationWarnProducer.send(map);
		}

		//测试
		if (response.getClassName().equals(TEST.getClassName())
				&& response.getMethodName().equals(TEST.getMethodName())
				&& response.getResponseTime() >= 0) {
			content = "鱼类识别 测试接口调用超时";
			HashMap<String, String> map = new HashMap<>();
			map.put("type", "mail");
			map.put("send", send);
			map.put("content", content);
//			mailUtil.sendMail(send, content);
//			smsUtil.sendCodeTrue("18316267773");
			identificationWarnProducer.send(map);
		}

	}
}
