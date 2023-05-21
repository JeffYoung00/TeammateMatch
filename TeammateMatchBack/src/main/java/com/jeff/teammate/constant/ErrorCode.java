package com.jeff.teammate.constant;

/**
 * 错误码
 *
 * @author yupi
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),//参数问题都可以用
    WRONG_CHAR(3,"使用了特殊字符"),

    REGISTERED(1,"用户名已被使用"),
    USER_NOT_FOUND(4,"登录失败,未找到用户"),
    WRONG_PASSWORD(5,"密码错误"),
    EMPTY_REQUEST(6,"访问失败,请求参数为空"),
    REGISTERED_TAG(7,"增加Tag失败,Tag已存在"),
    TAG_NOT_FOUNT(8,"删除Tag失败,Tag不存在"),
    GAME_NOT_FOUNT(9,"Game为空"),
    FULL_TABLE(9,"增加Tag失败,Tag已满"),


    JOINED_TEAM(10,"已加入过队伍"),
    NOT_OWNER(11,"非owner,无权限"),
    NOT_MEMBER(12,"并非队伍成员"),
    FULL_TEAM(13,"队伍满员"),
    TEAM_NOT_FOUND(14,"未找到队伍"),

    FAIL_TO_DELETE(13,"删除失败"),
    FAIL_TO_ADD(14,"增加失败"),

    OPERATION_FAILED(2,"操作失败"),
    NULL_ERROR(40001, "请求数据为空"),
    NOT_LOGIN(40100, "未登录"),
    NO_AUTH(40101, "无权限"),
    FORBIDDEN(40301, "禁止操作"),
    SYSTEM_ERROR(50000, "系统内部异常"),


    ;


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

