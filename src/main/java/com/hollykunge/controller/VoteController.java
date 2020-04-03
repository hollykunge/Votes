package com.hollykunge.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hollykunge.config.StatisticsDownloadData;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.model.Item;
import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteItem;
import com.hollykunge.msg.ObjectRestResponse;
import com.hollykunge.reflection.ReflectionUtils;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.UserService;
import com.hollykunge.service.UserVoteItemService;
import com.hollykunge.service.VoteService;
import com.hollykunge.util.Base64Utils;
import com.hollykunge.util.ExcelUtils;
import com.hollykunge.util.HttpResponseUtil;
import com.hollykunge.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author lark
 */
@Controller
public class VoteController extends BaseController{

    private final UserService userService;

    private final VoteService voteService;

    private final ItemService itemService;

    private final UserVoteItemService userVoteItemService;

    @Autowired
    public VoteController(UserService userService, VoteService voteService, ItemService itemService, UserVoteItemService userVoteItemService) {
        this.userService = userService;
        this.voteService = voteService;
        this.itemService = itemService;
        this.userVoteItemService = userVoteItemService;
    }

    @RequestMapping(value = "/votes/{username}", method = RequestMethod.GET)
    public String voteForUsername(@PathVariable String username,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model,
                                  @ModelAttribute("redirect") String redirect) {

        Optional<User> optionalUser = userService.findByUsername(username);
        if (!StringUtils.isEmpty(redirect)) {
            model.addAttribute("showAlertMessage", Base64Utils.decryption(redirect));
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Page<Vote> votes = voteService.findByUserOrderedByDatePageable(user, page);
            Pager pager = new Pager(votes);

            model.addAttribute("pager", pager);
            model.addAttribute("user", user);

            return "/votes";

        } else {
            return "/error";
        }
    }


    /**
     * 投票最终统计一键导出excel
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/voteStatistics/export/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse exportVoteStatic(@PathVariable Long id,
                                               HttpServletResponse resp) throws Exception {
        Optional<Vote> vote = voteService.findForId(id);
        if (!vote.isPresent()) {
            return HttpResponseUtil.setErrorResponse("没有查询到投票...");
        }
        Vote voteTemp = vote.get();
        if(!Objects.equals(voteTemp.getStatus(),VoteConstants.VOTE_FINAL_STATUS)){
            return HttpResponseUtil.setErrorResponse("投票没有结束不能导出投票...");
        }
        List<VoteItem> voteItems = getVoteItems(voteTemp);
        List<StatisticsDownloadData> statisticsDownloadData = JSONArray.parseArray(JSONObject.toJSONString(voteItems), StatisticsDownloadData.class);
        LinkedHashMap jsonObject = JSON.parseObject(voteTemp.getExcelHeader(), LinkedHashMap.class);
        //清洗一遍数据，excelhead中对应的值为null的给默认值无
        AtomicInteger setId = new AtomicInteger();
        for (StatisticsDownloadData data :
                statisticsDownloadData) {
            setId.getAndIncrement();
            ReflectionUtils.setFieldValue(data, "voteItemId", setId.intValue());
            jsonObject.forEach((key, value) -> {
                if (StringUtils.isEmpty(ReflectionUtils.getFieldValue(data, (String) key))) {
                    ReflectionUtils.setFieldValue(data, (String) key, "-");
                }
            });
            //此处处理的是，如果是否同规则，得分和排序结果两个字段为空会影响单元格，解决办法，将另外两个字段设置为无
            if (StringUtils.isEmpty(data.getCurrentStatisticsNum())) {
                data.setCurrentStatisticsNum("-");
            }
            if (StringUtils.isEmpty(data.getCurrentStatisticsToalScore())) {
                data.setCurrentStatisticsToalScore("-");
            }
            if (StringUtils.isEmpty(data.getCurrentStatisticsOrderScore())) {
                data.setCurrentStatisticsOrderScore(0);
            }
            //此处处理否同规则时
            if (!StringUtils.isEmpty(ReflectionUtils.getFieldValue(data, "agreeRulePassFlag"))) {
                if (Objects.equals(data.getAgreeRulePassFlag(), "1")) {
                    data.setAgreeRulePassFlag("通过");
                    continue;
                }
                data.setAgreeRulePassFlag("未通过");
            }
        }
        //开始生成excel
        //生成文件名称
        exportExcel(resp, voteTemp.getTitle(), jsonObject, statisticsDownloadData);
        return HttpResponseUtil.setSuccessResponse();
    }

    /**
     * 跳转到投票整体统计页面
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/voteStatistics/view/{id}", method = RequestMethod.GET)
    public String voteStatisticsView(@PathVariable Long id,
                                     Model model) throws Exception {
        Optional<Vote> vote = voteService.findForId(id);
        if(vote.isPresent()){
            Vote voteTemp = vote.get();
            List<VoteItem> voteItems = getVoteItems(voteTemp);
            model.addAttribute("voteItems",JSONObject.toJSONString(voteItems));
            model.addAttribute("vote",voteTemp);
            return "/votestatistics";
        }else{
            return "/error";
        }
    }

    private List<VoteItem> getVoteItems(Vote voteTemp) throws Exception {
        List<Item> itemsByVote = itemService.findItemsByVote(voteTemp);
        Collections.sort(itemsByVote, (o1, o2) -> o2.getTurnNum().compareTo(o1.getTurnNum()));
        //获取到了最后的一个轮次
        Item item = itemsByVote.get(0);
        //最后的投票结果
        List<VoteItem> finalturnVoteItems = getfinalTurnVoteItems(item);
        //如果就一个轮次，则返回最后一个轮次的结果
        if(itemsByVote.size() == 1){
            return finalturnVoteItems;
        }
        //查找是否有否同规则
        List<Item> agareeItems = itemsByVote.stream()
                .filter(entity -> Objects.equals(entity.getRules(), VoteConstants.ITEM_RULE_AGER))
                .collect(Collectors.toList());
        //倒序，最大的轮次不通过的人员，放在最上边
        Collections.sort(agareeItems, (o1, o2) -> o2.getTurnNum().compareTo(o1.getTurnNum()));
        List<VoteItem> result = new ArrayList<>();
        result.addAll(finalturnVoteItems);
        for (Item agreeItem :
                agareeItems) {
            result.addAll(getfinalTurnVoteItems(agreeItem)
                    .stream()
                    .filter(entity -> Objects.equals(entity.getAgreeRulePassFlag(), "0"))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    private void exportExcel(HttpServletResponse resp, String fileName, LinkedHashMap cellHeader, List data) throws IOException {
        ExcelUtils.export(resp, fileName, ExcelUtils.getHeader(cellHeader, null), "统计", data);
    }

    private List<VoteItem> getfinalTurnVoteItems(Item item) throws Exception {
        Map<String, Object> statistics = userVoteItemService.getStatistics(item);
        List<VoteItem> voteItems = (List<VoteItem>) statistics.get("voteItems");
        return voteItems;
    }
}
