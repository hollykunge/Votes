package com.hollykunge.util;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;

/**
 * @author: zhhongyu
 * @description: 字符串分数转为小数
 * @since: Create in 9:51 2019/11/1
 */
public class MarkToDecimalsUtil {

    public static double transfer(Item item) {
        if(judgeParams(item)){
            String[] split = item.getAgreePassPersent().split("/");
            double aa = Double.parseDouble(split[0])/Integer.parseInt(split[1]);
            return aa;
        }
        return 0;
    }

    private static boolean judgeParams(Item item){
        if(item == null){
            throw new BaseException("参数不能为空...");
        }
        if(item.getAgreePassPersent() == null){
            throw new BaseException("百分数不能为空...");
        }
        if(!item.getAgreePassPersent().contains("/")){
            throw new BaseException("没有分数...");
        }
        String[] split = item.getAgreePassPersent().split("/");
        if(split.length > 2 || split.length < 1){
            throw new BaseException("分数不符合要求...");
        }
        if(Integer.parseInt(split[0]) > Integer.parseInt(split[1])){
            throw new BaseException("分子不能大于分母");
        }
        return true;
    }

}
