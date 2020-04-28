package com.hollykunge.model;

/**
 * @program: Lark-Server
 * @description: 投票日志
 * @author: Mr.Do
 * @create: 2020-04-28 13:44
 */
@Entity
@Table(name = "vote_log")
public class VoteLog {

    @Id
    @Column(name = "id")
    private Long id;
    /**
     * 身份证
     */
    @Column(name = "log_user_id", nullable = false)
    private String logUserId;
    /**
     * 是谁
     */
    @Column(name = "log_user", nullable = false)
    private String logUser;
    /**
     * 什么操作
     */
    @Column(name = "log_method", nullable = false)
    private String logMethod;
    /**
     * 什么东西
     */
    @Column(name = "log_object", nullable = false)
    private String logObject;
    /**
     * 什么内容
     */
    @Column(name = "log_content", nullable = false)
    private String logContent;
    /**
     * 成功了吗
     */
    @Column(name = "has_success", nullable = false)
    private String hasSuccess;
}
