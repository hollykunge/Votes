package com.hollykunge.service;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    Optional<Item> findById(Long id);

    List<Item> findItemsByVote(Vote vote);
}
