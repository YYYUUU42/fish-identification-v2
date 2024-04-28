package com.yunqi.fish.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.fish.model.entity.FishImg;
import org.apache.ibatis.annotations.Mapper;

/**
* @author yu
* @description 针对表【fish_img(鱼类图片路径)】的数据库操作Mapper
* @Entity com.yunqi.model.entity.FishImg
*/
@Mapper
public interface FishImgMapper extends BaseMapper<FishImg> {

}




