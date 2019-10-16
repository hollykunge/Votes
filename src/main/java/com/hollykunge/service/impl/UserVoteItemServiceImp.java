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
            result.put("voteItems", this.getRuleList(objects, VoteConstants.ITEM_RULE_AGER));
            return result;
        }
        //排序
        if (Objects.equals(item.getRules(), VoteConstants.ITEM_RULE_ORDER)) {

        }
        //打分规则
        if (Objects.equals(item.getRules(), VoteConstants.ITEM_RULE_SCORE)) {
            List<Object[]> objects = userVoteItemRepository.scoreRule(item.getId());
            result.put("voteItems", this.getRuleList(objects, VoteConstants.ITEM_RULE_SCORE));
            return result;
        }
        return result;
    }

    private List<VoteItem> getRuleList(List<Object[]> projections, String flag) {
        List<VoteItem> voteItems = new ArrayList<>();
        for (Object[] objects:
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
        return voteItems;
    }
}
