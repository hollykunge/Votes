package com.hollykunge.util;

import com.hollykunge.config.SysLoginEnableConfig;
import com.hollykunge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: zhhongyu
 * @description: 系统登录
 * @since: Create in 14:14 2020/4/3
 */
@Component
public class SystemLoginEnableUtil {
    @Autowired
    private SysLoginEnableConfig sysLoginEnableConfig;

    public boolean isNeedLogin(){
        return sysLoginEnableConfig.isLoginEnable();
    }

    public User getDefaltUser(){
        User user = new User();
        user.setId(sysLoginEnableConfig.getLoginUserId());
        user.setUsername(String.valueOf(sysLoginEnableConfig.getLoginUserName()));
        user.setActive(1);
        return user;
    }
}
