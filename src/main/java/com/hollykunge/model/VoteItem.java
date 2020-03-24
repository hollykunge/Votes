package com.hollykunge.model;

import org.hibernate.validator.constraints.Length;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "vote_item_seq")
    @SequenceGenerator(name = "vote_item_seq",sequenceName = "VOTE_ITEM_SEQ",initialValue = 2,allocationSize = 1)
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
    @Column(name = "attr1", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr1;
    /**
     * 扩展字段2
     */
    @Column(name = "attr2", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr2;
    /**
     * 扩展字段3
     */
    @Column(name = "attr3", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr3;
    /**
     * 扩展字段4
     */
    @Column(name = "attr4", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr4;
    @Column(name = "attr5", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr5;

    @Column(name = "attr6", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr6;
    @Column(name = "attr7", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr7;
    @Column(name = "attr8", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr8;
    @Column(name = "attr9", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr9;
    @Column(name = "attr10", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr10;
    @Column(name = "attr11", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr11;
    @Column(name = "attr12", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr12;
    @Column(name = "attr13", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr13;
    @Column(name = "attr14", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr14;
    @Column(name = "attr15", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr15;
    @Column(name = "attr16", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr16;
    @Column(name = "attr17", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr17;
    @Column(name = "attr18", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr18;
    @Column(name = "attr19", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr19;
    @Column(name = "attr20", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr20;
    @Column(name = "attr21", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr21;
    @Column(name = "attr22", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr22;
    @Column(name = "attr23", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr23;
    @Column(name = "attr24", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr24;
    @Column(name = "attr25", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr25;
    @Column(name = "attr26", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr26;
    @Column(name = "attr27", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr27;
    @Column(name = "attr28", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr28;
    @Column(name = "attr29", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr29;
    @Column(name = "attr30", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr30;
    @Column(name = "attr31", columnDefinition = "varchar2(1000)")
    @Length(max = 300, message = "最多300个字符")
    private String attr31;
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

    public String getAttr7() {
        return attr7;
    }

    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    public String getAttr8() {
        return attr8;
    }

    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    public String getAttr9() {
        return attr9;
    }

    public void setAttr9(String attr9) {
        this.attr9 = attr9;
    }

    public String getAttr10() {
        return attr10;
    }

    public void setAttr10(String attr10) {
        this.attr10 = attr10;
    }

    public String getAttr11() {
        return attr11;
    }

    public void setAttr11(String attr11) {
        this.attr11 = attr11;
    }

    public String getAttr12() {
        return attr12;
    }

    public void setAttr12(String attr12) {
        this.attr12 = attr12;
    }

    public String getAttr13() {
        return attr13;
    }

    public void setAttr13(String attr13) {
        this.attr13 = attr13;
    }

    public String getAttr14() {
        return attr14;
    }

    public void setAttr14(String attr14) {
        this.attr14 = attr14;
    }

    public String getAttr15() {
        return attr15;
    }

    public void setAttr15(String attr15) {
        this.attr15 = attr15;
    }

    public String getAttr16() {
        return attr16;
    }

    public void setAttr16(String attr16) {
        this.attr16 = attr16;
    }

    public String getAttr17() {
        return attr17;
    }

    public void setAttr17(String attr17) {
        this.attr17 = attr17;
    }

    public String getAttr18() {
        return attr18;
    }

    public void setAttr18(String attr18) {
        this.attr18 = attr18;
    }

    public String getAttr19() {
        return attr19;
    }

    public void setAttr19(String attr19) {
        this.attr19 = attr19;
    }

    public String getAttr20() {
        return attr20;
    }

    public void setAttr20(String attr20) {
        this.attr20 = attr20;
    }

    public String getAttr21() {
        return attr21;
    }

    public void setAttr21(String attr21) {
        this.attr21 = attr21;
    }

    public String getAttr22() {
        return attr22;
    }

    public void setAttr22(String attr22) {
        this.attr22 = attr22;
    }

    public String getAttr23() {
        return attr23;
    }

    public void setAttr23(String attr23) {
        this.attr23 = attr23;
    }

    public String getAttr24() {
        return attr24;
    }

    public void setAttr24(String attr24) {
        this.attr24 = attr24;
    }

    public String getAttr25() {
        return attr25;
    }

    public void setAttr25(String attr25) {
        this.attr25 = attr25;
    }

    public String getAttr26() {
        return attr26;
    }

    public void setAttr26(String attr26) {
        this.attr26 = attr26;
    }

    public String getAttr27() {
        return attr27;
    }

    public void setAttr27(String attr27) {
        this.attr27 = attr27;
    }

    public String getAttr28() {
        return attr28;
    }

    public void setAttr28(String attr28) {
        this.attr28 = attr28;
    }

    public String getAttr29() {
        return attr29;
    }

    public void setAttr29(String attr29) {
        this.attr29 = attr29;
    }

    public String getAttr30() {
        return attr30;
    }

    public void setAttr30(String attr30) {
        this.attr30 = attr30;
    }

    public String getAttr31() {
        return attr31;
    }

    public void setAttr31(String attr31) {
        this.attr31 = attr31;
    }
}
