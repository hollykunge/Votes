package com.hollykunge.service;

import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;

import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:04 2019/10/15
 */
public interface UserVoteItemService {
    UserVoteItem add(UserVoteItem userVoteItem);

    List<UserVoteItem> findByUserIp(String ip);

    List<UserVoteItem> findByItemAndIp(Item item, String ip);
}
