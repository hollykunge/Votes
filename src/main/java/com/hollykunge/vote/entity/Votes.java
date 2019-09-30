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
 * @description: 投票
 * @author: Mr.Do
 * @create: 2019-09-24 15:13
 */
@Data
@Entity
public class Votes implements Serializable {
    @Id
    @GeneratedValue
    private String id;

    @Column(columnDefinition = "varchar(255) COMMENT '投票标题'", nullable = false)
    private String title;
    @Column(columnDefinition = "datetime COMMENT '创建时间'", nullable = false)
    private Date crtTime;
    @Column(columnDefinition = "datetime COMMENT '更新时间'", nullable = false)
    private Date updTime;
    @Column(columnDefinition = "varchar(11) COMMENT '创建人id'", nullable = false)
    private String crtUser;
    @Column(columnDefinition = "varchar(11) COMMENT '更新人id'", nullable = false)
    private String updUser;
    @Column(columnDefinition = "int(6) COMMENT '共有轮数'", nullable = false)
    private Integer turnsSize;

    @Column(columnDefinition = "varchar(2000) COMMENT '描述'", nullable = true)
    private String description;


}
