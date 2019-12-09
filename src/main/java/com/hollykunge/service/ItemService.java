package com.hollykunge.service;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item)throws Exception;
    Item findById(Long id);
    Optional<Item> findByIdAndCode(Long id,String code);
    Item setItemStatus(Long id,String status) throws Exception;
    Item deleteItem (Long id)throws Exception;

    List<Item> findByPrevious(String previous);
    List<Item> findItemsByVote(Vote vote);
}
