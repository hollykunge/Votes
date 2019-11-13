package com.hollykunge.controller.rpc;

import com.hollykunge.exception.BaseException;
import com.hollykunge.model.User;
import com.hollykunge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * 远程服务调用登录
 */
@Controller
@RequestMapping("api")
public class RpcLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 提供登录rpc接口
     * @return
     * @throws Exception
     */
    @PostMapping("login")
    public String login(@RequestParam String pid) throws Exception{
        String username = pid;
        if(StringUtils.isEmpty(username)){
            throw new BaseException("身份证号不能为空...");
        }
        //查询用户是否注册
        Optional<User> byUsername = userService.findByUsername(username);
        boolean isExit = byUsername.isPresent();
        if(!isExit){
            //如果没有该用户，注册一个role为user的用户
            User registrator = new User();
            registrator.setUsername(username);
            registrator.setPassword("123456");
            userService.save(registrator);
        }
        return "forward:/login";
    }

}
