package com.jeff.teammate.exception;

import com.jeff.teammate.constant.ErrorCode;
import com.jeff.teammate.model.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author yupi
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Response businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return Response.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return Response.error(ErrorCode.SYSTEM_ERROR);
    }
}
