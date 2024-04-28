package com.yunqi.fish.service;

import com.yunqi.fish.model.entity.UserInfo;

public interface UserService {
	/**
	 * 修改用户信息
	 * @param userInfo
	 * @return
	 */
	void updateUser(UserInfo userInfo);
}
