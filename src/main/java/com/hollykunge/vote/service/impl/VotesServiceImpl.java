package com.hollykunge.vote.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hollykunge.vote.entity.Votes;
import com.hollykunge.vote.repository.VotesRepository;
import com.hollykunge.vote.service.VotesService;
import com.hollykunge.vote.utils.PageBaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 投票
 * @author: Mr.Do
 * @create: 2019-09-26 16:47
 */
@Service
public class VotesServiceImpl implements VotesService {

    @Autowired
    private VotesRepository votesRepository;

    @Override
    public void add(Votes votes) {
        votesRepository.save(votes);
    }

    @Override
    public void update(Votes votes) {
        votesRepository.save(votes);
    }

    @Override
    public void delete(String id) {
        votesRepository.deleteById(id);
    }

    /**
     * 条件查询
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @param voteQuery
     * @return
     */
    @Override
    public PageBaseInfo<Votes> findVoteCriteria(Integer pageNum, Integer pageSize, Votes voteQuery) {
        PageHelper.startPage(pageNum, pageSize);
        Example<Votes> ex = Example.of(voteQuery);
        List<Votes> list = votesRepository.findAll(ex);
        PageInfo<Votes> pageInfo = new PageInfo<Votes>(list);
        // 总条数
        int totalSize = (int) pageInfo.getTotal();
        int totalPage = pageInfo.getPages();
        return new PageBaseInfo(pageNum, pageSize, totalSize, totalPage, list);
    }

}
