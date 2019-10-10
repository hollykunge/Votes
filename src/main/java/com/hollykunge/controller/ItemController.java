package com.hollykunge.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.hollykunge.config.ItemDownloadData;
import com.hollykunge.config.ItemUploadData;
import com.hollykunge.config.UploadDataListener;
import com.hollykunge.model.Item;
import com.hollykunge.model.Vote;
import com.hollykunge.model.User;
import com.hollykunge.model.VoteItem;
import com.hollykunge.service.ItemService;
import com.hollykunge.service.VoteItemService;
import com.hollykunge.service.VoteService;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lark
 */
@Controller
public class ItemController {

    private final VoteService voteService;
    private final UserService userService;
    private final ItemService itemService;
    private final VoteItemService voteItemService;

    @Autowired
    public ItemController(VoteService voteService, UserService userService, ItemService itemService, VoteItemService voteItemService) {
        this.voteService = voteService;
        this.userService = userService;
        this.itemService = itemService;
        this.voteItemService = voteItemService;
    }

    /**
     * 创建投票轮
     *
     * @param item
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/createTurn", method = RequestMethod.POST)
    public String createNewVote(@Valid Item item,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/turnForm";

        } else {
            Long voteId = item.getVote().getId();
            List<Item> items = itemService.findItemsByVote(item.getVote());

//            List<Item> itemsTemp = new ArrayList<>();
//
//            for (Item item1 : items) {
//                if (voteId.equals(item1.getVote().getId())) {
//                    itemsTemp.add(item1);
//                }
//            }

            item.setTurnNum(items.size() + 1);
            itemService.save(item);
            return "redirect:/vote/" + voteId;
        }
    }

    /**
     * 获取投票轮编辑页
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
                Item item = new Item();
                item.setUser(user.get());
                item.setVote(vote.get());
                model.addAttribute("item", item);
                return "/turnForm";
            } else {
                return "/error";
            }
        } else {
            return "/error";
        }
    }

    /**
     * 获取投票项页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/editItem/{id}", method = RequestMethod.GET)
    public String editItem(@PathVariable Long id,
                           Model model) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            Optional<List<VoteItem>> voteItems = voteItemService.findByVoteId(item.get().getVote());
            model.addAttribute("item", item.get());
            model.addAttribute("vote", item.get().getVote());
            model.addAttribute("voteItems", voteItems);
            return "/item";
        } else {
            return "/error";
        }
    }

    /**
     * 投票轮结果统计
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/stat/{id}", method = RequestMethod.GET)
    public String statItem(@PathVariable Long id,
                           Model model) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            model.addAttribute("item", item.get());
            return "/stat";
        } else {
            return "/error";
        }
    }

    /**
     * 投票项编辑保存
     *
     * @param item
     * @param bindingResult
     * @return
     */
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


    @RequestMapping(value = "/item/import", method = RequestMethod.POST)
    public String excelImport(MultipartFile file, HttpServletRequest request) throws IOException {
        try {
            String voteIdTemp = request.getHeader("voteId");
            if (StringUtils.isEmpty(voteIdTemp)) {
                throw new RuntimeException("vote不能为空...");
            }
            Vote vote = new Vote();
            vote.setId(Long.parseLong(voteIdTemp));
            EasyExcel.read(file.getInputStream(), ItemUploadData.class, new UploadDataListener(vote, voteItemService)).sheet().doRead();
            return "redirect:/editItem/" + voteIdTemp;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping(value = "/item/export")
    @ResponseBody
    public String export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=exportFile.xlsx");
        EasyExcel.write(response.getOutputStream(), ItemDownloadData.class).sheet("模板").doWrite(null);
        return "success";
    }

    /**
     * 获取邀请码
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public String editVoteWithId(@PathVariable Long id,
                                 Model model) {
        Optional<Item> item = itemService.findById(id);
        if (item.isPresent()) {
            model.addAttribute("code", item.get());
            return "/stat";
        } else {
            return "/error";
        }
    }
}
