package com.bsks.api.result;

/**
 * 返回自定义异常信息
 */
public class ResultException extends RuntimeException{

    private int errCode = ResultCode.UnknownException.getCode();

    public ResultException() {
        super(ResultCode.UnknownException.getMessage());
    }

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.errCode = resultCode.getCode();
    }

    public ResultException(String errMessage) {
        super(errMessage);
        this.errCode = -1;
    }

    public ResultException(int code, String message) {
        super(message);
        this.errCode = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
