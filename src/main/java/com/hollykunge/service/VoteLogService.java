package com.hollykunge.service;

import com.hollykunge.model.User;
import com.hollykunge.model.Vote;

/**
 * @program: Lark-Server
 * @description: rizhi
 * @author: Mr.Do
 * @create: 2020-04-28 13:55
 */
public interface VoteLogService {
    Page<Vote> findByUserOrderedByDatePageable(User user, int page);
}
