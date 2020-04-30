package com.hollykunge.controller;

import com.hollykunge.util.SystemLoginEnableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhhongyu
 * @description: 基类controller
 * @since: Create in 21:30 2020/4/3
 */
public class BaseController {
    @Autowired
    protected SystemLoginEnableUtil systemLoginEnableUtil;
    @Autowired
    protected HttpServletRequest request;
    @ModelAttribute
    public void nologinModel(Model model) {
        /**
         * 用来断定页面显示的内容要根据username进行显示
         */
        model.addAttribute("loginSys",systemLoginEnableUtil.isNeedLogin());
        /**
         * 页面过滤数据用的username
         */
        model.addAttribute("username",systemLoginEnableUtil.getDefaltUser(request).getUsername());
    }
}
