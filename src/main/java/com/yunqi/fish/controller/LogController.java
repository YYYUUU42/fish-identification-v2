package com.yunqi.fish.controller;

import com.yunqi.fish.mapper.LogMapper;
import com.yunqi.fish.model.entity.Log;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志
 */
@RestController
@RequiredArgsConstructor
public class LogController {

	private final LogMapper logMapper;

	/**
	 * 获得所有的日志
	 * @return
	 */
	@GetMapping("/fish/getLog")
	@ApiOperation("获得所有的日志")
	public List<Log> getLog() {
		return logMapper.selectList(null);
	}
}
