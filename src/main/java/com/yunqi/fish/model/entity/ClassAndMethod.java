package com.yunqi.fish.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassAndMethod {

	/**
	 * 类型
	 */
	private String className;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 响应时间
	 */
	private Long responseTime;
}
