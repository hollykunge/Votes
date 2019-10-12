package com.hollykunge.config;

import com.alibaba.fastjson.JSONObject;
import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteItem;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.util.ExceptionCommonUtil;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecation 读取excel文件
 * @author zhhongyu
 */
@Slf4j
public class UploadDataListener extends AnalysisEventListener<ItemUploadData> {


    private final VoteItemService voteItemService;

    private final Item item;

    public UploadDataListener(Item item,VoteItemService voteItemService){
        this.voteItemService = voteItemService;
        this.item = item;
    }


    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    List<ItemUploadData> list = new ArrayList<ItemUploadData>();

    @Override
    public void invoke(ItemUploadData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData(list);
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(List<ItemUploadData> data) {
        log.info("{}条数据，开始存储数据库！", list.size());
        data.stream().forEach(item ->{
            VoteItem voteItem = JSONObject.parseObject(JSONObject.toJSONString(item),VoteItem.class);
            //所在轮数为第一轮
            voteItem.setTurnNum("1");
            try {
                voteItemService.add(voteItem);
                log.info("存储数据库成功！");
            } catch (Exception e) {
                log.error("存储数据库失败！");
                log.error(ExceptionCommonUtil.getExceptionMessage(e));
            }
        });
    }
}