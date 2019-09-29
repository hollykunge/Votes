package com.hollykunge.vote.exception;

/**
 * @program: Lark-Server
 * @description: 异常基础接口
 * @author: Mr.Do
 * @create: 2019-09-25 14:29
 */
public interface BaseErrorInfo {
    /**
     * 错误码
     */
    String getResultCode();
    /**
     * 错误描述
     */
    String getResultMsg();
}
