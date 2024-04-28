package com.yunqi.fish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.fish.model.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yu
* @description 针对表【log】的数据库操作Mapper
* @Entity com.yunqi.model.entity.Log
*/
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}




