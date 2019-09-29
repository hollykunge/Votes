package com.hollykunge.vote.controller;

import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.service.ClientInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @program: Lark-Server
 * @description: 投票
 * @author: Mr.Do
 * @create: 2019-09-21 11:14
 */
@Controller
@RequestMapping("/client")
public class VoteClientController {

    @Resource
    ClientInfoService clientInfoService;

    @GetMapping("/login")
    public String login(Model model, @RequestParam(value = "code", required = true, defaultValue = "") String code) {
        VoteItems voteItem = clientInfoService.getVoteItem(code);
        model.addAttribute("voteItems", voteItem);
        return "client/voteClientMain";
    }
}
