package com.yunqi.fish.controller;

import com.yunqi.fish.common.ResponseResult;
import com.yunqi.fish.service.FishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鱼类信息
 */
@RestController
@RequestMapping("/fish")
@RequiredArgsConstructor
@Api("鱼类信息")
public class FishController {

	private final FishService fishService;

	/**
	 * 得到鱼的种类 (采集上传部分)
	 * @return
	 */
	@GetMapping("/fishcategory/list")
	@ApiOperation("得到鱼的种类 (采集上传部分)")
	public ResponseResult categoryList(){
		return ResponseResult.okResult(fishService.getCategories());
	}

	/**
	 * 鱼的信息 （中文名，英文名）
	 * @return
	 */
	@GetMapping("/fishinfo/list")
	@ApiOperation("鱼的信息 （中文名，英文名）")
	public ResponseResult infoList(){
		return ResponseResult.okResult(fishService.getInfoList());
	}
}
