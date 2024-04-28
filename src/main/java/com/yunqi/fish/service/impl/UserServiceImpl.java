package com.yunqi.fish.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yunqi.fish.exception.CustomException;
import com.yunqi.fish.service.UserService;
import com.yunqi.fish.mapper.UserInfoMapper;
import com.yunqi.fish.model.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.baomidou.mybatisplus.extension.toolkit.Db.save;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserInfoMapper userInfoMapper;
	private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*[^a-zA-Z0-9]).{6,}$";


	/**
	 * 根据传来的 userInfo 来判断是新增用户还是修改用户信息
	 *
	 * @param userInfo
	 * @return
	 */
	@Override
	public void updateUser(UserInfo userInfo) {
		validateUserInfo(userInfo);

		// 查询数据库中该openid是否存在，同时统计username和account的数量
		Optional<UserInfo> existingInfo = Optional.ofNullable(
				userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
						.eq(UserInfo::getOpenid, userInfo.getOpenid())
				)
		);

		Long usernameCount = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, userInfo.getUsername()));
		Long accountCount = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getAccount, userInfo.getAccount()));

		if (existingInfo.isPresent()) {
			UserInfo info = existingInfo.get();
			updateUser(userInfo, info, usernameCount, accountCount);
		} else {
			saveUser(userInfo, usernameCount, accountCount);
		}
	}

	/**
	 * 添加用户
	 *
	 * @param userInfo
	 * @param usernameCount
	 * @param accountCount
	 */
	private void saveUser(UserInfo userInfo, Long usernameCount, Long accountCount) {
		if (usernameCount > 0 || accountCount > 0) {
			throw new CustomException(1005, "用户名或账号已存在");
		}

		// 判断是否添加成功
		boolean saveResult = save(userInfo);
		if (!saveResult) {
			throw new CustomException(1006, "添加人物信息失败");
		}
	}

	/**
	 * 修改用户
	 *
	 * @param userInfo
	 * @param info
	 * @param usernameCount
	 * @param accountCount
	 */
	private void updateUser(UserInfo userInfo, UserInfo info, Long usernameCount, Long accountCount) {
		if (usernameCount > 0 && !info.getUsername().equals(userInfo.getUsername())) {
			throw new CustomException(1003, "用户名已存在");
		}

		if (accountCount > 0 && !info.getAccount().equals(userInfo.getAccount())) {
			throw new CustomException(1004, "账号已存在");
		}

		userInfo.setId(info.getId());

		// 判断是否修改成功
		int updateResult = userInfoMapper.updateById(userInfo);
		if (updateResult <= 0) {
			throw new CustomException(1006, "修改人物信息失败");
		}
	}

	/**
	 * 参数校验
	 *
	 * @param userInfo
	 */
	private void validateUserInfo(UserInfo userInfo) {
		String username = userInfo.getUsername();
		String account = userInfo.getAccount();
		String password = userInfo.getPassword();

		if (StringUtils.isAnyBlank(username, account, password)) {
			throw new CustomException(1001, "用户名、账号、密码不能为空");
		}

		if (!password.matches(PASSWORD_PATTERN)) {
			throw new CustomException(1002, "密码规则:包含数字以及大小写字母，不可有特殊字符,密码长度至少为6");
		}
	}
}
