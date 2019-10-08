package com.hollykunge.repository;

import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Page<Vote> findByUserOrderByCreateDateDesc(User user, Pageable pageable);

    Page<Vote> findAllByOrderByCreateDateDesc(Pageable pageable);

    Optional<Vote> findById(Long id);
}
