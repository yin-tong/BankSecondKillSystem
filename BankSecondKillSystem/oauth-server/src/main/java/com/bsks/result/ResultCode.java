package com.bsks.result;

/**
 * 接口返回的编码与信息
 */
public enum ResultCode {

    Success(20000, "请求成功"),
    UnknownException(-1, "未知异常"),
    SessionObsolete(1,"会话过时，请重新登录"),
    NoPermission(2,"权限不足"),

    // oauth-server
    TokenOverdue(3001,"token已经过期"),
    WrongToken(3002,"会话过期，请重新登录"),
    KeyError(3003, "key不存在");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    ResultCode(String successMessage) {
        this.code = 0;
        this.message = successMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
