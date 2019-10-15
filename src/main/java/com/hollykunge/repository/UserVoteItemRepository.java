package com.hollykunge.repository;

import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteIp;
import com.hollykunge.model.UserVoteItem;
import com.hollykunge.model.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author: zhhongyu
 * @description: jpa仓库之被投票项目集接口
 * @since: Create in 10:51 2019/10/9
 */
public interface UserVoteItemRepository extends JpaRepository<UserVoteItem, Long> {
    List<UserVoteItem> findByUserVoteIp(UserVoteIp userVoteIp);
}
