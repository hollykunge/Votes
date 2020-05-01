package com.hollykunge.dictionary;

import java.util.Objects;

/**
 * @author: zhhongyu
 * @description: 内网环境下配置请求头中的名称
 * @since: Create in 14:49 2019/10/28
 */
public enum RequestHeaderEnums {
    /**
     * 内网环境
     */
    DNNAME("dnname",1);

    private String name;
    private int value;

    RequestHeaderEnums(String name, int value){
        this.name = name;
        this.value = value;
    }
    public static RequestHeaderEnums getEnumByName(String name){
        if(Objects.isNull(name)){
            return null;
        }
        for(RequestHeaderEnums temp: RequestHeaderEnums.values()){
            if(Objects.equals(temp.getName(),name)){
                return temp;
            }
        }
        return null;
    }
    public String getName() {
        return name;
    }
    public int getValue() {
        return value;
    }
}
