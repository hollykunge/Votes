package com.hollykunge.vote.service.impl;

import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.service.VoteItemsService;
import com.hollykunge.vote.utils.PageBaseInfo;
import org.springframework.stereotype.Service;

/**
 * @program: Lark-Server
 * @description: 第几轮
 * @author: Mr.Do
 * @create: 2019-09-27 20:31
 */
@Service
public class VoteItemsServiceImpl implements VoteItemsService {
    @Override
    public void add(VoteItems votes) {

    }

    @Override
    public void update(VoteItems votes) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageBaseInfo<VoteItems> findVoteItemsCriteria(Integer page, Integer size, VoteItems voteQuery) {
        return null;
    }

    @Override
    public PageBaseInfo<VoteItems> findVoteItems(Integer page, Integer size) {
        return null;
    }

    @Override
    public VoteItems findVoteItem(String invitationCode) {
        return null;
    }

}
