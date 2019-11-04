package com.hollykunge.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    public Integer getTurnNum() {
        return turnNum;
    }

    public void setTurnNum(Integer turnNum) {
        this.turnNum = turnNum;
    }

    @Column(name = "turn_num", columnDefinition = "int(6) COMMENT '投票第几轮'")
    private Integer turnNum;

    @Column(columnDefinition = "int(6) COMMENT '预计投票人数'")
    private Integer memberSize;

    @Column(columnDefinition = "int(6) COMMENT '投票人数'")
    private Integer memberNum;

    @Column(columnDefinition = "varchar(11) COMMENT '上一轮id'")
    private String previousId;

    @Column(columnDefinition = "varchar(32) COMMENT '邀请码'")
    private String code;

    @Column(columnDefinition = "text COMMENT '规则内容'")
    private String content;
    /**
     * 规则范围（1否同2排序3打分）
     */
    @Column(name = "rules")
    @NotEmpty(message = "必填项不能为空")
    private String rules;
    /**
     * 1为同意 0为否决
     */
    @Column(name = "agree_rule")
    private String agreeRule;
    /**
     * 否同最大范围
     */
    @Column(name = "agree_max")
    private String agreeMax;
    /**
     * 否同最小范围
     */
    @Column(name = "agree_min")
    private String agreeMin;
    /**
     * 同意规则，进入下一轮的百分比
     */
    @Column(name = "agree_pass_persent")
    private String agreePassPersent;
    /**
     * 说明
     */
    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createDate;
    /**
     * 最大分数
     */
    @Column(name = "max_score")
    private String maxScore;
    /**
     * 最小分数
     */
    @Column(name = "min_score")
    private String minScore;

    /**
     * 投票轮状态0为无效，1为保存，2为发起，3为结束
     */
    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "vote_id", referencedColumnName = "vote_id", nullable = false)
    @NotNull
    private Vote vote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPreviousId() {
        return previousId;
    }

    public void setPreviousId(String previousId) {
        this.previousId = previousId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull
    private User user;

    public Integer getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(Integer memberSize) {
        this.memberSize = memberSize;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getAgreeRule() {
        return agreeRule;
    }

    public void setAgreeRule(String agreeRule) {
        this.agreeRule = agreeRule;
    }

    public String getAgreeMax() {
        return agreeMax;
    }

    public void setAgreeMax(String agreeMax) {
        this.agreeMax = agreeMax;
    }

    public String getAgreeMin() {
        return agreeMin;
    }

    public void setAgreeMin(String agreeMin) {
        this.agreeMin = agreeMin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(String maxScore) {
        this.maxScore = maxScore;
    }

    public String getMinScore() {
        return minScore;
    }

    public void setMinScore(String minScore) {
        this.minScore = minScore;
    }

    public String getAgreePassPersent() {
        return agreePassPersent;
    }

    public void setAgreePassPersent(String agreePassPersent) {
        this.agreePassPersent = agreePassPersent;
    }
}
