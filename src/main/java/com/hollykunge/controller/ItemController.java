package com.hollykunge.controller;

import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.VoteService;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

/**
 * @author lark
 */
@Controller
public class ItemController {

    private final VoteService voteService;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public ItemController(VoteService voteService, UserService userService, ItemService itemService) {
        this.voteService = voteService;
        this.userService = userService;
        this.itemService = itemService;
    }

    @RequestMapping(value = "/createTurn", method = RequestMethod.POST)
    public String createNewVote(@Valid Item item,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/turnForm";

        } else {
            itemService.save(item);
            return "redirect:/vote/" + item.getVote().getId();
        }
    }

    /**
     * 投票轮数发布
     *
     * @param id
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/voteVote/{id}", method = RequestMethod.GET)
    public String voteVoteWithId(@PathVariable Long id,
                                 Principal principal,
                                 Model model) {
        Optional<Vote> vote = voteService.findForId(id);
        if (vote.isPresent()) {
            Optional<User> user = userService.findByUsername(principal.getName());
            if (user.isPresent()) {
                Item turn = new Item();
                turn.setUser(user.get());
                turn.setVote(vote.get());
                model.addAttribute("turn", turn);
                return "/turnForm";
            } else {
                return "/error";
            }
        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/editItem/{id}", method = RequestMethod.GET)
    public String editItem(@PathVariable Long id,
                           Model model) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            model.addAttribute("item", item);
            return "/item";
        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    public String editVoteWithId(@Valid Item item,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/turnForm";

        } else {
            itemService.save(item);
            return "redirect:/vote/" + item.getVote().getId();
        }
    }

}
