package com.hollykunge.constants;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 17:30 2019/10/10
 */
public class VoteConstants {
    public static final String AGREEMENT_LETTER = "http://";
    public static final String INVITECODE_RPC = "/userVote/";

    /**
     * 投票轮新建状态
     */
    public static final String ITEM_ADD_STATUS = "1";
    /**
     * 投票轮已发布状态
     */
    public static final String ITEM_SEND_STATUS = "2";
    /**
     * 投票轮结束状态
     */
    public static final String ITEM_FINAL_STATUS = "3";

    public static final String DEFUALT_CLIENTIP = "127.0.0.1";
    /**
     * 用户一个ip已完成投票标识
     */
    public static final String USER_IP_VOTE_FINAL_FLAG = "1";
    /**
     * 否同规则
     */
    public static final String ITEM_RULE_AGER = "1";
    /**
     * 排序
     */
    public static final String ITEM_RULE_ORDER = "2";
    /**
     * 打分
     */
    public static final String ITEM_RULE_SCORE = "3";

    public static final String EXTAPIHEAD = "extapihead";
    /**
     * 确认投票项通过的百分比
     */
    public static final String VOTE_PASS_PERSENT = "2/3";
}