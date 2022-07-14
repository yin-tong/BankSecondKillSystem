package com.bsks.result;

/**
 * 接口返回的编码与信息
 */
public enum ResultCode {

    Success(20000, "请求成功"),
    UnknownException(-1, "未知异常"),
    SessionObsolete(1,"会话过时，请重新登录"),
    NoPermission(2,"权限不足,请先登录"),
    OauthServerError(3,"认证服务异常，请稍后再试");


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
