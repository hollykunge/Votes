package com.hollykunge.controller;

import com.hollykunge.config.ItemStatusConfig;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.model.User;
import com.hollykunge.model.UserVoteIp;
import com.hollykunge.model.VoteItem;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.VoteItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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


    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"{id}/{code}", method = RequestMethod.GET)
    public String inviteCodeView(@PathVariable Long id,
                                 @PathVariable String code,
                                 Model model) throws Exception {
        try{
            Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
            if(!itemTemp.isPresent()){
                throw new BaseException("无效地址...");
            }
            Optional<List<VoteItem>> optVoteItems = voteItemService.findByItem(itemTemp.get());
            model.addAttribute("item",itemTemp.get());
            model.addAttribute("itemStatus", ItemStatusConfig.getEnumByValue(itemTemp.get().getStatus()).getName());
            //用户列表页面显示的投票项
            model.addAttribute("voteItems",optVoteItems.get());
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 用户开始投票的保存
     * @param voteItem
     * @return
     * @throws Exception
     */
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"add", method = RequestMethod.POST)
    public String add(
            @Valid VoteItem voteItem,
                      Model model,
                      HttpServletRequest request) throws Exception {
        try{
            String clientIp = request.getHeader("clientIp");
            UserVoteIp userVoteIp = this.setClientIp(clientIp);
            //设置为投票完成
            userVoteIp.setStatus(VoteConstants.USER_IP_VOTE_FINAL_FLAG);
            //为了不影响user中前台提示信息，给username，password设置系统默认值了
            voteItem.setUserIps(Collections.singletonList(userVoteIp));
            voteItemService.add(voteItem);
            model.addAttribute("showMessage","操作成功！");
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 用户开始投票之后显示自己投票完的数据
     * @param voteItem
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"all", method = RequestMethod.GET)
    public String getAll(
            @Valid VoteItem voteItem,
            Model model,
            HttpServletRequest request) throws Exception {
        try{
            String clientIp = request.getHeader("clientIp");
            UserVoteIp userVoteIp = this.setClientIp(clientIp);
            //为了不影响user中前台提示信息，给username，password设置系统默认值了
            voteItem.setUserIps(Collections.singletonList(userVoteIp));
            List<VoteItem> voteItems = voteItemService.findByUserIps(Collections.singletonList(userVoteIp));
            //用户列表页面显示的投票项
            model.addAttribute("voteItems",voteItems);
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }

    private UserVoteIp setClientIp(String clientIp){
        //如果请求头中没有ip，则为本地测试，使用默认值了
        if(StringUtils.isEmpty(clientIp)){
            clientIp = VoteConstants.DEFUALT_CLIENTIP;
        }
        UserVoteIp userVoteIp = new UserVoteIp();
        userVoteIp.setIp(clientIp);
        return userVoteIp;
    }
}
