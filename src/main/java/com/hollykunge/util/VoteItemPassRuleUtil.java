package com.hollykunge.util;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.model.VoteItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 确定通过结果
 * @since: Create in 9:26 2019/11/1
 */
public class VoteItemPassRuleUtil {
    private static final String VOTEPASSPERSENT = VoteConstants.VOTE_PASS_PERSENT;

    public static List<VoteItem> passVoteItems(List<VoteItem> voteItems){
        double decimal = MarkToDecimalsUtil.transfer(VOTEPASSPERSENT);
        VoteItem maxVotem = Collections.max(voteItems,(voteItem1,voteItem2) ->{
            if(voteItem1.getVoteItemOrder() == null || voteItem2.getVoteItemOrder() == null){
                return -1;
            }
            return voteItem2.getVoteItemOrder().compareTo(voteItem1.getVoteItemOrder());
        });
        List<VoteItem> result = new ArrayList<VoteItem>();
        voteItems.stream().forEach(voteItem -> {
            if(caculate(voteItem.getVoteItemOrder(), decimal, maxVotem.getVoteItemOrder())){
                result.add(voteItem);
            }
        });
       return result;
    }

    private static boolean caculate(Integer num,
                                   Double decimal,
                                   Integer maxVotem){
        Integer temp = num;
        if(temp == null){
            temp = 0;
        }
        double fianlNum = maxVotem*decimal;
        if(temp == 0 || fianlNum > temp){
            return false;
        }
        return true;
    }
}
