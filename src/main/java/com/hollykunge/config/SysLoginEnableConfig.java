package com.hollykunge.config;

import com.hollykunge.dictionary.ApplicationSystemConfigEnums;
import com.hollykunge.dictionary.RequestHeaderEnums;
import com.hollykunge.exception.BaseException;
import com.hollykunge.model.User;
import com.hollykunge.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Objects;
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

    @Value("${system.login.enable}")
    private boolean loginEnable;
    private Long loginUserId;
    @Value("${system.login.username}")
    private String loginUserName;
    /**
     * 系统环境
     */
    @Value("${system.environment}")
    private String systemEnvironment;
    /**
     * 是否为内网环境
     */
    private boolean isIntranet = false;

    @PostConstruct
    public void setLoginUserId() {
        if(ApplicationSystemConfigEnums.getEnumByName(systemEnvironment) == null){
            throw new BaseException("配置文件中配置的system.environment错误，使用正确的系统环境");
        }
        /**
         * 使用登录时触发，使用登录后，直接使用系统自身的user表
         */
        if(loginEnable){
            return;
        }
        /**
         * 内网环境下，可使用dnname代替系统的userid
         */
        if(Objects.equals(ApplicationSystemConfigEnums.INTRANET.getName(),systemEnvironment)){
            intranetStrategy();
            return;
        }
        /**
         * 外网环境必须配置一个，系统内置的userid
         */
        extranetStrategy();
    }

    /**
     * 内网环境系统启动策略
     */
    private void intranetStrategy(){
        this.isIntranet = true;
        if(RequestHeaderEnums.getEnumByName(loginUserName) == null){
            throw new BaseException("配置一个正确的内网关请求头名称");
        }
    }

    /**
     * 外网环境系统启动策略
     */
    private void extranetStrategy(){
        Optional<User> byUsername = userRepository.findByUsername(this.loginUserName);
        if (!byUsername.isPresent()) {
            log.error("system.login.enable 注解使用错误，为true时，数据库必须内置一个配置的用户名...");
            throw new BaseException("system.login.enable 注解使用错误，为true时，数据库必须内置一个配置的用户名...");
        }
        this.loginUserId = byUsername.get().getId();
    }
}
