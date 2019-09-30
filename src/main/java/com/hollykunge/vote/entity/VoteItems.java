package com.hollykunge.vote.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: Lark-Server
 * @description: 投票内容
 * @author: Mr.Do
 * @create: 2019-09-27 19:29
 */
@Data
@Entity
public class VoteItems implements Serializable {
    @Id
    @GeneratedValue
    private String id;

    @Column(columnDefinition = "varchar(11) COMMENT '投票id'", nullable = false)
    private String voteId;

    @Column(columnDefinition = "varchar(500) COMMENT '规则'", nullable = false)
    private String roles;

    @Column(columnDefinition = "varchar(500) COMMENT '提示'", nullable = false)
    private String tips;

    @Column(columnDefinition = "varchar(255) COMMENT '范围'", nullable = false)
    private String scope;

    @Column(columnDefinition = "varchar(255) COMMENT '规则：AO-否同，RANK-排序'", nullable = false)
    private String regulation;

    @Column(columnDefinition = "int(6) COMMENT '第几轮'", nullable = false)
    private int turnNum;

    @Column(columnDefinition = "varchar(11) COMMENT '上一轮id'", nullable = false)
    private String previousId;

    @Column(columnDefinition = "varchar(11) COMMENT '邀请码'", nullable = false)
    private String invitationCode;

    @Column(nullable = false, columnDefinition = "text COMMENT '投票内容'")
    private String content;
}
