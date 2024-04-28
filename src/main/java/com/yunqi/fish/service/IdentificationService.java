package com.yunqi.fish.service;

import com.alibaba.fastjson.JSONObject;
import com.yunqi.fish.model.vo.AlgorithmVo;
import org.springframework.web.multipart.MultipartFile;

public interface IdentificationService {

	/**
	 * 鱼类识别
	 * @param file
	 * @return
	 */
	AlgorithmVo identification(MultipartFile file);
}
