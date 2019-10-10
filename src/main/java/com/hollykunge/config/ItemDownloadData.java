package com.hollykunge.config;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ItemDownloadData {
    @ExcelProperty("名称")
    private String name;
    @ExcelProperty("备用1")
    private String attr1;
    @ExcelProperty("备用2")
    private String attr2;
    @ExcelProperty("备用3")
    private String attr3;
    @ExcelProperty("备用4")
    private String attr4;
}