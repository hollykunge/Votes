package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.User;
import com.hollykunge.vote.entity.Votes;
import com.hollykunge.vote.utils.PageBaseInfo;
import org.springframework.data.domain.Page;

/**
 * @program: Lark-Server
 * @description: 投票
 * @author: Mr.Do
 * @create: 2019-09-26 16:47
 */
public interface VotesService {

    void add(Votes votes);

    void update(Votes votes);

    void delete(String id);

    PageBaseInfo<Votes> findVoteCriteria(Integer page,Integer size,Votes voteQuery);
}
