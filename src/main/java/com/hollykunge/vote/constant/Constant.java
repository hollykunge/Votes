package com.hollykunge.vote.constant;

import com.hollykunge.vote.exception.BaseErrorInfo;

/**
 * @program: Lark-Server
 * @description: 常量
 * @author: Mr.Do
 * @create: 2019-09-25 14:10
 */
public enum Constant implements BaseErrorInfo {
    // 操作错误定义
    SUCCESS("200", "成功"),
    BODY_NOT_MATCH("400", "请求数据格式错误"),
    NOT_FOUND("404", "未找到资源"),
    SERVER_ERROR("500", "服务器内部错误"),
    SERVER_BUSY("503", "服务器忙"),
    PARAMS_ERROR("500001", "输入参数异常"),
    USER_ERROR("500001", "不存在该用户"),
    ;

    private String resultCode;
    private String resultMsg;

    Constant(String resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
    @Override
    public String getResultCode() {
        return null;
    }

    @Override
    public String getResultMsg() {
        return null;
    }
}
