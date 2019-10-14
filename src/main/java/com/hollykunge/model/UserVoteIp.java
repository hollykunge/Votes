package com.hollykunge.model;

import javax.persistence.*;

/**
 * @author: zhhongyu
 * @description: 用户投票ip地址记录
 * @since: Create in 16:52 2019/10/14
 */
@Entity
@Table(name = "user_vote_ip")
public class UserVoteIp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    /**
     * 一个ip只能投一个轮
     */
    @Column(name = "ip", nullable = false, unique = true)
    private String ip;
    /**
     * ip的投票状态 0为未投票，1为以投票完成
     */
    @Column(name = "status", nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
