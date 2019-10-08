package com.hollykunge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * @author lark
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal) {

        if (principal != null) {
            return "redirect:/home";
        }
        return "/login";
    }

}
