package com.hollykunge.service.impl;

import com.hollykunge.model.Item;
import com.hollykunge.model.VoteItem;
import com.hollykunge.repository.VoteItemRepository;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.util.ExceptionCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:54 2019/10/9
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class VoteItemServiceImp implements VoteItemService {
    private final VoteItemRepository voteItemRepository;

    @Autowired
    public VoteItemServiceImp(VoteItemRepository voteItemRepository) {
        this.voteItemRepository = voteItemRepository;
    }

    @Override
    public void add(VoteItem voteItem)throws Exception {
        try {
            voteItemRepository.saveAndFlush(voteItem);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }

//    @Override
//    public Optional<List<VoteItem>> findByItemId(Item item) {
//        return voteItemRepository.findByItem(item);
//    }

    @Override
    public Optional<List<VoteItem>> findByItem(Item item) {
        Optional<List<VoteItem>> byItem = voteItemRepository.findByItem(item);
        if(!byItem.isPresent() && byItem.get().size() == 0){
            Collections.sort(byItem.get(),(voteItem1,voteItem2) ->{
                if(voteItem1.getParentStatisticsNum() != null && voteItem2.getParentStatisticsNum() != null){
                    return voteItem2.getParentStatisticsNum().compareTo(voteItem1.getCurrentStatisticsNum());
                }
                if(voteItem1.getCurrentStatisticsToalScore() != null && voteItem2.getCurrentStatisticsToalScore() != null){
                    return voteItem2.getCurrentStatisticsToalScore().compareTo(voteItem1.getCurrentStatisticsToalScore());
                }
                if(voteItem1.getParentStatisticsOrderScore() != null && voteItem2.getParentStatisticsOrderScore() != null){
                    return voteItem2.getParentStatisticsOrderScore().compareTo(voteItem1.getParentStatisticsOrderScore());
                }
                return 1;
            });
        }
        return byItem;
    }
    @Override
    public void deleteVoteItem(List<String> ids)throws Exception{
        if(ids.size() == 0){
            return;
        }
        try {
            for (String id:
                 ids) {
                voteItemRepository.delete(Long.valueOf(id));
            }
        }catch (Exception e){
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
    }
    @Override
    public void deleteByItem(Item item)throws Exception{
        try {
            voteItemRepository.deleteByItem(item);
        }catch (Exception e){
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }

}
