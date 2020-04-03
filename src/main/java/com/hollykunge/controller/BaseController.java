package com.hollykunge.controller;

import com.hollykunge.util.SystemLoginEnableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 21:30 2020/4/3
 */
public class BaseController {
    @Autowired
    private SystemLoginEnableUtil systemLoginEnableUtil;
    @ModelAttribute
    public void nologinModel(Model model) {
        model.addAttribute("loginSys",systemLoginEnableUtil.isNeedLogin());
        model.addAttribute("username",systemLoginEnableUtil.getDefaltUser().getUsername());
    }
}
