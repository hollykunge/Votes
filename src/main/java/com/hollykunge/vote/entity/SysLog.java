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
 * @description: 日志
 * @author: Mr.Do
 * @create: 2019-09-30 11:11
 */
@Data
@Entity
public class SysLog implements Serializable {

    private static final long serialVersionUID = -6309732882044872298L;

    @Id
    @GeneratedValue
    private Integer id;
    @Column(columnDefinition = "varchar(11) COMMENT '用户名'", nullable = false)
    private String crtUser;
    @Column(columnDefinition = "varchar(255) COMMENT '操作'", nullable = false)
    private String operation;
    @Column(columnDefinition = "varchar(255) COMMENT '方法'", nullable = false)
    private String method;
    @Column(columnDefinition = "varchar(1000) COMMENT '参数'", nullable = false)
    private String params;
    @Column(columnDefinition = "varchar(255) COMMENT 'ip'", nullable = false)
    private String ip;
    @Column(columnDefinition = "datetime COMMENT '创建时间'", nullable = false)
    private Date crtTime;
}
