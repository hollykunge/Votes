package com.hollykunge.vote.repository;

import com.hollykunge.vote.entity.VoteItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @program: Lark-Server
 * @description: 投票轮
 * @author: Mr.Do
 * @create: 2019-09-27 20:26
 */
public interface VoteItemsRepository extends JpaRepository<VoteItems, String>, JpaSpecificationExecutor<VoteItems> {
}
