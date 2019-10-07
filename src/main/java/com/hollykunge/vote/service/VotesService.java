package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.User;
import com.hollykunge.vote.entity.Votes;
import com.hollykunge.vote.utils.PageBaseInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 投票
 * @author: Mr.Do
 * @create: 2019-09-26 16:47
 */
public interface VotesService {

    /**
     * 添加投票
     * @param votes
     */
    void add(Votes votes);

    /**
     * 更新投票
     * @param votes
     */
    void update(Votes votes);

    /**
     * 删除投票
     * @param id
     */
    void delete(String id);

    /**
     * 条件分页
     * @param page
     * @param size
     * @param voteQuery
     * @return
     */
    PageBaseInfo<Votes> findVoteCriteria(Integer page,Integer size,Votes voteQuery);

    /**
     * 根据用户id查询投票
     * @param id 用户id
     * @return
     */
    List<Votes> findVotesByUserId();
}
