package com.hollykunge.dictionary;

import java.util.Objects;

/**
 * @author: zhhongyu
 * @description: 配置文件中配置系统所属环境的配置编码
 * @since: Create in 14:49 2019/10/28
 */
public enum ApplicationSystemConfigEnums {
    /**
     * 内网环境
     */
    INTRANET("intranet",1),
    /**
     * 外网环境
     */
    EXTRANET("extranet",2);

    private String name;
    private int value;

    ApplicationSystemConfigEnums(String name, int value){
        this.name = name;
        this.value = value;
    }
    public static ApplicationSystemConfigEnums getEnumByName(String name){
        if(Objects.isNull(name)){
            return null;
        }
        for(ApplicationSystemConfigEnums temp: ApplicationSystemConfigEnums.values()){
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
