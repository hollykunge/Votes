package com.hollykunge.controller;

import com.hollykunge.model.Vote;
import com.hollykunge.service.VoteService;
import com.hollykunge.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lark
 */
@Controller
public class HomeController {

    private final VoteService voteService;

    @Autowired
    public HomeController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "0") int page,
                       Model model) {

        Page<Vote> votes = voteService.findAllOrderedByDatePageable(page);
        Pager pager = new Pager(votes);

        model.addAttribute("pager", pager);

        return "/home";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "/home";
    }
}
