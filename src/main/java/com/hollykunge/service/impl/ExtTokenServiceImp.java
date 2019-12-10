package com.hollykunge.service.impl;

import com.hollykunge.service.ExtTokenService;
import com.hollykunge.util.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:39 2019/10/22
 */
@Slf4j
@Service
public class ExtTokenServiceImp implements ExtTokenService {

    @Override
    public String getToken(String clentIp,String interfaceAddress) throws Exception {
        String token = "vote_token_" + clentIp+ "_" + interfaceAddress + System.currentTimeMillis();
        if (!StringUtils.isEmpty(clentIp)&&!StringUtils.isEmpty(interfaceAddress)) {
            Object tk = LocalCache.get(token);
            if(tk != null){
                return (String) tk;
            }
        }
        LocalCache.put(token,token);
        return token;
    }
    @Override
    public String getCaCheToken(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return (String) LocalCache.get(token);
    }
    @Override
    public boolean removeCache(String token){
        if(LocalCache.checkCacheName(token)){
            LocalCache.remove(token);
        }
        return true;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void testTasks() {
        LocalCache.getCacheMap().forEach((key,value) ->{
            LocalCache.checkCacheName(key);
        });
    }

}
