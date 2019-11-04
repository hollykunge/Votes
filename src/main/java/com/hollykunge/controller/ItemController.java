package com.hollykunge.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hollykunge.config.*;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.model.VoteItem;
import com.hollykunge.reflection.ReflectionUtils;
import com.hollykunge.service.*;
import com.hollykunge.util.Base64Utils;
import com.hollykunge.util.ExceptionCommonUtil;
import com.hollykunge.util.ExtApiTokenUtil;
import com.hollykunge.util.VoteItemPassRuleUtils;
import com.hollykunge.vo.VoteItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lark
 */
@Slf4j
@Controller
public class ItemController {
    private final String LINK = ":";

    private final VoteService voteService;
    private final UserService userService;
    private final ItemService itemService;
    private final VoteItemService voteItemService;
    private final UserVoteItemService userVoteItemService;
    private final ExtApiTokenUtil extApiTokenUtil;

    @Autowired
    public ItemController(VoteService voteService, UserService userService,
                          ItemService itemService, VoteItemService voteItemService, UserVoteItemService userVoteItemService,ExtApiTokenUtil extApiTokenUtil) {
        this.voteService = voteService;
        this.userService = userService;
        this.itemService = itemService;
        this.voteItemService = voteItemService;
        this.userVoteItemService = userVoteItemService;
        this.extApiTokenUtil = extApiTokenUtil;
    }

    /**
     * 创建投票轮
     *
     * @param item
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/createTurn", method = RequestMethod.POST)
    public String createNewVote(@Valid Item item,
                                BindingResult bindingResult) throws Exception {
        String view = "redirect:/voteVote/" + item.getVote().getId() + "/";
        //如果为null为增加页面，使用0
        if (StringUtils.isEmpty(item.getId())) {
            view += "0";
        } else {
            view += item.getId();
        }
        try {
            if (!StringUtils.isEmpty(item.getAgreeMin())) {
                Integer.parseInt(item.getAgreeMin());
            }
            if (!StringUtils.isEmpty(item.getAgreeMax())) {
                Integer.parseInt(item.getAgreeMax());
            }
            if (!StringUtils.isEmpty(item.getMemberSize())) {
                item.getMemberSize();
            }
            if (!StringUtils.isEmpty(item.getMaxScore())) {
                Integer.parseInt(item.getMaxScore());
            }
            if (!StringUtils.isEmpty(item.getMinScore())) {
                Integer.parseInt(item.getMinScore());
            }
        } catch (NumberFormatException e) {
            error(bindingResult, "memberSize", "error.memberSize", "限制投票人数,范围（最大）, \n" +
                    "范围（最小）,打分项请输入数值字段");
            return frashItemView(item);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.stream().forEach(error -> {
                log.error(error.getDefaultMessage());
            });
            return frashItemView(item);
        } else {
            try {
                Long voteId = item.getVote().getId();
                itemService.save(item);
                return "redirect:/vote/" + voteId;
            } catch (BaseException e) {
                error(bindingResult, "memberSize", "error.memberSize", e.getMessage());
                return frashItemView(item);
            }
        }
    }

    public String frashItemView(@Valid Item item) throws Exception {
        return "/turnForm";
    }

    private void error(BindingResult bindingResult, String s, String s1, String s2) {
        bindingResult
                .rejectValue(s, s1, s2);
    }

    private void success(BindingResult bindingResult, String s, String s1, String s2) {
        bindingResult
                .rejectValue(s, s1, s2);
    }


    /**
     * 获取投票轮编辑页
     *
     * @param voteId    投票id
     * @param itemId    投票轮id
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/voteVote/{voteId}/{itemId}", method = RequestMethod.GET)
    public String voteVoteWithId(@PathVariable Long voteId,
                                 @PathVariable Long itemId,
                                 Principal principal,
                                 Model model) throws Exception {
        try {
            Item item = itemService.findById(itemId);
            //为添加
            if (item == null) {
                item = new Item();
                Optional<User> user = userService.findByUsername(principal.getName());
                Optional<Vote> vote = voteService.findForId(voteId);
                item.setUser(user.get());
                item.setVote(vote.get());
                //添加页面时默认选择否同
                item.setRules("1");
            }
            if (item.getMemberSize() == null){
                item.setMemberSize(item.getVote().getMemberSize());
            }
            if (item.getBody() == null){
                item.setBody(item.getVote().getBody());
            }
            model.addAttribute("item", item);
            return "/turnForm";
        } catch (Exception e) {
            throw e;
        }
//        Optional<Vote> vote = voteService.findForId(id);
//        if (vote.isPresent()) {
//            Optional<User> user = userService.findByUsername(principal.getName());
//            if (user.isPresent()) {
//                Item turn = new Item();
//                turn.setUser(user.get());
//                turn.setVote(vote.get());
//                model.addAttribute("turn", turn);
//                return "/turnForm";
//            } else {
//                return "/error";
//            }
//        } else {
//            return "/error";
//        }
    }

    /**
     * 获取投票项页
     *
     * @param id
     * @param model
     * @return
     */
//    @ExtApiToken(interfaceAdress = "/item/import")
    @RequestMapping(value = "/voteItemsView/{id}", method = RequestMethod.GET)
    public String voteItemsView(@PathVariable Long id,
                           Model model)throws Exception {

        try {
            Item item = itemService.findById(id);
            if(!StringUtils.isEmpty(item.getPreviousId())){
                Item prentItem = itemService.findById(Long.valueOf(item.getPreviousId()));
                model.addAttribute("parentItemRule",prentItem.getRules());
            }
            model.addAttribute("item", item);
            model.addAttribute("vote",item.getVote());
            return "/item";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ajax加载表数据
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/voteItems/{id}", method = RequestMethod.GET)
    public @ResponseBody List<VoteItemVO> voteItems(@PathVariable Long id,HttpServletRequest request)throws Exception {
        try {
            Item item = itemService.findById(id);
            Optional<List<VoteItem>> voteItems = voteItemService.findByItem(item);
            List<VoteItemVO> result = JSONArray.parseArray(JSONObject.toJSONString(voteItems.get()),VoteItemVO.class);
            return result;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 投票轮结果统计
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/stat/{id}", method = RequestMethod.GET)
    public String statItem(@PathVariable Long id,
                           Model model) {
        try {
            Item item = itemService.findById(id);
            model.addAttribute("item", item);
            return "/stat";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 投票项编辑保存
     *
     * @param item
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    public String editVoteWithId(@Valid Item item,
                                 BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.stream().forEach(error -> {
                log.error(error.getDefaultMessage());
            });
            return "/turnForm";

        } else {
            itemService.save(item);
            return "redirect:/vote/" + item.getVote().getId();
        }
    }

    /**
     * 导入投票轮数据
     *
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/item/import", method = RequestMethod.POST)
//    @ExtApiIdempotent(value = VoteConstants.EXTAPIHEAD)
    public @ResponseBody Map<String,Object> excelImport(MultipartFile file, HttpServletRequest request,Model model) throws Exception {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> headerResult = new HashMap<>();
        try {
            String itemIdTemp = request.getHeader("itemId");
            if (StringUtils.isEmpty(itemIdTemp)) {
                throw new BaseException("item不能为空...");
            }
            Item item = new Item();
            item.setId(Long.parseLong(itemIdTemp));
            EasyExcel.read(file.getInputStream(), ItemUploadData.class, new UploadHeaderDataListener(item,voteService,itemService,headerResult)).sheet().doRead();
            if((Integer) headerResult.get("status") == 500){
                return headerResult;
            }
            EasyExcel.read(file.getInputStream(), ItemUploadData.class, new UploadDataListener(item, voteItemService,result)).sheet().doRead();
            result.put("excelHeader",headerResult);
            return result;
        } catch (Exception e) {
            result.put("status",500);
            result.put("msg",e.getMessage());
            return result;
        }
    }

    /**
     * 导出模板
     *
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/item/export")
    @ResponseBody
    public String export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=exportFile.xlsx");
        EasyExcel.write(response.getOutputStream(), ItemDownloadData.class).sheet("模板").doWrite(null);
        return "success";
    }

    /**
     * 设置投票轮状态
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setItemStatus/{id}/{status}", method = RequestMethod.GET)
    public String setItemStatus(@PathVariable Long id,
                                @PathVariable String status,
                                RedirectAttributes redirectAttributes) throws Exception {
        //投票轮发布的时候，判断是否有投票项，没有不能发起投票
        Optional<List<VoteItem>> voteItems;
        if (Objects.equals(VoteConstants.ITEM_SEND_STATUS, status)) {
            Item item = new Item();
            item.setId(id);
            voteItems = voteItemService.findByItem(item);
            Item itemTemp = itemService.findById(id);
            if (voteItems.get().size() == 0) {
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("第" + itemTemp.getTurnNum() + "轮，没有投票项不能发起投票"));
                return "redirect:/vote/" + itemTemp.getVote().getId();
            }
            if(Objects.equals(itemTemp.getRules(),"1")){
                if(voteItems.get().size() < Integer.parseInt(itemTemp.getAgreeMin())){
                    redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("第"+itemTemp.getTurnNum()+"轮，投票项小于了限制的最小投票数"));
                    return "redirect:/vote/"+itemTemp.getVote().getId();
                }
            }
        }
        if (Objects.equals(VoteConstants.ITEM_FINAL_STATUS, status)) {
            Item itemTemp = itemService.findById(id);
            if (itemTemp.getMemberNum() == 0) {
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("暂无投票，不能结束本轮投票！"));
                return "redirect:/vote/" + itemTemp.getVote().getId();
            }
        }
        Item item = itemService.setItemStatus(id, status);
        return "redirect:/vote/" + item.getVote().getId();
    }

    /**
     * 删除投票轮
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable Long id,RedirectAttributes redirectAttributes) throws Exception {
        Item byId = itemService.findById(id);
        Optional<List<VoteItem>> byItem = voteItemService.findByItem(byId);
        if(byItem.isPresent() && byItem.get().size() > 0){
            redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("包含投票项，不能删除该投票轮"));
            return "redirect:/vote/" + byId.getVote().getId();
        }
        Item item = itemService.deleteItem(id);
        return "redirect:/vote/" + item.getVote().getId();
    }

    /**
     * 删除投票项
     *
     * @param itemId
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteVoteItem/{itemId}/{ids}", method = RequestMethod.GET)
    public @ResponseBody String deleteVoteItems(@PathVariable String itemId,

                                  @PathVariable String ids,
                                  Model model)throws Exception{
        try {
            voteItemService.deleteVoteItem(Arrays.asList(ids.split(",")));
            model.addAttribute("showMessage","删除成功！");
            return "success";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 添加一个投票项
     *
     * @param voteItem
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addVoteItem", method = RequestMethod.POST)
    public String addVoteItem(@RequestBody VoteItem voteItem) throws Exception {
        try {
            voteItemService.add(voteItem);
            return "redirect:/voteItemsView/" + voteItem.getItem().getId();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 统计接口
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/statistics/{id}", method = RequestMethod.GET)
    public String getStatistics(@PathVariable Long id,
                                Model model) throws Exception {
        try {
            Item item = getItem(id);
            Map<String, Object> statistics = userVoteItemService.getStatistics(item);
            model.addAttribute("statistics", JSONObject.toJSONString(statistics));
            model.addAttribute("item", item);
            model.addAttribute("itemObj", JSONObject.toJSONString(item));
            return "/stat";
        } catch (Exception e) {
            throw e;
        }
    }

    private Item getItem(Long id) {
        Item item = null;
        item = itemService.findById(id);
        if (item == null) {
            throw new BaseException("错误的投票轮..");
        }
        return item;
    }

    /**
     * 统计结果excel导出
     *
     * @param id(item的id)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/statistics/export/{id}", method = RequestMethod.GET)
    public @ResponseBody
    String statisticsExport(@PathVariable Long id,
                            HttpServletResponse response) throws Exception {
        try {
            Item item = this.getItem(id);
            Map<String, Object> statistics = userVoteItemService.getStatistics(item);
            List<VoteItem> voteItems = (List<VoteItem>) statistics.get("voteItems");
            List<StatisticsDownloadData> statisticsDownloadData = JSONArray.parseArray(JSONObject.toJSONString(voteItems), StatisticsDownloadData.class);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = "投票结果统计";
            if (item != null) {
                String voteName = item.getVote().getTitle();
                String trun = item.getTurnNum() + "";
                fileName = voteName + "第" + trun + fileName;
            }
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            String excelHeader = item.getVote().getExcelHeader();
            LinkedHashMap jsonObject = JSON.parseObject(excelHeader, LinkedHashMap.class);
            //清洗一遍数据，excelhead中对应的值为null的给默认值无
            AtomicInteger setId = new AtomicInteger();
            statisticsDownloadData.forEach(data -> {
                setId.getAndIncrement();
                ReflectionUtils.setFieldValue(data,"voteItemId", setId.intValue());
                AtomicInteger index = new AtomicInteger();
                jsonObject.forEach((key, value) -> {
                    index.getAndIncrement();
                    if (index.intValue() > 7) {
                        return;
                    }
                    if (StringUtils.isEmpty(ReflectionUtils.getFieldValue(data, (String) key))) {
                        ReflectionUtils.setFieldValue(data, (String) key, "无");
                    }
                });
                if(Objects.equals(item.getRules(),VoteConstants.ITEM_RULE_AGER)){
                    if(data.getCurrentStatisticsNum() == null){
                        ReflectionUtils.setFieldValue(data,"currentStatisticsNum", "0");
                    }
                    if(Objects.equals(data.getAgreeRulePassFlag(),"1")){
                        ReflectionUtils.setFieldValue(data,"agreeRulePassFlag", "通过");
                    }else{
                        ReflectionUtils.setFieldValue(data,"agreeRulePassFlag", "未通过");
                    }
                }
            });
            EasyExcel.write(response.getOutputStream())
                    .head(head(jsonObject,item.getRules()))
                    .sheet("统计情况")
                    .doWrite(statisticsDownloadData);
            return "success";
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 使用上一轮投票项
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/useParentItem/{id}", method = RequestMethod.GET)
    public String itemToNext(@PathVariable Long id,
                             Model model) throws Exception {
        try {
            Item dataItem = itemService.findById(id);
            String previousId = dataItem.getPreviousId();
            if (StringUtils.isEmpty(previousId)) {
                throw new BaseException("第一轮投票不能使用上一轮投票项");
            }
            Item item = getItem(Long.valueOf(previousId));
            Optional<List<VoteItem>> byItem = voteItemService.findByItem(item);
            if (!byItem.isPresent() || byItem.get().size() == 0) {
                throw new BaseException("上一轮没有投票项...");
            }
            voteItemService.deleteByItem(dataItem);
            List<VoteItem> tempData = JSONArray.parseArray(JSONObject.toJSONString(byItem.get()), VoteItem.class);
            //规则为否同的时候，按百分比规则确定进入下一轮人数
            if(Objects.equals(item.getRules(),VoteConstants.ITEM_RULE_AGER)){
                Long totalNum = userVoteItemService.countIpByItem(item);
                tempData = VoteItemPassRuleUtils.passVoteItems(tempData,item,Integer.parseInt(String.valueOf(totalNum)));
            }
            for (VoteItem vote : tempData) {
                this.resetVoteItem(vote);
                vote.setItem(dataItem);
                vote.setTurnNum(String.valueOf(dataItem.getTurnNum()));
                voteItemService.add(vote);
            }
            return "redirect:/voteItemsView/" + id;
        } catch (Exception e) {
            throw e;
        }
    }

    private void resetVoteItem(VoteItem voteItem) {
        voteItem.setVoteItemId(null);
        voteItem.setParentStatisticsNum(voteItem.getCurrentStatisticsNum());
        voteItem.setParentStatisticsOrderScore(voteItem.getCurrentStatisticsOrderScore());
        voteItem.setParentStatisticsToalScore(voteItem.getCurrentStatisticsToalScore());
        voteItem.setCurrentStatisticsNum(null);
        voteItem.setCurrentStatisticsOrderScore(null);
        voteItem.setCurrentStatisticsToalScore(null);
        voteItem.setVoteItemOrder(null);
        voteItem.setAgreeRulePassFlag(null);
    }

    private List<List<String>> head(LinkedHashMap jsonObject,String flag) {
        try {
            List<List<String>> list = new ArrayList<List<String>>();
            List<String> one = new ArrayList<>();
            one.add("序号");
            list.add(one);
            Collection<Object> values = jsonObject.values();
            AtomicInteger index = new AtomicInteger();
            values.forEach((Object ob) -> {
                index.getAndIncrement();
                if (index.intValue() > 7) {
                    return;
                }
                List<String> head = new ArrayList<String>();
                head.add((String) ob);
                list.add(head);
            });
            List<String> fina = new ArrayList<>();
            if(Objects.equals(flag,"1")){
                fina.add("总票数结果");
            }
            if(Objects.equals(flag,"2")){
                fina.add("排序汇总结果");
            }
            if(Objects.equals(flag,"3")){
                fina.add("总得分结果");
            }
            list.add(fina);
            if(Objects.equals(flag,"1")){
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
}
