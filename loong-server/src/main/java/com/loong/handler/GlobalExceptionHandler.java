package com.loong.handler;

import com.loong.constant.MessageConstant;
import com.loong.exception.BaseException;
import com.loong.result.Result;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * catch business exception
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * catch SQLIntegrityConstraintViolationException
     * 
     * @param exception
     * @return
     */
    @ExceptionHandler
    public Result sqlExceptionHandler(SQLIntegrityConstraintViolationException exception) {
        String message = exception.getMessage();

        if (message.contains("Duplicate entry")) {
            String[] error_msg = message.split(" ");

            String duplicate_username = error_msg[2] + " ";
            return Result.error(duplicate_username + MessageConstant.USERNAME_EXIST);
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }

    }

}
