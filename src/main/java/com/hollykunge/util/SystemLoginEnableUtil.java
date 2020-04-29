package com.hollykunge.util;

import com.hollykunge.config.SysLoginEnableConfig;
import com.hollykunge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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

    public User getDefaltUser(HttpServletRequest request){
        User user = getDefaltUser();
        /**
         * 判断是否为内网环境
         */
        if(sysLoginEnableConfig.isIntranet()){
            String dnname = request.getHeader(sysLoginEnableConfig.getLoginUserName());
            String pid = ParsingDnnameHeaderUtil.parsing(dnname);
            user.setUsername(pid);
        }
        return user;
    }
    private User getDefaltUser(){
        User user = new User();
        user.setUsername(String.valueOf(sysLoginEnableConfig.getLoginUserName()));
        user.setId(sysLoginEnableConfig.getLoginUserId());
        user.setActive(1);
        return user;
    }
}
