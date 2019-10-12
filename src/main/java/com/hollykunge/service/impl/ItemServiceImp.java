package com.hollykunge.service.impl;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.repository.ItemRepository;
import com.hollykunge.service.ItemService;
import com.hollykunge.util.UUIDUtils;
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
        if(item.getVote() == null){
            throw new BaseException("设置投票不能为空...");
        }
        List<Item> itemsTemp = itemRepository.findByVote(item.getVote());
        if(StringUtils.isEmpty(item.getId())){
            item.setTurnNum(itemsTemp.size()+1);
            item.setMemberNum(0);
        }
        //设置随机码，防止用户窜改地址
        item.setCode(UUIDUtils.getUUID());
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id);
    }
    @Override
    public Item setItemStatus(Item item) throws Exception{
        Item result = null;
        if(jurageItem(item)){
            Item exitItem = itemRepository.findOne(item.getId());
            exitItem.setStatus(item.getStatus());
            result = itemRepository.saveAndFlush(exitItem);
        }
        return result;
    }

    @Override
    public Optional<Item> findByIdAndCode(Long id,String code) {
        return itemRepository.findByIdAndCode(id,code);
    }

    private boolean jurageItem(Item item){
        if(item == null){
            throw new BaseException("参数不能为空...");
        }
        if(StringUtils.isEmpty(item.getId())){
            throw new BaseException("id不能为空...");
        }
        if(StringUtils.isEmpty(item.getStatus())){
            throw new BaseException("状态不能为空...");
        }
        return true;
    }
}
