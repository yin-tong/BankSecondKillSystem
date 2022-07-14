package com.bsks.api.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接口统一返回格式
 * @param <T>
 */
@ApiModel(value="Result 统一返回格式", description="统一返回格式")
public class Result<T>{

    @ApiModelProperty(value = "返回编码 正确编码:20000 ,其他为错误编码")
    private int code;

    @ApiModelProperty(value = "返回信息")
    private String message;

    @ApiModelProperty(value = "返回时间")
    private String time;

    @ApiModelProperty(value = "返回时间戳")
    private long timestamp;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(){}

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(date);
        this.timestamp = new Timestamp(date.getTime()).getTime();
    }

    public Result(String successMessage,T data) {
        this.code = 20000;
        this.message = successMessage;
        this.data = data;
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(date);
        this.timestamp = new Timestamp(date.getTime()).getTime();
    }

    public Result(String successMessage) {
        this(successMessage,null);
    }

    public Result(ResultCode resultCode) {
        this(resultCode.getCode(),resultCode.getMessage());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
