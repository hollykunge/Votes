package com.hollykunge.controller;

import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.util.Base64Utils;
import com.hollykunge.util.Pager;

import java.util.Optional;

/**
 * @program: Lark-Server
 * @description: 日志
 * @author: Mr.Do
 * @create: 2020-04-28 13:51
 */
@Controller
public class VoteLogController {
    @ControllerWebLog(name = "查询", intoDb = true)
    @RequestMapping(value = "/logs/{username}", method = RequestMethod.GET)
    public String getLogs(@PathVariable String username,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model,
                                  @ModelAttribute("redirect") String redirect) {

        Optional<User> optionalUser = userService.findByUsername(username);
        if (!StringUtils.isEmpty(redirect)) {
            model.addAttribute("showAlertMessage", Base64Utils.decryption(redirect));
        }
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Page<VoteLog> voteLogs = logService.findByUserOrderedByDatePageable(user, page);
            Pager pager = new Pager(voteLogs);

            model.addAttribute("pager", pager);
            model.addAttribute("user", user);

            return "/votes";

        } else {
            return "/error";
        }
    }
}
