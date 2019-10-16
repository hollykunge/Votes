package com.hollykunge.repository;

import com.hollykunge.model.Item;
import com.hollykunge.model.UserVoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: jpa仓库之被投票项目集接口
 * @since: Create in 10:51 2019/10/9
 */
public interface UserVoteItemRepository extends JpaRepository<UserVoteItem, Long> {
    List<UserVoteItem> findByIp(String ip);
    List<UserVoteItem> findByItemAndIp(Item item,String ip);
    @Query(value = "select count(1) from (SELECT distinct user_ip FROM user_vote_item group by item_id,user_ip having item_id = ?1)", nativeQuery = true)
    Long countIp(Long itemId);

    @Query(value = "SELECT count(1)  num,vote_item_id voteItemId " +
            " FROM USER_VOTE_ITEM " +
            " group by vote_item_id,item_id " +
            " having item_id = ?1 "+
            " order by num desc", nativeQuery = true)
    List<Object[]> agreeRule(Long itemId);

    @Query(value = "SELECT sum(u.score) sco,vote_item_id voteItemId " +
            "            FROM USER_VOTE_ITEM u " +
            "            group by u.vote_item_id,u.item_id " +
            "            having item_id = ?1 " +
            "            order by sco desc", nativeQuery = true)
    List<Object[]> scoreRule(Long itemId);
}
