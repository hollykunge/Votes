package com.hollykunge.service.impl;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;
import com.hollykunge.repository.UserVoteItemRepository;
import com.hollykunge.repository.VoteItemRepository;
import com.hollykunge.service.UserVoteItemService;
import com.hollykunge.util.ExceptionCommonUtil;
import com.hollykunge.util.MarkToDecimalsUtil;
import com.hollykunge.util.VoteItemPassRuleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:05 2019/10/15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserVoteItemServiceImp implements UserVoteItemService {
    @Autowired
    private UserVoteItemRepository userVoteItemRepository;
    @Autowired
    private VoteItemRepository voteItemRepository;

    @Override
    public UserVoteItem add(UserVoteItem userVoteItem) {
        try {
            userVoteItemRepository.saveAndFlush(userVoteItem);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        return userVoteItem;
    }

    @Override
    public List<UserVoteItem> findByUserIp(String ip) {
        return userVoteItemRepository.findByIp(ip);
    }

    @Override
    public List<UserVoteItem> findByItemAndIp(Item item, String ip) {
        return userVoteItemRepository.findByItemAndIp(item, ip);
    }

    @Override
    public Long countIpByItem(Item item) {
        if (StringUtils.isEmpty(item.getId())) {
            throw new BaseException("itemid不能为空...");
        }
        return userVoteItemRepository.countIp(item.getId());
    }

    @Override
    public Map<String, Object> getStatistics(Item item) throws Exception {
        Map<String, Object> result = new HashMap<>(256);
        //投票人数
        result.put("coutips", this.countIpByItem(item));
        //获取item轮次规则
        if (item == null || StringUtils.isEmpty(item.getRules())) {
            throw new BaseException("item为空或item没有规则...");
        }
        //否同
        if (Objects.equals(item.getRules(), VoteConstants.ITEM_RULE_AGER)) {
            List<Object[]> objects = userVoteItemRepository.agreeRule(item.getId());
            result.put("voteItems", this.getRuleList(item, objects, VoteConstants.ITEM_RULE_AGER));
            return result;
        }
        //排序
        if (Objects.equals(item.getRules(), VoteConstants.ITEM_RULE_ORDER)) {
            result.put("voteItems", this.getOrderRuleList(item));
            return result;
        }
        //打分规则
        if (Objects.equals(item.getRules(), VoteConstants.ITEM_RULE_SCORE)) {
            List<Object[]> objects = userVoteItemRepository.scoreRule(item.getId());
            result.put("voteItems", this.getRuleList(item, objects, VoteConstants.ITEM_RULE_SCORE));
            return result;
        }
        return result;
    }

    private List<VoteItem> getRuleList(Item item, List<Object[]> projections, String flag) {
        List<VoteItem> itemData = voteItemRepository.findByItem(item).get();
        if (itemData.size() == 0) {
            throw new BaseException("没有投票项...");
        }
        Long agreeRuleCount = null;
        double decimal = 0;
        if(Objects.equals(item.getRules(),VoteConstants.ITEM_RULE_AGER)){
            agreeRuleCount = userVoteItemRepository.countIp(item.getId());
            decimal = MarkToDecimalsUtil.transfer(item);
        }
        List<VoteItem> voteItems = new ArrayList<>();
        for (Object[] objects :
                projections) {
            VoteItem one = voteItemRepository.findOne(Long.parseLong(String.valueOf(objects[1])));
            if (Objects.equals(flag, VoteConstants.ITEM_RULE_AGER)) {
                one.setCurrentStatisticsNum(Integer.parseInt(String.valueOf(objects[0])));
                //确定这个投票项是否通过
                if(VoteItemPassRuleUtils.caculate(one.getCurrentStatisticsNum(),
                        decimal,
                        Integer.parseInt(String.valueOf(agreeRuleCount)))){
                    one.setAgreeRulePassFlag("1");
                }
            }
            if (Objects.equals(flag, VoteConstants.ITEM_RULE_SCORE)) {
                one.setCurrentStatisticsToalScore(Integer.parseInt(String.valueOf(objects[0])));
            }
            voteItems.add(one);
        }
        List<VoteItem> collect = itemData
                .stream()
                .filter(voteItem -> !voteItems.stream().anyMatch(vote -> (long) vote.getVoteItemId() == voteItem.getVoteItemId()))
                .collect(Collectors.toList());
        voteItems.addAll(collect);
        this.setVoteItemOrder(voteItems);
        return voteItems;
    }

    /**
     * 排序规则算法
     *
     * @param item
     * @return
     */
    private List<VoteItem> getOrderRuleList(Item item) {
        Optional<List<VoteItem>> byItem = voteItemRepository.findByItem(item);
        if (byItem.isPresent() && byItem.get().size() > 0) {
            for (VoteItem voteItem :
                    byItem.get()) {
                List<Integer> integers = userVoteItemRepository.orderRule(item.getId(), voteItem.getVoteItemId());
                voteItem.setCurrentStatisticsOrderScore(calculate(byItem.get().size(), integers));
            }
            Collections.sort(byItem.get(), (VoteItem o1, VoteItem o2) -> {
                //倒序
                return o2.getCurrentStatisticsOrderScore().compareTo(o1.getCurrentStatisticsOrderScore());
            });
        }
        List<VoteItem> voteItems = this.setVoteItemOrder(byItem.get());
        return voteItems;
    }

    private Integer calculate(Integer max, List<Integer> list) {
        int i = list.stream().mapToInt(integer -> max + 1 - integer).sum();
        return i;
    }

    private List<VoteItem> setVoteItemOrder(List<VoteItem> voteItem) {
        //设置起始值为1
        if(voteItem.size() > 0){
            voteItem.get(0).setVoteItemOrder(1);
        }
        for (int i = 1; i < voteItem.size(); i++) {
            int y = 0;
            if(voteItem.get(i).getCurrentStatisticsOrderScore() != null){
                y = voteItem.get(i).getCurrentStatisticsOrderScore().compareTo(voteItem.get(i - 1).getCurrentStatisticsOrderScore());
            }
            if(voteItem.get(i).getCurrentStatisticsNum() != null){
                y = voteItem.get(i).getCurrentStatisticsNum().compareTo(voteItem.get(i - 1).getCurrentStatisticsNum());
            }
            if(voteItem.get(i).getCurrentStatisticsToalScore() != null){
                y = voteItem.get(i).getCurrentStatisticsToalScore().compareTo(voteItem.get(i - 1).getCurrentStatisticsToalScore());
            }
            if(voteItem.get(i).getCurrentStatisticsToalScore() == null
                    && voteItem.get(i).getCurrentStatisticsNum() == null
                    && voteItem.get(i).getCurrentStatisticsOrderScore() == null){
                continue;
            }
            if(y == 0){
                voteItem.get(i).setVoteItemOrder(voteItem.get(i-1).getVoteItemOrder());
                continue;
            }
            voteItem.get(i).setVoteItemOrder(voteItem.get(i-1).getVoteItemOrder()+1);
        }
        return voteItem;
    }
}
