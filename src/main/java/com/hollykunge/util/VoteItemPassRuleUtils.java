package com.hollykunge.util;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.VoteItem;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 确定通过结果
 * @since: Create in 9:26 2019/11/1
 */
public class VoteItemPassRuleUtils {

    public static List<VoteItem> passVoteItems(List<VoteItem> voteItems, Item item, Integer totalNum) throws Exception {
//        double decimal = MarkToDecimalsUtil.transfer(item);
        double decimal = 0;
        if(!StringUtils.isEmpty(item.getAgreePassPersent())){
            getDecimal(item.getAgreePassPersent());
        }
        List<VoteItem> result = new ArrayList<VoteItem>();
        double finalDecimal = decimal;
        voteItems.stream().forEach(voteItem -> {
            if (caculate(voteItem.getCurrentStatisticsNum(), finalDecimal, totalNum)) {
                result.add(voteItem);
            }
        });
        return result;
    }

    /**
     * 四舍五入判断是否通过
     * @param num      得票结果
     * @param decimal  系数
     * @param totalNum 专家人数
     * @return
     */
    public static boolean caculate(Integer num,
                                   Double decimal,
                                   Integer totalNum) {
        Integer temp = num;
        if (temp == null) {
            temp = 0;
        }
        double finalNum = totalNum * decimal;
        if (temp == 0 || Math.ceil(finalNum) > temp) {
            return false;
        }
        return true;
    }

    public static double getDecimal(String data)throws Exception{
        if(StringUtils.isEmpty(data)){
            throw new BaseException("百分比数据不能为空...");
        }
        try{
            double parseDouble = Double.parseDouble(data);
            if(parseDouble > 100 || parseDouble < 0){
                throw new BaseException("百分比不能大于100或者小于0");
            }
            return parseDouble/100;
        }catch (Exception e){
            throw e;
        }
    }
}
