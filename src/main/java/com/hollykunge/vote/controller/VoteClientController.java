package com.hollykunge.vote.controller;

import com.hollykunge.vote.anotation.OpsLog;
import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.service.ClientInfoService;
import com.hollykunge.vote.utils.BaseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

import static com.hollykunge.vote.constant.Constant.PARAMS_ERROR;

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

    @OpsLog("开始投票")
    @GetMapping("/start")
    public String start(Model model, @RequestParam(value = "code", required = true, defaultValue = "") String code) {
        if (StringUtils.isEmpty(code)){
            throw new BaseException(PARAMS_ERROR);
        }
        VoteItems voteItem = clientInfoService.getVoteItem(code);
        model.addAttribute("voteItems", voteItem);
        return "client/voteClientMain";
    }
}
