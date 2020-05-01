package com.hollykunge.util;

import com.hollykunge.exception.BaseException;
import org.springframework.util.StringUtils;

/**
 * @author: zhhongyu
 * @description: 封装断言类
 * @since: Create in 14:21 2020/4/29
 */
public class Assert {
    public static void notNull(String arg){
        if(StringUtils.isEmpty(arg)){
            throw new BaseException("参数不能为空...");
        }
    }
}
