package com.jeff.teammate.constant;

public interface UserConstant {
    /**
     * Session中有USER_LOGIN_STATE代表用户在线
     */
    String USER_LOGIN_STATE="user";
    /**
     * Session中有GAME_NAME代表用户当前模块
     */
    String GAME_NAME="game";
    /**
     * Role
     */
    int Administrator=1;
    int CommonUser=0;
    /**
     * 在线, 填入user表中
     */
    int offline=0;
    int online=1;

}
