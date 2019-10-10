package com.hollykunge.config;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 16:17 2019/10/10
 */
public enum ItemStatusConfig {
    NOEFFECT("无效","0"),
    EFFECT("新建","1"),
    IS_SEND("发起","2"),
    IS_FINAL("结束","3");

    private String name;
    private String value;

    ItemStatusConfig(String name,String value){
        this.name = name;
        this.value = value;
    }
    public static ItemStatusConfig getEnumByValue(String value){
        if(null == value){
            return NOEFFECT;
        }
        for(ItemStatusConfig temp: ItemStatusConfig.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return NOEFFECT;
    }
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
}
