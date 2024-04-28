package com.yunqi.fish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.fish.model.entity.FishCategory;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yu
* @description 针对表【fish_category(鱼类信息分类)】的数据库操作Mapper
* @Entity com.yunqi.model.entity.FishCategory
*/
@Mapper
public interface FishCategoryMapper extends BaseMapper<FishCategory> {

}




