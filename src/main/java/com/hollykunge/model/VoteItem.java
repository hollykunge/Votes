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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
