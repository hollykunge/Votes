package com.hollykunge.service;

import com.hollykunge.model.Item;

import java.util.Optional;

public interface ItemService {

    Item save(Item item)throws Exception;

    Optional<Item> findById(Long id);
}
