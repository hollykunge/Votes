package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.vo.ClientInfo;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 客户投票端
 * @author: Mr.Do
 * @create: 2019-09-29 14:09
 */
public interface ClientInfoService {
    /**
     * 获取客户端
     * @param id
     * @return
     */
    ClientInfo getClientById(String id);

    /**
     * 获取所有已连接客户端
     * @param voteId 参与的投票id
     * @return
     */
    List<ClientInfo> getListById(String voteId);

    /**
     * 获取当轮投票
     * @param code 邀请码
     * @return
     */
    VoteItems getVoteItem(String code);
}
