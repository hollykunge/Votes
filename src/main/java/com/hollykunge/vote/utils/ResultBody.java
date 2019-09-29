package com.hollykunge.vote.utils;

import com.alibaba.fastjson.JSONObject;
import com.hollykunge.vote.constant.Constant;
import com.hollykunge.vote.exception.BaseErrorInfo;
import lombok.Data;

/**
 * @program: Lark-Server
 * @description: 结果集
 * @author: Mr.Do
 * @create: 2019-09-24 14:50
 */
@Data
public class ResultBody<T> {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果
     */
    private T result;

    public ResultBody() {
    }

    private ResultBody(T data){
        this.code = "200";
        this.message = "success";
        this.result = data;
    }

    public ResultBody(BaseErrorInfo errorInfo) {
        this.code = errorInfo.getResultCode();
        this.message = errorInfo.getResultMsg();
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> ResultBody<T> success() {
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static <T> ResultBody<T> success(T data) {
        ResultBody rb = new ResultBody();
        rb.setCode(Constant.SUCCESS.getResultCode());
        rb.setMessage(Constant.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static <T> ResultBody<T> error(BaseErrorInfo errorInfo) {
        ResultBody rb = new ResultBody();
        rb.setCode(errorInfo.getResultCode());
        rb.setMessage(errorInfo.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String code, String message) {
        ResultBody rb = new ResultBody();
        rb.setCode(code);
        rb.setMessage(message);
        rb.setResult(null);
        return rb;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
