package com.jeff.teammate.model.response;

import com.jeff.teammate.constant.ErrorCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public Response(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    //成功
    public static<T> Response<T> success(T data){
        return new Response<T>(0,data,"success");
    }
    //失败
    public static<T> Response<T> error(int code,String message){
        return new Response<T>(code,null,message);
    }
    public static<T> Response<T> error(ErrorCode err){
        return new Response<T>(err.getCode(),null,err.getMessage());
    }

    private static final long serialVersionUID = 111L;
}
