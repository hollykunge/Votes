package com.hollykunge.config;

import com.alibaba.fastjson.JSONObject;
import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteItem;
import com.hollykunge.service.VoteItemService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecation 读取excel文件
 * @author zhhongyu
 */
@Slf4j
public class UploadDataListener extends AnalysisEventListener<ItemUploadData> {


    private final VoteItemService voteItemService;

    private final Vote vote;

    public UploadDataListener(Vote vote,VoteItemService voteItemService){
        this.voteItemService = voteItemService;
        this.vote = vote;
    }


    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
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
            //初始化导入被投票项设置默认
            voteItem.setIsTrunNext("1");
            //所在轮数为第一轮
            voteItem.setTurnNum("1");
            voteItem.setVote(vote);
            voteItemService.add(voteItem);
        });
        log.info("存储数据库成功！");
    }
}