package com.hollykunge.repository;

import com.hollykunge.model.ExtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:35 2019/10/22
 */
public interface ExtTokenRepository extends JpaRepository<ExtToken,Long> {
    List<ExtToken> findByToken(String token);
    List<ExtToken> findByIp(String ip);
    List<ExtToken> findByCreateDateBetween(Date start,Date end);
}
