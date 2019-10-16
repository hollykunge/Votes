package com.hollykunge.service.impl;

import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;
import com.hollykunge.repository.UserVoteItemRepository;
import com.hollykunge.service.UserVoteItemService;
import com.hollykunge.util.ExceptionCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:05 2019/10/15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserVoteItemServiceImp implements UserVoteItemService {
    @Autowired
    private UserVoteItemRepository userVoteItemRepository;

    @Override
    public UserVoteItem add(UserVoteItem userVoteItem) {
        try{
            userVoteItemRepository.saveAndFlush(userVoteItem);
        }catch (Exception e){
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        return userVoteItem;
    }

    @Override
    public List<UserVoteItem> findByUserIp(String ip) {
        return userVoteItemRepository.findByIp(ip);
    }
    @Override
    public List<UserVoteItem> findByItemAndIp(Item item, String ip) {
        return userVoteItemRepository.findByItemAndIp(item,ip);
    }
}
