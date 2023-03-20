package com.jeff.teammate.exception;
import com.jeff.teammate.constant.ErrorCode;

public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    public int getCode(){
        return this.code;
    }
}
