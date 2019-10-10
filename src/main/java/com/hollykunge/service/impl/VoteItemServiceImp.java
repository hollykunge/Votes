package com.hollykunge.service.impl;

import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteItem;
import com.hollykunge.repository.VoteItemRepository;
import com.hollykunge.repository.VoteRepository;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.util.ExceptionCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:54 2019/10/9
 */
@Service
@Slf4j
public class VoteItemServiceImp implements VoteItemService {
    private final VoteItemRepository voteItemRepository;

    @Autowired
    public VoteItemServiceImp(VoteItemRepository voteItemRepository) {
        this.voteItemRepository = voteItemRepository;
    }

    @Override
    public void add(VoteItem voteItem) {
        try {
            voteItemRepository.saveAndFlush(voteItem);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }

    @Override
    public Optional<List<VoteItem>> findByVoteId(Vote vote) {
        return voteItemRepository.findByVote(vote);
    }
}
