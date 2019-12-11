package com.hollykunge.controller;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.model.Item;
import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.service.ItemService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lark
 */
@Controller
public class VoteController {

    private final UserService userService;

    private final VoteService voteService;

    private final ItemService itemService;

    @Autowired
    public VoteController(UserService userService, VoteService voteService,ItemService itemService) {
        this.userService = userService;
        this.voteService = voteService;
        this.itemService = itemService;
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

    /**
     * 设置投票状态接口
     * @param id
     * @param status
     * @param principal
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/vote/setstatus/{id}/{status}",method = RequestMethod.GET)
    public String setVoteStatus(@PathVariable Long id,
                                @PathVariable String status,
                                Principal principal,
                                RedirectAttributes redirectAttributes) throws Exception {
        //结束投票时增加校验
        if(Objects.equals(status,VoteConstants.VOTE_FINAL_STATUS)){
            Vote voteTemp = new Vote();
            voteTemp.setId(id);
            List<Item> itemsByVote = itemService.findItemsByVote(voteTemp);
            boolean noFinal = itemsByVote.stream().anyMatch(item -> Objects.equals(VoteConstants.ITEM_ADD_STATUS, item.getStatus()) ||
                    Objects.equals(VoteConstants.ITEM_SEND_STATUS, item.getStatus()));
            if(noFinal){
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("包含没有结束的投票伦.."));
                return "redirect:/votes/"+principal.getName();
            }
        }
        Vote vote = new Vote();
        vote.setId(id);
        vote.setStatus(status);
        voteService.updateById(vote);
        return "redirect:/votes/"+principal.getName();
    }
}
