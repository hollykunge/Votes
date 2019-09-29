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

    @Column(nullable = false)
    private String voteId;

    @Column(nullable = false)
    private int turnNum;

    @Column(nullable = false)
    private String invitationCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
