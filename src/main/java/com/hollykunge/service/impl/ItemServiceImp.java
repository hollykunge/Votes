package com.hollykunge.service.impl;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.repository.ItemRepository;
import com.hollykunge.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImp(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findItemsByVote(Vote vote){
        return itemRepository.findItemsByVote(vote);
    }
}
