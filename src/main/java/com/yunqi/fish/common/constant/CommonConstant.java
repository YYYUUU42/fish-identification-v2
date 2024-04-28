package com.yunqi.fish.common.constant;


public class CommonConstant {

	/**
	 * 算法路径
	 */
	//TODO 改回服务器的地址再部署
	public static final String ALGORITHM_PATH = "E:\\fish\\鱼类识别算法0530";

	/**
	 * 需要运行算法的图片路径
	 */
	public static final String IMG_PATH = ALGORITHM_PATH + "\\detect\\images";

	/**
	 * 需要定期删除路径
	 */
	public static final String ALGORITHM_RESULT_DELETE = "C:\\fish\\鱼类识别算法0530\\detect\\runs\\detect";

	/**
	 * 存放调用算法后图片路径
	 */
	public static final String NGINX_PATH = "C:\\nginx-1.24.0\\html\\static";

	/**
	 * 内网穿透
	 */
	public static final String NGINX_IMG = "https://5f7453b7.r21.cpolar.top/static/";

	/**
	 * 算法调用接口地址，用于定时访问，看是否有异常
	 */
	public static final String IDENTIFICATION_URL = "https://5f7453b7.r21.cpolar.top/fish/identification";

}
