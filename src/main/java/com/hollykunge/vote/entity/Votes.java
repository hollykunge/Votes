package com.hollykunge.vote.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: Lark-Server
 * @description: 投票项
 * @author: Mr.Do
 * @create: 2019-09-24 15:13
 */
@Data
@Entity
public class Votes implements Serializable {
    @Id
    @GeneratedValue
    private String id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Date crtTime;
    @Column(nullable = false)
    private Date updTime;
    @Column(nullable = false)
    private String crtUser;
    @Column(nullable = false)
    private String updUser;

    /**
     * 一共有几轮
     */
    @Column(nullable = false)
    private Integer turnsSize;

    /**
     * 规则：AO-否同，RANK-排序
     */
    @Column(nullable = false)
    private String regulation;

    /**
     * 范围
     */
    @Column(nullable = false)
    private String scope;
    @Column(nullable = false)
    private String description;


}
