package com.hollykunge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/auth/{username}")
    public String auth(Model model, @PathVariable String username, HttpServletResponse response){
        model.addAttribute("username", username);
        model.addAttribute("password", "123456");
        return "/login";
    }
}
