package com.yunqi.fish.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName fish_info
 */
@TableName(value ="fish_info")
@Data
public class FishInfo implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 中文名
     */
    private String fishkinds;

    /**
     * 英文名
     */
    private String fishlatinname;

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
        FishInfo other = (FishInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFishkinds() == null ? other.getFishkinds() == null : this.getFishkinds().equals(other.getFishkinds()))
            && (this.getFishlatinname() == null ? other.getFishlatinname() == null : this.getFishlatinname().equals(other.getFishlatinname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFishkinds() == null) ? 0 : getFishkinds().hashCode());
        result = prime * result + ((getFishlatinname() == null) ? 0 : getFishlatinname().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fishkinds=").append(fishkinds);
        sb.append(", fishlatinname=").append(fishlatinname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}