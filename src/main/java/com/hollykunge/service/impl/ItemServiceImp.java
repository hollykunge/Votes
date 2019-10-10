package com.hollykunge.service.impl;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.repository.ItemRepository;
import com.hollykunge.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional(rollbackFor = Exception.class)
@Service
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImp(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) throws Exception{
        if(StringUtils.isEmpty(item.getAgreeMax())||StringUtils.isEmpty(item.getAgreeMin())){
            throw new BaseException("规则最大范围最小范围不能为空...");
        }
        if(item.getVote() == null){
            throw new BaseException("设置投票不能为空...");
        }
        List<Item> itemsTemp = itemRepository.findByVote(item.getVote());
        item.setTurnNum(String.valueOf(itemsTemp.size()+1));
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }
}
