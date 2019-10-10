package com.hollykunge.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "body", columnDefinition = "TEXT")
    @NotEmpty(message = "请输入注意事项")
    private String body;

    @Column(name = "turnNum")
    private String turnNum;

    @Column(columnDefinition = "int(6) COMMENT '预计投票人数'")
    private String memberSize;

    @Column(columnDefinition = "int(6) COMMENT '投票人数'")
    private String memberNum;

    @Column(columnDefinition = "varchar(11) COMMENT '上一轮id'")
    private String previousId;

    @Column(columnDefinition = "varchar(11) COMMENT '邀请码'")
    private String code;

    @Column(columnDefinition = "text COMMENT '投票内容'")
    private String content;
    /**
     * 规则范围（1否同2排序3打分）
     */
    @Column(name = "rules")
    private String rules;
    /**
     * 1为同意 0为否决
     */
    @Column(name = "agreeRule")
    private String agreeRule;
    /**
     * 否同最大范围
     */
    @Column(name = "agreeMax")
    private String agreeMax;
    /**
     * 否同最小范围
     */
    @Column(name = "agreeMin")
    private String agreeMin;
    /**
     * 说明
     */
    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createDate;

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

    public String getTurnNum() {
        return turnNum;
    }

    public void setTurnNum(String turnNum) {
        this.turnNum = turnNum;
    }

    public String getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(String memberSize) {
        this.memberSize = memberSize;
    }

    public String getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(String memberNum) {
        this.memberNum = memberNum;
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
}
