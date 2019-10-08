package com.hollykunge.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "item")
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "vote_id", referencedColumnName = "vote_id", nullable = false)
    @NotNull
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull
    private User user;
}
