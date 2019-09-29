package com.hollykunge.vote.repository;

import com.hollykunge.vote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: Lark-Server
 * @description: 用户数据访问
 * @author: Mr.Do
 * @create: 2019-09-26 14:52
 */
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String name);
    User findById(long id);
    void deleteById(Long id);
}
