package com.hollykunge.vote.utils;

import com.hollykunge.vote.exception.BaseErrorInfo;

/**
 * @program: Lark-Server
 * @description: 异常基类
 * @author: Mr.Do
 * @create: 2019-09-24 16:19
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected String errorCode;

    /**
     * 错误信息
     */
    protected String errorMsg;

    public BaseException() {
        super();
    }

    public BaseException(BaseErrorInfo errorInfoInterface) {
        super(errorInfoInterface.getResultCode());
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BaseException(BaseErrorInfo errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getResultCode(), cause);
        this.errorCode = errorInfoInterface.getResultCode();
        this.errorMsg = errorInfoInterface.getResultMsg();
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorCode, String errorMsg) {
        super(errorCode);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
