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

    public List<User> getUserList();

    public User findUserById(long id);

    public void add(User user);

    public void update(User user);

    public void delete(long id);

    public User findByUsername(String username);

}
