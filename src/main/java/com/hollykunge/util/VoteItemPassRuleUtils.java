package com.hollykunge.util;

import com.hollykunge.model.Item;
import com.hollykunge.model.VoteItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 确定通过结果
 * @since: Create in 9:26 2019/11/1
 */
public class VoteItemPassRuleUtils {

    public static List<VoteItem> passVoteItems(List<VoteItem> voteItems, Item item, Integer totalNum) {
        double decimal = MarkToDecimalsUtil.transfer(item);
        List<VoteItem> result = new ArrayList<VoteItem>();
        voteItems.stream().forEach(voteItem -> {
            if (caculate(voteItem.getCurrentStatisticsNum(), decimal, totalNum)) {
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
        if (temp == 0 || Math.round(finalNum) > temp) {
            return false;
        }
        return true;
    }
}
