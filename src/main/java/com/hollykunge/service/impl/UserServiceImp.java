package com.hollykunge.service.impl;

import com.hollykunge.model.User;
import com.hollykunge.repository.RoleRepository;
import com.hollykunge.repository.UserRepository;
import com.hollykunge.service.UserService;
import com.hollykunge.util.SystemLoginEnableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String USER_ROLE = "ROLE_USER";
    @Autowired
    private SystemLoginEnableUtil systemLoginEnableUtil;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        //不需要登录
        if(!systemLoginEnableUtil.isNeedLogin() && !systemLoginEnableUtil.isIntranet()){
            Optional result = Optional.of(systemLoginEnableUtil.getDefaltUser(request));
            return result;
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        // Encode plaintext password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        // Set Role to ROLE_USER

        user.setRoles(Collections.singletonList(roleRepository.findByRole(USER_ROLE)));
        return userRepository.saveAndFlush(user);
    }
}
