package com.jeff.teammate.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 *
 * @TableName user
 *
 * 用于得到详细的用户信息: 基础信息+date+int+password
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    private int userState;

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
    private int userRole;

    /**
     * 已md5加密, 长度大于等于8
     */
    private String userPassword;

    /**
     * game id -> user vector
     */
    private String vector;

    /**
     * team id
     */
    private Integer teamId;

    @TableField(exist = false)
    private static final long serialVersionUID = 2L;
}