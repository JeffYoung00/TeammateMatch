package com.jeff.teammate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName user
 *
 * 用于得到详细的用户信息: 基础信息+data+int+password
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
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
     * 0:离线;1:在线;2:删除;
     */
    private Integer userState;

    /**
     * 长度大于等于6
     */
    private String userName;

    /**
     * male or female or null
     */
    private String gender;

    /**
     * 头像的url
     */
    private String avatarUrl;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private String phone;


    /**
     * 普通用户=0, 管理员用户=1
     */
    private Integer userRole;

    /**
     * 已md5加密, 长度大于等于8
     */
    private String userPassword;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}