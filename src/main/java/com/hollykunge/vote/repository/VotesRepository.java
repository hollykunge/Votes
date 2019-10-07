package com.hollykunge.vote.repository;

import com.hollykunge.vote.entity.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @program: Lark-Server
 * @description: 投票数据访问
 * @author: Mr.Do
 * @create: 2019-09-24 15:23
 */
public interface VotesRepository extends JpaRepository<Votes, String>, JpaSpecificationExecutor<Votes>, CrudRepository<Votes, String> {
}
