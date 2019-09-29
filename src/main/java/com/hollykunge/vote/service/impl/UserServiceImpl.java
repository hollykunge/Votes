package com.hollykunge.vote.service.impl;

import com.hollykunge.vote.entity.User;

import com.hollykunge.vote.repository.UserRepository;
import com.hollykunge.vote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 实现
 * @author: Mr.Do
 * @create: 2019-09-26 15:11
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

}
