package com.hollykunge.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @deprecation 用户投票项信息主表
 * @author zhhongyu
 * @since 2019-10-9
 */
@Entity
@Table(name = "user_vote_item")
public class UserVoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    /**
     * 1为同意 0为不同意
     */
    @Column(name = "agree_flag")
    private String agreeFlag;
    /**
     * 排序
     */
    @Column(name = "order_rule")
    private String order;
    /**
     * 分数
     */
    @Column(name = "score")
    private String score;

    @ManyToOne
    @JoinColumn(name = "vote_item_id", referencedColumnName = "id", nullable = false)
    private VoteItem voteItem;

    @ManyToOne
    @JoinColumn(name = "user_ip", referencedColumnName = "ip", nullable = false)
    private UserVoteIp userVoteIp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreeFlag() {
        return agreeFlag;
    }

    public void setAgreeFlag(String agreeFlag) {
        this.agreeFlag = agreeFlag;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public VoteItem getVoteItem() {
        return voteItem;
    }

    public void setVoteItem(VoteItem voteItem) {
        this.voteItem = voteItem;
    }

    public UserVoteIp getUserVoteIp() {
        return userVoteIp;
    }

    public void setUserVoteIp(UserVoteIp userVoteIp) {
        this.userVoteIp = userVoteIp;
    }
}
