package com.yunqi.fish.message.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailBean implements Serializable {
	private static final long serialVersionUID = -2116367492649751914L;
	/**
	 * 邮件接收人
	 */
	private String recipient;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;
}
