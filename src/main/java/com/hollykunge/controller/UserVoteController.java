package com.hollykunge.controller;

import com.hollykunge.config.ItemStatusConfig;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.Item;
import com.hollykunge.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author lark
 */
@Slf4j
@Controller
public class UserVoteController {
    @Autowired
    private ItemService itemService;


    @RequestMapping(value = VoteConstants.INVITECODE_RPC+"{id}/{code}", method = RequestMethod.GET)
    public String inviteCodeView(@PathVariable Long id,
                                 @PathVariable String code,
                                 Model model,HttpServletRequest request) throws Exception {
        try{
            Optional<Item> itemTemp = itemService.findByIdAndCode(id,code);
            if(!itemTemp.isPresent()){
                throw new BaseException("无效连接...");
            }
            model.addAttribute("item",itemTemp.get());
            model.addAttribute("itemStatus", ItemStatusConfig.getEnumByValue(itemTemp.get().getStatus()).getName());
            return "/userVote";
        }catch (Exception e){
            throw e;
        }
    }
}
