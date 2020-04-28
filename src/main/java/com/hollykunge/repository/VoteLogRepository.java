package com.hollykunge.repository;

import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteLog;

import java.util.Optional;

/**
 * @program: Lark-Server
 * @description: rizhi
 * @author: Mr.Do
 * @create: 2020-04-28 13:57
 */
public interface VoteLogRepository extends JpaRepository<VoteLog, Long> {
    Page<VoteLog> findByUserOrderByCreateDateDesc(User user, Pageable pageable);

    Page<VoteLog> findAllByOrderByCreateDateDesc(Pageable pageable);

    Optional<VoteLog> findById(Long id);

    VoteLog save(VoteLog voteLog);
}
