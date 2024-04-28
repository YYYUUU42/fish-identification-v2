package com.yunqi.fish.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    /**
     * 父分类id
     */
    private String categoryId;

    /**
     * 当前分类id
     */
    private String id;

    /**
     * 当前分类名称
     */
    private String name;

    /**
     *
     */
    private List<CategoryVo> categoryList;

    /**
     *
     */
    private String path;

    public CategoryVo(String categoryId, String id, String name) {
        this.categoryId = categoryId;
        this.id = id;
        this.name = name;
    }

    public CategoryVo(String categoryId, String id, String name, String path) {
        this.categoryId = categoryId;
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
