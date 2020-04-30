package com.hollykunge.util;

import com.hollykunge.config.SysLoginEnableConfig;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.User;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author: zhhongyu
 * @description: 系统登录
 * @since: Create in 14:14 2020/4/3
 */
@Component
public class SystemLoginEnableUtil {
    @Autowired
    private SysLoginEnableConfig sysLoginEnableConfig;
    @Autowired
    private UserService userService;

    public boolean isNeedLogin(){
        return sysLoginEnableConfig.isLoginEnable();
    }

    public boolean isIntranet(){
        return sysLoginEnableConfig.isIntranet();
    }

    public User getDefaltUser(HttpServletRequest request){
        /**
         * 判断是否为内网环境
         */
        if(isIntranet()){
            String pid = ParsingDnnameHeaderUtil.getDnname(request,sysLoginEnableConfig.getLoginUserName());
            Optional<User> user = userService.findByUsername(pid);
            if(!user.isPresent()){
                throw new BaseException("投票系统不存在你..无权访问");
            }
            return user.get();
        }
        User user = getDefaltUser();
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
