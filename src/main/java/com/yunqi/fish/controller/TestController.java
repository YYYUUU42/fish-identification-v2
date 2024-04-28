package com.yunqi.fish.controller;

import com.yunqi.fish.anno.Time;
import com.yunqi.fish.common.ResponseResult;
import com.yunqi.fish.job.DetectionJob;
import com.yunqi.fish.model.entity.ClassAndMethod;
import com.yunqi.fish.model.vo.AlgorithmVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

	private final DetectionJob detectionJob;

	private final StringRedisTemplate stringRedisTemplate;



//	@Time
	@PostMapping("/test")
	public ResponseResult test() throws Exception{

		detectionJob.algorithmDetection();
		return ResponseResult.okResult(123);
	}
}