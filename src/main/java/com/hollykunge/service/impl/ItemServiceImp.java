package com.hollykunge.service.impl;

import com.hollykunge.constants.VoteConstants;
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

import java.util.Comparator;
import java.util.List;

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
            //状态为保存
            item.setStatus("1");
            //创建的b不是第一轮投票
            Item maxItem = null;
            if(itemsTemp.size() > 0){
                //添加的时候，查询上一轮次的item_id
                maxItem = itemsTemp.stream().max(new Comparator<Item>() {
                    @Override
                    public int compare(Item o1, Item o2) {
                        return Integer.compare(o1.getTurnNum(), o2.getTurnNum());
                    }
                }).get();
                item.setPreviousId(String.valueOf(maxItem.getId()));
            }
            //上轮投票是否是已结束状态
            if(!itemSending(maxItem)){
                throw new BaseException("上一轮没有结束，不能再创建投票轮");
            }
        }
        //设置随机码，防止用户窜改地址
        if (StringUtils.isEmpty(item.getCode())){
            item.setCode(UUIDUtils.getUUID());
        }
        return itemRepository.saveAndFlush(item);
    }

    private boolean itemSending(Item item){
        if(item == null||VoteConstants.ITEM_FINAL_STATUS.equals(item.getStatus())){
            return true;
        }
        return false;
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id);
    }
    @Override
    public Item setItemStatus(Long id,String status) throws Exception{
        Item exitItem = itemRepository.findOne(id);
        if(exitItem == null){
            throw new BaseException("暂无该伦次...");
        }
        exitItem.setStatus(status);
        itemRepository.saveAndFlush(exitItem);
        return exitItem;
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

    @Override
    public Item deleteItem(Long id) {
        Item one = itemRepository.findOne(id);
        if(one == null){
            throw new BaseException("查询不到这个数据...没法删除");
        }
        itemRepository.delete(id);
        return one;
    }
    @Override
    public List<Item> findByPrevious(String previous){
       return itemRepository.findByPreviousId(previous);
    }

    @Override
    public List<Item> findItemsByVote(Vote vote) {
        return itemRepository.findByVote(vote);
    }
}
