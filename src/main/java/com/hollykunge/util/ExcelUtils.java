package com.hollykunge.util;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 14:55 2019/12/12
 */
@Slf4j
public final class ExcelUtils {
    /**
     * 获取单元格表头
     *
     * @param jsonObject
     * @param flag
     * @return
     */
    public static List<List<String>> getHeader(LinkedHashMap jsonObject, String flag) {
        try {
            List<List<String>> list = new ArrayList<List<String>>();
            List<String> one = new ArrayList<>();
            one.add("主键");
            list.add(one);
            Collection<Object> values = jsonObject.values();
            AtomicInteger index = new AtomicInteger();
            values.forEach((Object ob) -> {
                index.getAndIncrement();
                if (index.intValue() > 32) {
                    return;
                }
                List<String> head = new ArrayList<String>();
                head.add((String) ob);
                list.add(head);
            });
            List<String> fina = new ArrayList<>();
            if (Objects.equals(flag, "1")) {
                fina.add("总票数结果");
            }
            if (Objects.equals(flag, "2")) {
                fina.add("排序汇总结果");
            }
            if (Objects.equals(flag, "3")) {
                fina.add("总得分结果");
            }
            if(!fina.isEmpty()){
                list.add(fina);
            }
            if (StringUtils.isEmpty(flag)) {
                List<String> agree = new ArrayList<>();
                agree.add("否同规则结果");
                list.add(agree);
                List<String> order = new ArrayList<>();
                order.add("排序规则结果");
                list.add(order);
                List<String> socore = new ArrayList<>();
                socore.add("打分规则结果");
                list.add(socore);
                List<String> agreepass = new ArrayList<>();
                agreepass.add("否同规则是否通过");
                list.add(agreepass);
            }
            if (Objects.equals(flag, "1")) {
                List<String> pass = new ArrayList<>();
                pass.add("是否通过");
                list.add(pass);
            }
            return list;
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }

    /**
     * 开始执行导入excel
     *
     * @param response    响应流
     * @param fileName    文件名称（中文已转码）
     * @param cellHeaders 表头名称
     * @param sheetName   sheet页名称
     * @param data        数据
     * @throws IOException
     */
    public static void export(HttpServletResponse response,
                              String fileName,
                              List<List<String>> cellHeaders,
                              String sheetName,
                              List data) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8");
        fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        if (StringUtils.isEmpty(sheetName)) {
            sheetName = "sheet";
        }
        EasyExcel.write(response.getOutputStream())
                .head(cellHeaders)
                .sheet(sheetName)
                .doWrite(data);
    }
}
