package com.bsks.result;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接口统一返回格式
 * @param <T>
 */
public class Result<T>{

    private int code;

    private String message;

    private String time;

    private long timestamp;

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

    public Result(String successMessage, T data) {
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

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data +
                '}';
    }
}
