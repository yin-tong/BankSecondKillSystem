package com.bsks.api.conponent;


import com.bsks.api.result.Result;
import com.bsks.api.result.ResultException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * 捕获系统出现的所有异常
     */
    @ExceptionHandler(Exception.class)
    public Result systemError(Exception e) {
        e.printStackTrace();
        return new Result(-1, e.getMessage());
    }

    @ExceptionHandler(value = ResultException.class)
    public Result globalExceptionHandler(ResultException e){
        e.printStackTrace();
        Result result = new Result(e.getErrCode(), e.getMessage());
        return result;
    }
}
