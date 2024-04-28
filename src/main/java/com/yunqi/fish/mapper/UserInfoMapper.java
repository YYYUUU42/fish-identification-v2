package com.yunqi.fish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.fish.model.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yu
* @description 针对表【user_info】的数据库操作Mapper
* @Entity com.yunqi.model.entity.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




