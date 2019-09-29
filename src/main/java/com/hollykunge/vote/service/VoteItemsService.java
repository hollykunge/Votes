package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.utils.PageBaseInfo;

/**
 * @program: Lark-Server
 * @description: 第几轮
 * @author: Mr.Do
 * @create: 2019-09-27 20:30
 */
public interface VoteItemsService {

    void add(VoteItems votes);

    void update(VoteItems votes);

    void delete(String id);

    PageBaseInfo<VoteItems> findVoteItemsCriteria(Integer page,Integer size,VoteItems voteQuery);

    PageBaseInfo<VoteItems> findVoteItems(Integer page,Integer size);
}
