package com.hollykunge.service;

import com.hollykunge.model.ExtToken;

import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:38 2019/10/22
 */
public interface ExtTokenService {
    String getToken(String clentIp,String interfaceAddress) throws Exception;

    List<ExtToken> findToken(String token);

    boolean deleteToken(List<ExtToken> tokenList);
}
