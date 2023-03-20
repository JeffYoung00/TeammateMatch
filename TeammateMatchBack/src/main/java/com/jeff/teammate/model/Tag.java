package com.jeff.teammate.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName tag
 */
@TableName(value ="tag")
@Data
public class Tag implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    @TableLogic
    private Integer isDelete;

    /**
     *
     */
    private String tagName;

    /**
     * 所属游戏名
     */
    private String gameName;

    /**
     * 分组标签, 1为分组, 0为普通标签
     */
    private Integer isGroup;

    /**
     * 父标签的id
     */
    private String groupName;

    @TableField(exist = false)
    private static final long serialVersionUID = 2L;
}