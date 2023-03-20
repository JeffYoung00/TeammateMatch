package com.jeff.teammate.model.response;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jeff.teammate.model.User;
import com.jeff.teammate.model.UserWithTags;
import lombok.Data;

import java.util.Date;

/**
 * 脱敏处理之后的User,去掉了password和updateTime
 * 用于返回
 */
@Data
public class SafeUser {

    private Long id;
    private Date createTime;
    private String userName;
    private String gender;
    private String avatarUrl;
    private String email;
    private String phone;
    private Integer userState;
    private Integer userRole;

    public SafeUser(User user){
        this.userName=user.getUserName();
        this.avatarUrl=user.getAvatarUrl();
        this.email=user.getEmail();
        this.phone=user.getPhone();
        this.gender=user.getGender();
        this.id=user.getId();

        this.createTime=user.getCreateTime();
        this.userState=user.getUserState();
        this.userRole=user.getUserRole();
    }

    public SafeUser(UserWithTags user){
        this.userName=user.getUserName();
        this.avatarUrl=user.getAvatarUrl();
        this.email=user.getEmail();
        this.phone=user.getPhone();
        this.gender=user.getGender();
        this.id=user.getId();

        this.createTime=user.getCreateTime();
        this.userState=user.getUserState();
        this.userRole=user.getUserRole();
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 5L;
}
