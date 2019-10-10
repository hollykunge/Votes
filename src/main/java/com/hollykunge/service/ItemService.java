package com.hollykunge.service;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item)throws Exception;
    Item findById(Long id);
    Item setItemStatus(Item item) throws Exception;
//    List<Item> findItemsByVote(Vote vote);
}
