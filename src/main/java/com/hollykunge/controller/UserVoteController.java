package com.hollykunge.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hollykunge.annotation.ExtApiIdempotent;
import com.hollykunge.annotation.ExtApiToken;
import com.hollykunge.config.ItemStatusConfig;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.dictionary.VoteHttpResponseStatus;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.ExtToken;
import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;
import com.hollykunge.msg.ObjectRestResponse;
import com.hollykunge.service.ExtTokenService;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.UserVoteItemService;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.util.ClientIpUtil;
import com.hollykunge.util.ExtApiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author lark
 */
@Slf4j
@Controller
public class UserVoteController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private VoteItemService voteItemService;
    @Autowired
    private UserVoteItemService userVoteItemService;
    @Autowired
    private ExtApiTokenUtil extApiTokenUtil;
    @Autowired
    private ExtTokenService extTokenService;

    @ExtApiToken(interfaceAdress = VoteConstants.INVITECODE_RPC+"add")
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"{id}/{code}", method = RequestMethod.GET)
    public String inviteCodeView(@PathVariable Long id,
                                 @PathVariable String code,
                                 Model model,
                                 HttpServletRequest request,
                                 @ModelAttribute("redirect") String redirect) throws Exception {
        try{
            Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
            if(!itemTemp.isPresent()){
                throw new BaseException("无效地址...");
            }
            //如果当前轮的状态为结束，则进入当前轮的统计页面
            if(itemTemp.isPresent() && Objects.equals(itemTemp.get().getStatus(),VoteConstants.ITEM_FINAL_STATUS)){
                return this.inviteCodeStatisticsView(id,code,model,request,null);
            }
            Optional<List<VoteItem>> optVoteItems = voteItemService.findByItem(itemTemp.get());
            model.addAttribute("item",itemTemp.get());
            model.addAttribute("itemStatus", ItemStatusConfig.getEnumByValue(itemTemp.get().getStatus()).getName());
            //用户列表页面显示的投票项
            model.addAttribute("voteItems", JSONObject.toJSONString(optVoteItems.get()));

            String clientIp = getClientIp(request);
            List<UserVoteItem> userVoteItems = userVoteItemService.findByItemAndIp(itemTemp.get(), clientIp);
            //用户投完票项目，前台展示可采用如果userVoteItems没有数据，则为第一次投票列表使用voteItems
            model.addAttribute("userVoteItems",JSONObject.toJSONString(userVoteItems));
            if(!StringUtils.isEmpty(redirect)){
                model.addAttribute("showAlertMessage",redirect);
            }
            if(!StringUtils.isEmpty(itemTemp.get().getPreviousId())){
                Item parent = itemService.findById(Long.valueOf(itemTemp.get().getPreviousId()));
                model.addAttribute("parentItemRules",parent.getRules());
            }
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 进入统计结果页面
     * @param id
     * @param code
     * @param model
     * @param request
     * @param redirect
     * @return
     * @throws Exception
     */
    public String inviteCodeStatisticsView(Long id,
                                 String code,
                                 Model model,
                                 HttpServletRequest request,
                                 String redirect) throws Exception {
        try{
            Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
            if(!itemTemp.isPresent()){
                throw new BaseException("无效地址...");
            }
            Map<String, Object> statistics = userVoteItemService.getStatistics(itemTemp.get());
            model.addAttribute("statistics", JSONObject.toJSONString(statistics));
            model.addAttribute("item", itemTemp.get());
            model.addAttribute("itemObj", JSONObject.toJSONString(itemTemp.get()));
            if(!StringUtils.isEmpty(redirect)){
                model.addAttribute("showAlertMessage", redirect);
            }
            return "/userStat";
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 用户投票点击下一轮接口（地址：/userVote/nextTurn/{id}/{code}）
     * @param id
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"nextTurn/"+"{id}/{code}", method = RequestMethod.GET)
    public String nextTurnItem(@PathVariable Long id,
                               @PathVariable String code,
                               Model model,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes)throws Exception{
        Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
        if(!itemTemp.isPresent()){
            throw new BaseException("无效地址...");
        }
        List<Item> byPrevious = itemService.findByPrevious(String.valueOf(id));
        //当前轮为发起，进入当前轮投票
        if(itemTemp.isPresent() && Objects.equals(itemTemp.get().getStatus(),VoteConstants.ITEM_SEND_STATUS)){
            return this.inviteCodeView(id,code,model,request,"没有下一轮投票！");
        }
        if(itemTemp.isPresent()
                && Objects.equals(itemTemp.get().getStatus(),VoteConstants.ITEM_FINAL_STATUS)){
            //当前轮为结束，下一轮为没有，当前轮统计页面
            if(byPrevious.size() == 0){
                //当前轮的统计
                return this.inviteCodeStatisticsView(id,code,model,request,"没有下一轮投票！");
            }
            Item item = byPrevious.get(0);
            //当前轮为结束，下一轮为新建，当前轮统计页面
            if(Objects.equals(item.getStatus(),VoteConstants.ITEM_ADD_STATUS)){
                return this.inviteCodeStatisticsView(id,code,model,request,"下一轮没有发起！");
            }
            //当前轮为结束，下一轮为发起，下一轮投票页面
            if(Objects.equals(item.getStatus(),VoteConstants.ITEM_SEND_STATUS)){
                List<ExtToken> vote_token = extTokenService.findToken((String) request.getSession().getAttribute("vote_token"));
                extTokenService.deleteToken(vote_token);
                //手动生成新的token
                extApiTokenUtil.extApiToken(ClientIpUtil.getClientIp(request),VoteConstants.INVITECODE_RPC+"add");
                return this.inviteCodeView(item.getId(),item.getCode(),model,request,null);
            }
            //当前轮为结束，下一轮为结束，下一轮统计页面
            if(Objects.equals(item.getStatus(),VoteConstants.ITEM_FINAL_STATUS)){
                return this.inviteCodeStatisticsView(item.getId(),item.getCode(),model,request,null);
            }
        }
        return "/403";
    }

    /**
     * 用户开始投票的保存
     * @param userVoteItems
     * @return
     * @throws Exception
     */
    @ExtApiIdempotent(VoteConstants.EXTAPIHEAD)
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"add/{id}/{code}", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse add(@PathVariable Long id,
                                  @PathVariable String code,
                                  @RequestBody String userVoteItems,
                                  Model model,
                                  HttpServletRequest request) throws Exception {
        try{
            ObjectRestResponse response = new ObjectRestResponse();
            String clientIp = getClientIp(request);
            List<UserVoteItem> userVoteItemlist = JSONArray.parseArray(userVoteItems, UserVoteItem.class);
            List<UserVoteItem> collect = userVoteItemlist.stream().filter(new Predicate<UserVoteItem>() {
                @Override
                public boolean test(UserVoteItem userVoteItem) {
                    if (Objects.equals(userVoteItem.getAgreeFlag(), "1")) {
                        return true;
                    }
                    return false;
                }
            }).collect(Collectors.toList());
            Item item = itemService.findById(id);
            //否同规则校验
            if(Objects.equals(item.getRules(),VoteConstants.ITEM_RULE_AGER)){
                if(collect.size()>Integer.parseInt(item.getAgreeMax())||
                        collect.size()<Integer.parseInt(item.getAgreeMin())){
                    response.setStatus(VoteHttpResponseStatus.INFORMATIONAL.getValue());
                    response.setMessage("投票数量小于最低数量或大于最高数量...");
                    return response;
                }
            }
            for (UserVoteItem userVoteItem:
                    userVoteItemlist) {
                userVoteItem.setIp(clientIp);
                userVoteItemService.add(userVoteItem);
            }
            Long memgerNum = userVoteItemService.countIpByItem(item);
            item.setMemberNum(Integer.parseInt(String.valueOf(memgerNum)));
            itemService.save(item);
            model.addAttribute("showMessage","操作成功！");
            response.setStatus(VoteHttpResponseStatus.SUCCESS.getValue());
            response.setRel(true);
            return response;
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 用户开始投票之后显示自己投票完的数据
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"all", method = RequestMethod.GET)
    public String getAll(
            Model model,
            HttpServletRequest request) throws Exception {
        try{
            String clientIp = getClientIp(request);
            //为了不影响user中前台提示信息，给username，password设置系统默认值了
            List<UserVoteItem> voteItems = userVoteItemService.findByUserIp(clientIp);
            Item item = new Item();
            item.setId((long) 2);
            userVoteItemService.findByItemAndIp(item,clientIp);
            //用户列表页面显示的投票项
            model.addAttribute("voteItems",voteItems);
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }
    
    public String getClientIp(HttpServletRequest request){
        String clientIp = request.getHeader("clientIp");
        //如果请求头中没有ip，则为本地测试，使用默认值了
        if(StringUtils.isEmpty(clientIp)){
            clientIp = request.getRemoteHost();
        }
        if(StringUtils.isEmpty(clientIp)){
            clientIp = VoteConstants.DEFUALT_CLIENTIP;
        }
        return clientIp;
    }
}
