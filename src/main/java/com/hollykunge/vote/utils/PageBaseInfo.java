package com.hollykunge.vote.utils;

import lombok.Data;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 分页
 * @author: Mr.Do
 * @create: 2019-09-27 14:10
 */
@Data
public class PageBaseInfo<T> {

    //页大小
    private long pageSize;

    //当前页
    private long pageNum;

    //总条数
    private long totalSize;

    //总页数
    private long totalPage;

    //数据集
    private List rows;

    public final static long NO_SIZE = 20;

    public PageBaseInfo(){
        this.pageSize = NO_SIZE;
    }

    public PageBaseInfo(long pageSize, long pageNum, long totalSize, long totalPage, List rows) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalSize = totalSize;
        this.totalPage = totalPage;
        this.rows = rows;
    }
}
