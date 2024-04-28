package com.yunqi.fish.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunqi.fish.common.ResponseResult;
import com.yunqi.fish.service.UserService;
import com.yunqi.fish.utils.BeanCopyUtils;
import com.yunqi.fish.mapper.UserInfoMapper;
import com.yunqi.fish.model.entity.UserInfo;
import com.yunqi.fish.model.vo.UserInfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/fish/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserInfoMapper userInfoMapper;


	/**
	 * 修改用户信息
	 *
	 * @param userInfo
	 * @return
	 */
	@PostMapping("/update")
	@ApiOperation("修改用户信息")
	public ResponseResult updateUser(@RequestBody UserInfo userInfo) {
		//todo 单一职责原则 这个接口包括添加用户和修改的功能，应分开
		userService.updateUser(userInfo);
		return ResponseResult.okResult();
	}

	/**
	 * 获得用户信息
	 *
	 * @param openid
	 * @return
	 */
	@GetMapping("/getUserInfo")
	@ApiOperation("获得用户信息")
	public ResponseResult getUserInfo(String openid) {
		UserInfo one = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, openid));
		UserInfoVo userInfoVo = null;
		if (one != null) {
			userInfoVo = BeanCopyUtils.copyBean(one, UserInfoVo.class);
		}
		return ResponseResult.okResult(userInfoVo);
	}
}
