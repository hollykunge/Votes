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
        String token = "vote_token_" + clentIp+ "_" + interfaceAddress + "_" + System.currentTimeMillis();
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
        String cacheToken = (String) LocalCache.get(token);
        log.info("获取幂等性token为：{}",cacheToken);
        return cacheToken;
    }
    @Override
    public boolean removeCache(String token){
        if(LocalCache.checkCacheName(token)){
            LocalCache.remove(token);
            log.info("幂等性的token已被清除，token为：{}",token);
        }
        return true;
    }

    @Override
    public boolean removeFailCache(String token) {
        if(LocalCache.checkFailCacheName(token)){
            LocalCache.removeFailCache(token);
        }
        return true;
    }

    @Override
    public String getFailCacheToken(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return (String) LocalCache.getFailCache(token);
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void testTasks() {
        //清除缓存，防止死锁
        LocalCache.getCacheMap().forEach((key,value) ->{
            LocalCache.checkCacheName(key);
        });
    }
    @Scheduled(cron = "0 0/5 * * * ?")
    public void failCachetask(){
        //清除失效缓存
        LocalCache.getFailCacheMap().forEach((key,value) ->{
            LocalCache.checkFailCacheName(key);
        });
    }

}
