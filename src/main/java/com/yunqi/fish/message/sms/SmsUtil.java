package com.yunqi.fish.message.sms;

import com.alibaba.fastjson.JSON;
import com.yunqi.fish.exception.CustomException;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "yu.third-party.sms")
public class SmsUtil {
	private String apiUrl;
	private String appId;
	private String appSecret;
	private String templateId;

	/**
	 * 提供给别的服务进行调用
	 *
	 * @param phone
	 * @return
	 */
	public void sendCodeTrue(String phone) {
		try {
			ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);

			//发送短信，参数需要通过Map传递
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("number", phone);
			params.put("templateId", templateId);

			String result = client.send(params);

			Map map = JSON.parseObject(result, Map.class);
			int status = (int) map.get("code");
			if (status == 0) {
				log.info("短信发送成功");
			}
		} catch (Exception e) {
			log.error("===",e);
			throw new CustomException(500, "短信发送失败");
		}
	}
}
