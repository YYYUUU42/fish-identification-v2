package com.yunqi.fish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.fish.model.entity.FishInfo;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yu
* @description 针对表【fish_info】的数据库操作Mapper
* @Entity com.yunqi.model.entity.FishInfo
*/
@Mapper
public interface FishInfoMapper extends BaseMapper<FishInfo> {

}




