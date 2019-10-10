package com.hollykunge.config;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ItemDownloadData {
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("备用1")
    private String arrt1;
    @ExcelProperty("备用2")
    private String arrt2;
    @ExcelProperty("备用3")
    private String arrt3;
    @ExcelProperty("备用4")
    private String arrt4;
}