package com.hollykunge.controller;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import com.hollykunge.service.VoteService;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Optional;

/**
 * @author lark
 */
@Controller
public class TurnController {
    private final String LINK = ":";

    private final VoteService voteService;
    private final UserService userService;

    @Autowired
    public TurnController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }

    @RequestMapping(value = "/newVote", method = RequestMethod.GET)
    public String newVote(Principal principal,
                          Model model) {

        Optional<User> user = userService.findByUsername(principal.getName());

        if (user.isPresent()) {
            Vote vote = new Vote();
            vote.setUser(user.get());

            model.addAttribute("vote", vote);

            return "/voteForm";

        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/saveVote", method = RequestMethod.POST)
    public String createNewVote(@Valid Vote vote,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/voteForm";
        } else {
            voteService.save(vote);
            return "redirect:/votes/" + vote.getUser().getUsername();
        }
    }

    @RequestMapping(value = "/editVote/{id}", method = RequestMethod.GET)
    public String editVoteWithId(@PathVariable Long id,
                                 Principal principal,
                                 Model model) {

        Optional<Vote> optionalVote = voteService.findForId(id);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();

            if (isPrincipalOwnerOfVote(principal, vote)) {
                model.addAttribute("vote", vote);
                return "/voteForm";
            } else {
                return "/403";
            }

        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/vote/{id}", method = RequestMethod.GET)
    public String getVoteWithId(@PathVariable Long id,
                                Principal principal,
                                Model model,
                                HttpServletRequest request) throws UnknownHostException {

        Optional<Vote> optionalVote = voteService.findForId(id);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();

            model.addAttribute("vote", vote);
            if (isPrincipalOwnerOfVote(principal, vote)) {
                model.addAttribute("username", principal.getName());
            }
            //进入邀请页面接口地址
            InetAddress address= InetAddress.getByName(request.getServerName());
            String hostAddress = address.getHostAddress()+LINK+request.getServerPort();
            model.addAttribute("address", VoteConstants.AGREEMENT_LETTER +hostAddress+VoteConstants.INVITECODE_RPC);
            return "/vote";

        } else {
            return "/error";
        }
    }

    @RequestMapping(value = "/vote/{id}", method = RequestMethod.DELETE)
    public String deleteVoteWithId(@PathVariable Long id,
                                   Principal principal) {

        Optional<Vote> optionalVote = voteService.findForId(id);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();

            if (isPrincipalOwnerOfVote(principal, vote)) {
                voteService.delete(vote);
                return "redirect:/home";
            } else {
                return "/403";
            }

        } else {
            return "/error";
        }
    }

    private boolean isPrincipalOwnerOfVote(Principal principal, Vote vote) {
        return principal != null && principal.getName().equals(vote.getUser().getUsername());
    }
}
