package com.hollykunge.config;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.User;
import com.hollykunge.repository.UserRepository;
import com.hollykunge.util.SystemLoginEnableUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * @author: zhhongyu
 * @description: 配置系统登录方式
 * @since: Create in 14:05 2020/4/3
 */
@Slf4j
@Order(1)
@Data
@Configuration
public class SysLoginEnableConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SystemLoginEnableUtil systemLoginEnableUtil;

    @Value("${system.login.enable}")
    private boolean loginEnable;
    private Long loginUserId;
    @Value("${system.login.username}")
    private String loginUserName;

    @PostConstruct
    public void setLoginUserId() {
        if(systemLoginEnableUtil.isNeedLogin()){
            return;
        }
        Optional<User> byUsername = userRepository.findByUsername(this.loginUserName);
        if (!byUsername.isPresent()) {
            log.error("system.login.enable 注解使用错误，为true时，数据库必须内置一个配置的用户名...");
            throw new BaseException("system.login.enable 注解使用错误，为true时，数据库必须内置一个配置的用户名...");
        }
        this.loginUserId = byUsername.get().getId();
    }
}
