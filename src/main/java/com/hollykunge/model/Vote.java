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
 * @author dd
 */
@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vote_id")
    private Long id;

    @Column(name = "title", nullable = false)
    @Length(min = 2, message = "输入至少2个字符")
    @NotEmpty(message = "请输入标题")
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "excel_header")
    private String excelHeader;

    @Column(columnDefinition = "int(6) COMMENT '预计投票人数'")
    private Integer memberSize;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @NotNull
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public String getExcelHeader() {
        return excelHeader;
    }

    public void setExcelHeader(String excelHeader) {
        this.excelHeader = excelHeader;
    }

    public Integer getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(Integer memberSize) {
        this.memberSize = memberSize;
    }

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    @OrderBy("turn_num ASC")
    private Collection<Item> items;
}
