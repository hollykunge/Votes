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

    /**
     * 条件分页
     * @param page 当前页码
     * @param size 当前页数据行数
     * @param voteQuery
     * @return
     */
    PageBaseInfo<VoteItems> findVoteItemsCriteria(Integer page,Integer size,VoteItems voteQuery);

    /**
     * 无条件分页
     * @param page 当前页码
     * @param size 当前页数据行数
     * @return
     */
    PageBaseInfo<VoteItems> findVoteItems(Integer page,Integer size);

    /**
     * 查询邀请码对应投票轮
     * @param invitationCode 邀请码
     * @return
     */
    VoteItems findVoteItem(String invitationCode);
}
