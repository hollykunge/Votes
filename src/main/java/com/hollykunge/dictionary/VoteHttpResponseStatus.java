package com.hollykunge.dictionary;

/**
 * @author: zhhongyu
 * @description: 状态码字典（200成功，100服务器返给前端提示，400客户端错误，500服务器内部错误）
 * @since: Create in 14:49 2019/10/28
 */
public enum VoteHttpResponseStatus {
    SUCCESS("success",200),
    INFORMATIONAL("information",100),
    CLIENT_ERROR("clienterror",400),
    SERVER_ERROR("servererror",500);

    private String name;
    private int value;

    VoteHttpResponseStatus(String name,int value){
        this.name = name;
        this.value = value;
    }
    public static VoteHttpResponseStatus getEnumByValue(int value){
        for(VoteHttpResponseStatus temp: VoteHttpResponseStatus.values()){
            if(temp.getValue() == value){
                return temp;
            }
        }
        return CLIENT_ERROR;
    }
    public String getName() {
        return name;
    }
    public int getValue() {
        return value;
    }
}
