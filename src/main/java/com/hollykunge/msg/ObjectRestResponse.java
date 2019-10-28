package com.hollykunge.msg;

/**
 * 对象返回类型
 * @author 协同设计小组
 * @date 2017/6/11
 */
public class ObjectRestResponse<T> extends BaseResponse {

    T result;
    boolean rel;

    public boolean isRel() {
        return rel;
    }

    public void setRel(boolean rel) {
        this.rel = rel;
    }


    public ObjectRestResponse rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public ObjectRestResponse data(T data) {
        this.setResult(data);
        return this;
    }

    public ObjectRestResponse msg(String msg) {
        this.setMessage(msg);
        return this;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
