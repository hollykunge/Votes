package com.hollykunge.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private Integer order;
    /**
     * 分数
     */
    @Column(name = "score")
    private Integer score;

    @ManyToOne
    @JoinColumn(name = "vote_item_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private VoteItem voteItem;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Item item;


    @Column(name = "user_ip")
    private String ip;


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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public VoteItem getVoteItem() {
        return voteItem;
    }

    public void setVoteItem(VoteItem voteItem) {
        this.voteItem = voteItem;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
