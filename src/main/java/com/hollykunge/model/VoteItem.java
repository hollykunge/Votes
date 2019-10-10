package com.hollykunge.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
/**
 * @deprecation 被投票项
 * @author zhhongyu
 * @since 2019-10-9
 */
@Data
@Entity
@Table(name = "vote_item")
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long voteItemId;
    /**
     * 被投票项目名称
     */
    @Column(name = "name", nullable = false)
    @NotEmpty(message = "请输入被投票项名称")
    private String name;
    /**
     * 所在轮数
     */
    @Column(name = "turn_num", nullable = false)
    private String turnNum;
    /**
     * 是否进入下一轮(1为是，0为否)
     */
    @Column(name = "is_trun_next", nullable = false)
    private String isTrunNext;

    @ManyToOne
    @JoinColumn(name = "vote_id", referencedColumnName = "vote_id", nullable = false)
    @NotNull
    private Vote vote;
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

}
