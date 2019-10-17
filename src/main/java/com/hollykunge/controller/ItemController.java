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
import com.hollykunge.service.*;
import com.hollykunge.util.Base64Utils;
import com.hollykunge.util.ExceptionCommonUtil;
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

    @Autowired
    public ItemController(VoteService voteService, UserService userService,
                          ItemService itemService, VoteItemService voteItemService,UserVoteItemService userVoteItemService) {
        this.voteService = voteService;
        this.userService = userService;
        this.itemService = itemService;
        this.voteItemService = voteItemService;
        this.userVoteItemService = userVoteItemService;
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
                                BindingResult bindingResult) throws Exception{
        String view = "redirect:/voteVote/"+item.getVote().getId()+"/";
        //如果为null为增加页面，使用0
        if(StringUtils.isEmpty(item.getId())){
            view+="0";
        }else{
            view+=item.getId();
        }
        try{
            if(!StringUtils.isEmpty(item.getAgreeMin())){
                Integer.parseInt(item.getAgreeMin());
            }
            if(!StringUtils.isEmpty(item.getAgreeMax())){
                Integer.parseInt(item.getAgreeMax());
            }
            if(!StringUtils.isEmpty(item.getMemberSize())){
                Integer.parseInt(item.getMemberSize());
            }
            if(!StringUtils.isEmpty(item.getMaxScore())){
                Integer.parseInt(item.getMaxScore());
            }
            if(!StringUtils.isEmpty(item.getMinScore())){
                Integer.parseInt(item.getMinScore());
            }
        }catch (NumberFormatException e){
             error(bindingResult,"memberSize","error.memberSize","限制投票人数,范围（最大）, \n" +
                     "范围（最小）,打分项请输入数值字段");
             return frashItemView(item);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.stream().forEach(error->{
                log.error(error.getDefaultMessage());
            });
            return frashItemView(item);
        } else {
            try{
                Long voteId = item.getVote().getId();
                itemService.save(item);
                return "redirect:/vote/" + voteId;
            }catch (BaseException e){
                error(bindingResult,"memberSize","error.memberSize",e.getMessage());
                return frashItemView(item);
            }
        }
    }

    public String frashItemView(@Valid Item item)throws Exception {
            return "/turnForm";
    }

    private void error(BindingResult bindingResult,String s,String s1,String s2){
        bindingResult
                .rejectValue(s, s1, s2);
    }
    private void success(BindingResult bindingResult,String s,String s1,String s2){
        bindingResult
                .rejectValue(s, s1, s2);
    }


    /**
     * 获取投票轮编辑页
     *
     * @param voteId 投票id
     * @param itemId 投票轮id
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/voteVote/{voteId}/{itemId}", method = RequestMethod.GET)
    public String voteVoteWithId(@PathVariable Long voteId,
                                 @PathVariable Long itemId,
                                 Principal principal,
                                 Model model)throws Exception {
        try{
            Item item = itemService.findById(itemId);
            //为添加
            if(item == null){
                item = new Item();
                Optional<User> user = userService.findByUsername(principal.getName());
                Optional<Vote> vote = voteService.findForId(voteId);
                item.setUser(user.get());

                item.setVote(vote.get());
                //添加页面时默认选择否同
                item.setRules("1");
            }
            model.addAttribute("item", item);
            return "/turnForm";
        }catch (Exception e){
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
    @RequestMapping(value = "/editItem/{id}", method = RequestMethod.GET)
    public String editItem(@PathVariable Long id,
                           Model model)throws Exception {
        try {
            Item item = itemService.findById(id);
            Optional<List<VoteItem>> voteItems = voteItemService.findByItem(item);
            model.addAttribute("item", item);
            model.addAttribute("vote",item.getVote());
            model.addAttribute("voteItems", null);
            if(voteItems.get().size()>0){
                model.addAttribute("voteItems", JSONObject.toJSONString(voteItems.get()));
            }
            return "/item";
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
        try{
            Item item = itemService.findById(id);
            model.addAttribute("item", item);
            return "/stat";
        }catch (Exception e){
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
                                 BindingResult bindingResult) throws Exception{
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.stream().forEach(error->{
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
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/item/import", method = RequestMethod.POST)
    public String excelImport(MultipartFile file, HttpServletRequest request) throws Exception {
        try {
            String itemIdTemp = request.getHeader("itemId");
            if (StringUtils.isEmpty(itemIdTemp)) {
                throw new BaseException("item不能为空...");
            }
            Item item = new Item();
            item.setId(Long.parseLong(itemIdTemp));
            EasyExcel.read(file.getInputStream(), ItemUploadData.class, new UploadDataListener(item, voteItemService)).sheet().doRead();
            EasyExcel.read(file.getInputStream(), ItemUploadData.class, new UploadHeaderDataListener(item,voteService,itemService)).sheet().doRead();
            return "redirect:/editItem/" + itemIdTemp;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    /**
     * 导出模板
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
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setItemStatus/{id}/{status}", method = RequestMethod.GET)
    public String setItemStatus(@PathVariable Long id,
                                @PathVariable String status,
                                RedirectAttributes redirectAttributes) throws Exception {
        //投票轮发布的时候，判断是否有投票项，没有不能发起投票
        Optional<List<VoteItem>> voteItems;
        if(Objects.equals(VoteConstants.ITEM_SEND_STATUS,status)){
            Item item = new Item();
            item.setId(id);
            voteItems = voteItemService.findByItem(item);
            Item itemTemp = itemService.findById(id);
            if(voteItems.get().size()==0){
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("第"+itemTemp.getTurnNum()+"轮，没有投票项不能发起投票"));
                return "redirect:/vote/"+itemTemp.getVote().getId();
            }
        }
        if(Objects.equals(VoteConstants.ITEM_FINAL_STATUS,status)){
            Item itemTemp = itemService.findById(id);
            if(itemTemp.getMemberNum() == 0){
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("暂无投票，不能结束本轮投票！"));
                return "redirect:/vote/"+itemTemp.getVote().getId();
            }
        }
        Item item = itemService.setItemStatus(id, status);
        return "redirect:/vote/"+item.getVote().getId();
    }

    /**
     * 删除投票轮
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteItem(@PathVariable Long id) throws Exception {
        Item item = itemService.deleteItem(id);
        return "redirect:/vote/"+item.getVote().getId();
    }

    /**
     * 删除投票项
     * @param itemId
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/deleteVoteItem/{itemId}/{ids}", method = RequestMethod.GET)
    public String deleteVoteItems(@PathVariable String itemId,
                                  @PathVariable String ids)throws Exception{
        try {
            voteItemService.deleteVoteItem(Arrays.asList(ids.split(",")));
            return "redirect:/editItem/"+itemId;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 添加一个投票项
     * @param voteItem
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addVoteItem", method = RequestMethod.POST)
    public String addVoteItem(@RequestBody VoteItem voteItem)throws Exception{
        try {
            voteItemService.add(voteItem);
            return "redirect:/editItem/"+voteItem.getItem().getId();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 统计接口
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/statistics/{id}", method = RequestMethod.GET)
    public String getStatistics(@PathVariable Long id,
                                Model model)throws Exception{
        try {
            Item item = getItem(id);
            Map<String, Object> statistics = userVoteItemService.getStatistics(item);
            model.addAttribute("statistics",statistics);
            return "";
        } catch (Exception e) {
            throw e;
        }
    }

    private Item getItem(Long id){
        Item item = null;
        item = itemService.findById(id);
        if(item == null){
            throw new BaseException("错误的投票轮..");
        }
        return item;
    }

    /**
     * 统计结果excel导出
     * @param id(item的id)
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/statistics/import/{id}", method = RequestMethod.GET)
    public @ResponseBody String statisticsImport (@PathVariable Long id,
                                HttpServletResponse response)throws Exception{
        try {
            Item item = this.getItem(id);
            Map<String, Object> statistics = userVoteItemService.getStatistics(item);
            List<VoteItem> voteItems = (List<VoteItem>) statistics.get("voteItems");
            List<StatisticsDownloadData> statisticsDownloadData = JSONArray.parseArray(JSONObject.toJSONString(voteItems), StatisticsDownloadData.class);
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=itemResult.xlsx");
            EasyExcel.write(response.getOutputStream())
                    .head(head(item.getVote().getExcelHeader()))
                    .sheet("统计情况")
                    .doWrite(statisticsDownloadData);
            return "success";
        } catch (Exception e) {
            throw e;
        }
    }

    private List<List<String>> head(String header) {
        try{
            JSONObject jsonObject = JSON.parseObject(header);
            List<List<String>> list = new ArrayList<List<String>>();
            List<String> one = new ArrayList<>();
            one.add("序号");
            list.add(one);
            Collection<Object> values = jsonObject.values();
            AtomicInteger index = new AtomicInteger();
            values.forEach((Object ob) ->{
                index.getAndIncrement();
                if(index.intValue() > 7){
                    return;
                }
                List<String> head = new ArrayList<String>();
                head.add((String) ob);
                list.add(head);
            });
            List<String> fina = new ArrayList<>();
            fina.add("统计结果");
            list.add(fina);
            return list;
        }catch (Exception e){
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
            throw e;
        }
    }
}
