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
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Date crtTime;
    @Column(nullable = false)
    private Date updTime;
    @Column(nullable = false)
    private String crtUser;
    @Column(nullable = false)
    private String updUser;
}
