package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.User;

import java.util.List;

/**
 * @program: Lark-Server
 * @description: 用户
 * @author: Mr.Do
 * @create: 2019-09-26 14:58
 */
public interface UserService {

    List<User> getUserList();

    User findUserById(long id);

    void add(User user);

    void update(User user);

    void delete(long id);

    User findByUsername(String username);

}
