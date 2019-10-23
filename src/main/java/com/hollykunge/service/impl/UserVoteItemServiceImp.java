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
        List<VoteItem> voteItems = new ArrayList<>();
        for (Object[] objects :
                projections) {
            VoteItem one = voteItemRepository.findOne(Long.parseLong(String.valueOf(objects[1])));
            if (Objects.equals(flag, VoteConstants.ITEM_RULE_AGER)) {
                one.setStatisticsNum(String.valueOf(objects[0]));
            }
            if (Objects.equals(flag, VoteConstants.ITEM_RULE_SCORE)) {
                one.setStatisticsToalScore(String.valueOf(objects[0]));
            }
            voteItems.add(one);
        }
        List<VoteItem> collect = itemData
                .stream()
                .filter(voteItem -> !voteItems.stream().anyMatch(vote -> (long)vote.getVoteItemId() == voteItem.getVoteItemId()))
                .collect(Collectors.toList());
        voteItems.addAll(collect);
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
                Integer max = userVoteItemRepository.orderRuleMaxScore(item.getId(), voteItem.getVoteItemId());
                voteItem.setStatisticsOrderScore(calculate(max, integers));
            }
            Collections.sort(byItem.get(), new Comparator<VoteItem>() {
                @Override
                public int compare(VoteItem o1, VoteItem o2) {
                    //倒序
                    return o2.getStatisticsOrderScore().compareTo(o1.getStatisticsOrderScore());
                }
            });
        }
        return byItem.get();
    }

    private Integer calculate(Integer max, List<Integer> list) {
        int i = list.stream().mapToInt(integer -> integer * (max + 1 - integer)).sum();
        return i;
    }
}
