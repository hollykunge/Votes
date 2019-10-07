package com.hollykunge.vote.controller;

import com.hollykunge.vote.anotation.OpsLog;
import com.hollykunge.vote.entity.User;
import com.hollykunge.vote.entity.VoteItems;
import com.hollykunge.vote.entity.Votes;
import com.hollykunge.vote.service.UserService;
import com.hollykunge.vote.service.VoteItemsService;
import com.hollykunge.vote.service.VotesService;
import com.hollykunge.vote.utils.BaseException;
import com.hollykunge.vote.utils.PageBaseInfo;
import com.hollykunge.vote.utils.ResultBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

import java.util.List;

import static com.hollykunge.vote.constant.Constant.PARAMS_ERROR;
import static com.hollykunge.vote.constant.Constant.USER_ERROR;

/**
 * @program: Lark-Server
 * @description: 投票管理，包括投票列表的增删改查
 * @author: Mr.Do
 * @create: 2019-09-20 23:27
 */
@Controller
@RequestMapping("/manage")
public class VoteManageController {

    @Resource
    UserService userService;

    @Resource
    VotesService votesService;

    @Resource
    VoteItemsService voteItemsService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录
     *
     * @param model
     * @param username
     * @param password
     * @return
     */
    @OpsLog("管理员登录")
    @PostMapping("/list")
    public String login(Model model,
                              @RequestParam(value = "username", required = true, defaultValue = "admin") String username,
                              @RequestParam(value = "password", required = true, defaultValue = "123456") String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new BaseException(USER_ERROR);
        }
        model.addAttribute("user", user);
        return "list";
    }

    /**
     * 投票列表分页
     *
     * @param mv
     * @param id 用户id
     * @return
     */
    @OpsLog("管理员查看投票列表")
    @GetMapping("/list")
    public ModelAndView pageFront(ModelAndView mv) {
        List<Votes> votes = votesService.findVotesByUserId();
        mv.setViewName("voteManageList");
        mv.addObject("res", ResultBody.success(votes));
        return mv;
    }

    /**
     * 投票列表分页
     *
     * @param mv
     * @param votes
     * @param pageNum
     * @param pageSize
     * @return
     */
    @OpsLog("管理员查看投票列表")
    @GetMapping("/page")
    public ModelAndView page(ModelAndView mv,
                             @RequestBody Votes votes,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "20") int pageSize) {
        PageBaseInfo<Votes> pageBaseInfo = votesService.findVoteCriteria(pageNum, pageSize, votes);
        mv.setViewName("voteManageList");
        mv.addObject("res", ResultBody.success(pageBaseInfo));
        return mv;
    }
    /**
     * 添加页面
     * @return
     */
    @OpsLog("管理员添加一项投票")
    @GetMapping("/add")
    public String addPage() {
        return "add";
    }
    /**
     * 添加投票项
     *
     * @param model
     * @return
     */
    @OpsLog("管理员添加一项投票")
    @PostMapping("/")
    public String add(Model model, @RequestBody Votes votes) {
        if (votes == null) {
            throw new BaseException(PARAMS_ERROR);
        }
        votesService.add(votes);
        model.addAttribute("res", ResultBody.success());
        return "list";
    }

    /**
     * 修改投票项
     *
     * @param model
     * @param votes
     * @return
     */
    @OpsLog("管理员修改一项投票")
    @PutMapping("/")
    public String update(Model model, @RequestBody Votes votes) {
        if (votes == null) {
            throw new BaseException(PARAMS_ERROR);
        }
        votesService.add(votes);
        model.addAttribute("res", ResultBody.success());
        return "list";
    }

    /**
     * 删除投票项
     *
     * @param model
     * @param id
     * @return
     */
    @OpsLog("管理员删除一项投票")
    @DeleteMapping("/")
    public String delete(Model model, @RequestParam(value = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            throw new BaseException(PARAMS_ERROR);
        }
        votesService.delete(id);
        model.addAttribute("res", ResultBody.success());
        return "list";
    }

    /**
     * 投票项统计，几轮
     *
     * @param model
     * @param id
     * @return
     */
    @OpsLog("管理员查看一项投票统计情况")
    @GetMapping("/statistics")
    public String statistics(Model model, @RequestParam(value = "id", required = true) String id,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "20") int pageSize) {
        if (StringUtils.isEmpty(id)) {
            throw new BaseException(PARAMS_ERROR);
        }
        PageBaseInfo<VoteItems> pageBaseInfo = voteItemsService.findVoteItems(pageNum, pageSize);
        model.addAttribute("res", ResultBody.success(pageBaseInfo));
        return "manage/statistics";
    }

    /**
     * 投票开始
     *
     * @param model
     * @param id
     * @return
     */
    @OpsLog("管理员开始一项投票")
    @GetMapping("/start")
    public String start(Model model, @RequestParam(value = "id", required = true) String id) {
        model.addAttribute("id", id);
        return "list";
    }

    /**
     * 投票结束
     *
     * @param model
     * @param id
     * @return
     */
    @OpsLog("管理员结束一项投票")
    @GetMapping("/end")
    public String end(Model model, @RequestParam(value = "id", required = true) String id) {
        model.addAttribute("id", id);
        return "list";
    }

    /**
     * 生成邀请码，针对一轮投票
     *
     * @param model
     * @param id
     * @return
     */
    @OpsLog("管理员生成一项投票邀请码")
    @GetMapping("/invitation")
    public String invitation(Model model, @RequestParam(value = "id", required = true) String id) {
        return "list";
    }
}
