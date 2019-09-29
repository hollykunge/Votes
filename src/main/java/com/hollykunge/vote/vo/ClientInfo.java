package com.hollykunge.vote.vo;

import lombok.Data;

/**
 * @program: Lark-Server
 * @description: 客户端会话
 * @author: Mr.Do
 * @create: 2019-09-29 14:12
 */
@Data
public class ClientInfo {
    //会话id
    private String id;
    //客户端ip
    private String ip;
    //会话时长
    private int timeStamp;
    //所属投票
    private String voteId;
}
