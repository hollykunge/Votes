package com.hollykunge.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/**
 * @deprecation 被投票项
 * @author zhhongyu
 * @since 2019-10-9
 */
@Entity
@Table(name = "vote_item")
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long voteItemId;
    /**
     * 为了映射excel表头，统一约定字段
     * 被投票项目名称
     */
    @Column(name = "attr0", nullable = false)
    @NotEmpty(message = "请输入被投票项名称")
    private String attr0;
    /**
     * 所在轮数
     */
    @Column(name = "turn_num", nullable = false)
    private String turnNum;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Item item;

    /**
     * 扩展字段1
     */
    @Column(name = "attr1")
    private String attr1;
    /**
     * 扩展字段2
     */
    @Column(name = "attr2")
    private String attr2;
    /**
     * 扩展字段3
     */
    @Column(name = "attr3")
    private String attr3;
    /**
     * 扩展字段4
     */
    @Column(name = "attr4")
    private String attr4;
    @Column(name = "attr5")
    private String attr5;

    @Column(name = "attr6")
    private String attr6;
    /**
     * 当前轮统计结果
     */
    @Column(name = "current_statistics_num")
    private Integer currentStatisticsNum;
    @Column(name = "current_statistics_toal_score")
    private Integer currentStatisticsToalScore;
    @Column(name = "current_statistics_order_score")
    private Integer currentStatisticsOrderScore;
    /**
     * 上一轮统计结果
     */
    @Column(name = "parent_statistics_num")
    private Integer parentStatisticsNum;
    @Column(name = "parent_statistics_toal_score")
    private Integer parentStatisticsToalScore;
    @Column(name = "parent_statistics_order_score")
    private Integer parentStatisticsOrderScore;
    /**
     * 排序
     */
    @Column(name = "vote_item_order")
    private Integer voteItemOrder;
    /**
     * 否同规则的时候，确定该投票项是否按确定的通过率通过标识
     * 1为通过，0为不通过
     */
    @Column(name = "agree_rule_pass_flag")
    private String agreeRulePassFlag;

    public String getAgreeRulePassFlag() {
        return agreeRulePassFlag;
    }

    public void setAgreeRulePassFlag(String agreeRulePassFlag) {
        this.agreeRulePassFlag = agreeRulePassFlag;
    }

    public Integer getCurrentStatisticsNum() {
        return currentStatisticsNum;
    }

    public void setCurrentStatisticsNum(Integer currentStatisticsNum) {
        this.currentStatisticsNum = currentStatisticsNum;
    }

    public Integer getCurrentStatisticsToalScore() {
        return currentStatisticsToalScore;
    }

    public void setCurrentStatisticsToalScore(Integer currentStatisticsToalScore) {
        this.currentStatisticsToalScore = currentStatisticsToalScore;
    }

    public Integer getParentStatisticsNum() {
        return parentStatisticsNum;
    }

    public void setParentStatisticsNum(Integer parentStatisticsNum) {
        this.parentStatisticsNum = parentStatisticsNum;
    }

    public Integer getParentStatisticsToalScore() {
        return parentStatisticsToalScore;
    }

    public void setParentStatisticsToalScore(Integer parentStatisticsToalScore) {
        this.parentStatisticsToalScore = parentStatisticsToalScore;
    }

    public Integer getVoteItemOrder() {
        return voteItemOrder;
    }

    public void setVoteItemOrder(Integer voteItemOrder) {
        this.voteItemOrder = voteItemOrder;
    }


    public Integer getCurrentStatisticsOrderScore() {
        return currentStatisticsOrderScore;
    }

    public void setCurrentStatisticsOrderScore(Integer currentStatisticsOrderScore) {
        this.currentStatisticsOrderScore = currentStatisticsOrderScore;
    }

    public Integer getParentStatisticsOrderScore() {
        return parentStatisticsOrderScore;
    }

    public void setParentStatisticsOrderScore(Integer parentStatisticsOrderScore) {
        this.parentStatisticsOrderScore = parentStatisticsOrderScore;
    }

    public String getAttr6() {
        return attr6;
    }

    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public Long getVoteItemId() {
        return voteItemId;
    }

    public void setVoteItemId(Long voteItemId) {
        this.voteItemId = voteItemId;
    }

    public String getAttr0() {
        return attr0;
    }

    public void setAttr0(String attr0) {
        this.attr0 = attr0;
    }

    public String getTurnNum() {
        return turnNum;
    }

    public void setTurnNum(String turnNum) {
        this.turnNum = turnNum;
    }


    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    public String getAttr4() {
        return attr4;
    }

    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
