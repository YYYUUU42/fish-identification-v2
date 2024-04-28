package com.yunqi.fish.model.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlgorithmVo {
    /**
     * 鱼类中文名称
     */
    private String chineseName;
    /**
     * 鱼类英文名称
     */
    private String englishName;
    /**
     * 置信度
     */
    private String confidenceDegree;
    /**
     * 鱼类图片路径,算法改后的
     */
    private String newImgPath;
    /**
     * 鱼类图片路径,原图
     */
    private String oldImgPath;

}
