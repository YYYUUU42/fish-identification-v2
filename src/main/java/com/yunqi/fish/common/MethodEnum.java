package com.yunqi.fish.common;

import lombok.Data;

/**
 * 每个接口对应的类和方法名
 */
public enum MethodEnum {
	/**
	 * 算法调用接口
	 */
	IDENTIFICATION_INVOKE("IdentificationController", "identification"),

	/**
	 * 测试接口
	 */
	TEST("TestController","test");

	private String className;
	private String methodName;

	MethodEnum(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

}
