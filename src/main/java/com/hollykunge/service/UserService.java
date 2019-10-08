package com.hollykunge.service;

import com.hollykunge.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User save(User user);
}
