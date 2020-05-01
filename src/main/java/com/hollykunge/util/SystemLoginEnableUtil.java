package com.hollykunge.util;

import com.hollykunge.config.SysLoginEnableConfig;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.User;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
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
            //使用jvm缓存用户，减少数据库压力
            String pid = ParsingDnnameHeaderUtil.getDnname(request,sysLoginEnableConfig.getLoginUserName());
            Object cacheUser = LocalCache.get(pid);
            User user;
            if(Objects.isNull(cacheUser)){
                Optional<User> userData = userService.findByUsername(pid);
                if(!userData.isPresent()){
                    throw new BaseException("投票系统不存在你..无权访问");
                }
                user = userData.get();
                LocalCache.put(pid,user);
            }
            user = (User) cacheUser;
            return user;
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
