package com.hollykunge.service.impl;

import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import com.hollykunge.repository.VoteRepository;
import com.hollykunge.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteServiceImp implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImp(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public Optional<Vote> findForId(Long id) {
        return voteRepository.findById(id);
    }

    @Override
    public Vote save(Vote vote) {
        return voteRepository.saveAndFlush(vote);
    }

    @Override
    public Page<Vote> findByUserOrderedByDatePageable(User user, int page) {
        return voteRepository.findByUserOrderByCreateDateDesc(user, new PageRequest(subtractPageByOne(page), 5));
    }

    @Override
    public Page<Vote> findAllOrderedByDatePageable(int page) {
        return voteRepository.findAllByOrderByCreateDateDesc(new PageRequest(subtractPageByOne(page), 5));
    }

    @Override
    public void delete(Vote vote) {
        voteRepository.delete(vote);
    }

    private int subtractPageByOne(int page){
        return (page < 1) ? 0 : page - 1;
    }
}
