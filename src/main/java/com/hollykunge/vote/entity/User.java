package com.hollykunge.vote.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @program: Lark-Server
 * @description: 用户
 * @author: Mr.Do
 * @create: 2019-09-26 14:48
 */
@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private String id;
    @Column(columnDefinition = "varchar(11) COMMENT '用户名称'", nullable = false)
    private String username;
    @Column(columnDefinition = "varchar(11) COMMENT '密码'", nullable = false)
    private String password;
    @Column(columnDefinition = "datetime COMMENT '创建时间'", nullable = false)
    private Date crtTime;
    @Column(columnDefinition = "datetime COMMENT '更新时间'", nullable = false)
    private Date updTime;
    @Column(columnDefinition = "varchar(11) COMMENT '创建人id'", nullable = false)
    private String crtUser;
    @Column(columnDefinition = "varchar(11) COMMENT '更新人id'", nullable = false)
    private String updUser;
}
