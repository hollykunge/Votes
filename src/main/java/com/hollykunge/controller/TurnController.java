package com.hollykunge.controller;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import com.hollykunge.service.*;
import com.hollykunge.util.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lark
 */
@Controller
public class TurnController extends BaseController{
    private final String LINK = ":";

    private final VoteService voteService;
    private final UserService userService;

    @Autowired
    public TurnController(VoteService voteService, UserService userService) {
        this.voteService = voteService;
        this.userService = userService;
    }
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserVoteItemService userVoteItemService;
    @Autowired
    private VoteItemService voteItemService;

    @RequestMapping(value = "/newVote", method = RequestMethod.GET)
    public String newVote(Principal principal,
                          Model model) {
        User user;
        //登录状态
        if(principal != null){
            String userName = principal.getName();
            user = userService.findByUsername(userName).get();
        }else{
            //无登录状态
            user = systemLoginEnableUtil.getDefaltUser(request);
        }
        Vote vote = new Vote();
        vote.setUser(user);
        model.addAttribute("vote", vote);
        return "/voteForm";
    }

    @RequestMapping(value = "/saveVote", method = RequestMethod.POST)
    public String createNewVote(@Valid Vote vote,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/voteForm";
        } else {
            //默认增加投票为新建
            vote.setStatus(VoteConstants.VOTE_ADD_STATUS);
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
                                HttpServletRequest request,
                                @ModelAttribute("redirect") String redirect) throws UnknownHostException {

        Optional<Vote> optionalVote = voteService.findForId(id);

        if(!StringUtils.isEmpty(redirect)){
            model.addAttribute("showAlertMessage", Base64Utils.decryption(redirect));
        }
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
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) throws Exception {

        Optional<Vote> optionalVote = voteService.findForId(id);

        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();
            //当为结束状态时进行删除投票，将关联的一大堆东西全部都删除了
            if(Objects.equals(vote.getStatus(),VoteConstants.VOTE_FINAL_STATUS)){
                List<Item> itemsByVote = itemService.findItemsByVote(vote);
                for(Item item : itemsByVote){
                    userVoteItemService.deleteByItem(item);
                    voteItemService.deleteByItem(item);
                }
                itemService.deleteByVote(vote);
                voteService.delete(vote);
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("删除成功"));
                return "redirect:/votes/"+ vote.getUser().getUsername();
            }
            List<Item> itemsByVote = itemService.findItemsByVote(vote);
            if(itemsByVote.size() > 0){
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("未结束投票或者包含投票项不能进行删除操作"));
                return "redirect:/votes/"+ vote.getUser().getUsername();
            }
            if (isPrincipalOwnerOfVote(principal, vote)) {
                voteService.delete(vote);
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("删除成功"));
                return "redirect:/votes/"+ vote.getUser().getUsername();
            } else {
                return "/403";
            }

        } else {
            return "/error";
        }
    }

    /**
     * 设置投票状态接口
     *
     * @param id
     * @param status
     * @param principal
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/vote/setstatus/{id}/{status}", method = RequestMethod.GET)
    public String setVoteStatus(@PathVariable Long id,
                                @PathVariable String status,
                                Principal principal,
                                RedirectAttributes redirectAttributes) throws Exception {
        //结束投票时增加校验
        if (Objects.equals(status, VoteConstants.VOTE_FINAL_STATUS)) {
            Vote voteTemp = new Vote();
            voteTemp.setId(id);
            List<Item> itemsByVote = itemService.findItemsByVote(voteTemp);
            if(itemsByVote == null || itemsByVote.size() == 0){
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("不能结束,有未结束的投票轮..."));
                return "redirect:/vote/" + voteTemp.getId();
            }
            boolean noFinal = itemsByVote.stream().anyMatch(item -> Objects.equals(VoteConstants.ITEM_ADD_STATUS, item.getStatus()) ||
                    Objects.equals(VoteConstants.ITEM_SEND_STATUS, item.getStatus()));
            if (noFinal) {
                redirectAttributes.addAttribute("redirect", Base64Utils.encrypt("不能结束,有未结束的投票轮..."));
                return "redirect:/vote/" + voteTemp.getId();
            }
        }
        Vote vote = new Vote();
        vote.setId(id);
        vote.setStatus(status);
        voteService.updateById(vote);
        return "redirect:/vote/" + vote.getId();
    }

    private boolean isPrincipalOwnerOfVote(Principal principal, Vote vote) {
        return principal != null && principal.getName().equals(vote.getUser().getUsername());
    }
}
