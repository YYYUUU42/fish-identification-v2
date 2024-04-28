package com.yunqi.fish.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 鱼类图片路径
 * @TableName fish_img
 */
@TableName(value ="fish_img")
@Data
public class FishImg implements Serializable {
    /**
     * 鱼类名称id
     */
    private Long fishId;

    /**
     * 鱼类图片路径
     */
    private String imgPath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FishImg other = (FishImg) that;
        return (this.getFishId() == null ? other.getFishId() == null : this.getFishId().equals(other.getFishId()))
            && (this.getImgPath() == null ? other.getImgPath() == null : this.getImgPath().equals(other.getImgPath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFishId() == null) ? 0 : getFishId().hashCode());
        result = prime * result + ((getImgPath() == null) ? 0 : getImgPath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fishId=").append(fishId);
        sb.append(", imgPath=").append(imgPath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}