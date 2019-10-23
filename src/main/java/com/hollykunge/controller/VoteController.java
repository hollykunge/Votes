package com.hollykunge.controller;

import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.service.UserService;
import com.hollykunge.service.VoteService;
import com.hollykunge.util.Base64Utils;
import com.hollykunge.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author lark
 */
@Controller
public class VoteController {

    private final UserService userService;

    private final VoteService voteService;

    @Autowired
    public VoteController(UserService userService, VoteService voteService) {
        this.userService = userService;
        this.voteService = voteService;
    }

    @RequestMapping(value = "/votes/{username}", method = RequestMethod.GET)
    public String voteForUsername(@PathVariable String username,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model,
                                  @ModelAttribute("redirect") String redirect) {

        Optional<User> optionalUser = userService.findByUsername(username);
        if(!StringUtils.isEmpty(redirect)){
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
}
