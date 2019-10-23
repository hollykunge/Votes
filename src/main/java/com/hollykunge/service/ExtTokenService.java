package com.hollykunge.service;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:38 2019/10/22
 */
public interface ExtTokenService {
    String getToken(String clentIp,String interfaceAddress) throws Exception;

    boolean findToken(String token);
}
