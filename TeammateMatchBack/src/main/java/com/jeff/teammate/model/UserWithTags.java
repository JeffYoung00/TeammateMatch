package com.jeff.teammate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用sql的tag搜索得到的结果类,去掉了password和updateTime,加上了联表查询出的tags
 * 用于在内存中计算
 */
@Data
public class UserWithTags implements Serializable {
    private Long id;
    private String userName;
    private String gender;
    private String avatarUrl;
    private String email;
    private String phone;
    private Date createTime;
    private Integer userState;
    private Integer userRole;
    private String tags;
    private Integer tagsCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 3L;
}
