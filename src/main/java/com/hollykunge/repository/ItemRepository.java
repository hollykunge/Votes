package com.hollykunge.repository;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findById(Long id);
    List<Item> findByVote(Vote vote);
    Optional<Item> findByIdAndCode(Long id,String code);
    List<Item> findByPreviousId(String previousId);
}
