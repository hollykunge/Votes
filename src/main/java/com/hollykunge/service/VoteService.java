package com.hollykunge.service;

import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface VoteService {

    Optional<Vote> findForId(Long id);

    Vote save(Vote vote);

    Page<Vote> findByUserOrderedByDatePageable(User user, int page);

    Page<Vote> findAllOrderedByDatePageable(int page);

    void delete(Vote vote);

    Vote updateById(Vote vote) throws Exception;
}
