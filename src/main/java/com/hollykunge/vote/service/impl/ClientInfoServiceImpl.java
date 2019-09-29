package com.hollykunge.vote.service.impl;

import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.service.ClientInfoService;
import com.hollykunge.vote.service.VoteItemsService;
import com.hollykunge.vote.vo.ClientInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 客户端
 * @author: Mr.Do
 * @create: 2019-09-29 16:30
 */
public class ClientInfoServiceImpl implements ClientInfoService {

    @Autowired
    VoteItemsService voteItemsService;

    @Override
    public ClientInfo getClientById(String id) {
        return null;
    }

    @Override
    public List<ClientInfo> getListById(String voteId) {
        return null;
    }

    @Override
    public VoteItems getVoteItem(String code) {
        return voteItemsService.findVoteItem(code);
    }
}
