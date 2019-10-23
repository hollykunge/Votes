package com.hollykunge.vo;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 15:44 2019/10/23
 */
public class VoteItemVO {
    private Long voteItemId;
    /**
     * 为了映射excel表头，统一约定字段
     * 被投票项目名称
     */
    private String attr0;
    /**
     * 所在轮数
     */
    private String turnNum;
    /**
     * 扩展字段1
     */
    private String attr1;
    /**
     * 扩展字段2
     */
    private String attr2;
    /**
     * 扩展字段3
     */
    private String attr3;
    /**
     * 扩展字段4
     */
    private String attr4;
    private String attr5;

    private String attr6;
    private String statisticsNum;
    private String statisticsToalScore;
    private Integer statisticsOrderScore;

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

    public String getAttr5() {
        return attr5;
    }

    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    public String getAttr6() {
        return attr6;
    }

    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    public String getStatisticsNum() {
        return statisticsNum;
    }

    public void setStatisticsNum(String statisticsNum) {
        this.statisticsNum = statisticsNum;
    }

    public String getStatisticsToalScore() {
        return statisticsToalScore;
    }

    public void setStatisticsToalScore(String statisticsToalScore) {
        this.statisticsToalScore = statisticsToalScore;
    }

    public Integer getStatisticsOrderScore() {
        return statisticsOrderScore;
    }

    public void setStatisticsOrderScore(Integer statisticsOrderScore) {
        this.statisticsOrderScore = statisticsOrderScore;
    }
}
