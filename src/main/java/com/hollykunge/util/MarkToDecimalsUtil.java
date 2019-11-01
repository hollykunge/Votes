package com.hollykunge.util;

import com.hollykunge.exception.BaseException;
import org.springframework.util.StringUtils;

/**
 * @author: zhhongyu
 * @description: 字符串分数转为小数
 * @since: Create in 9:51 2019/11/1
 */
public class MarkToDecimalsUtil {

    public static double transfer(String mark) {
        if(StringUtils.isEmpty(mark)){
            throw new BaseException("参数不能为空..");
        }
        if(!mark.contains("/")){
            throw new BaseException("参数中没有分数线 / ...");
        }
        String[] split = mark.split("/");
        if(split.length < 1){
            throw new BaseException("百分数字符串不规范..");
        }
        double aa = Double.parseDouble(split[0])/Integer.parseInt(split[1]);
        return aa;
    }

}
