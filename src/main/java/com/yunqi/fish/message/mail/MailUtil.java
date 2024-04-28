package com.yunqi.fish.message.mail;

import com.yunqi.fish.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


/**
 * @author yu
 * @description 发送邮件
 * @date 2024-03-20
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MailUtil {
	/**
	 * 邮件发送者
	 */
	@Value("${spring.mail.username}")
	private String MAIL_SENDER;

	/**
	 * 注入QQ发送邮件的bean
	 */
	private final JavaMailSender javaMailSender;

	public void sendMail(String mail,String content) {
		MailBean mailBean = new MailBean();
		//接收者
		mailBean.setRecipient(mail);
		//标题
		mailBean.setSubject("鱼类识别警告");
		//内容主体
		mailBean.setContent(content);
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			//发送者
			mailMessage.setFrom(MAIL_SENDER);
			//接收者
			mailMessage.setTo(mailBean.getRecipient());
			//邮件标题
			mailMessage.setSubject(mailBean.getSubject());
			//邮件内容
			mailMessage.setText(mailBean.getContent());
			//发送邮箱
			javaMailSender.send(mailMessage);

			log.info("邮件发送成功");
		} catch (Exception e) {
			throw new CustomException(500,"邮件发送失败");
		}
	}
}
