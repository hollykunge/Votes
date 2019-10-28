package com.hollykunge.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hollykunge.annotation.ExtApiIdempotent;
import com.hollykunge.annotation.ExtApiToken;
import com.hollykunge.config.ItemStatusConfig;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.UserVoteItemService;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.util.Base64Utils;
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
                model.addAttribute("showAlertMessage", Base64Utils.decryption(redirect));
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
     * 用户投票点击下一轮接口（地址：/userVote/nextTurn/{id}/{code}）
     * @param id
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"nextTurn/"+"{id}/{code}", method = RequestMethod.GET)
    public String nextTurnItem(@PathVariable Long id,
                               @PathVariable String code,
                               RedirectAttributes redirectAttributes)throws Exception{
        Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
        if(!itemTemp.isPresent()){
            throw new BaseException("无效地址...");
        }
        List<Item> byPrevious = itemService.findByPrevious(String.valueOf(id));
        if(byPrevious.size() == 0){
            redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("没有下一轮投票！"));
            return "redirect:"+VoteConstants.INVITECODE_RPC+id+"/"+code;
        }
        Item item = byPrevious.get(0);
        return "redirect:"+VoteConstants.INVITECODE_RPC+item.getId()+"/"+item.getCode();
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
    public String add(@PathVariable Long id,
            @PathVariable String code,
            @RequestBody String userVoteItems,
                      Model model,
                      HttpServletRequest request) throws Exception {
        try{
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
                    //重新生成幂等性token
                    extApiTokenUtil.extApiToken(ClientIpUtil.getClientIp(request),VoteConstants.INVITECODE_RPC+"add");
                    return"投票数量低于投票轮设置的最低数量或高于最高数量...";
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
            return "success";
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
