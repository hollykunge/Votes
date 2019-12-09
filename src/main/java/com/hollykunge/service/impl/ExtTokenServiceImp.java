package com.hollykunge.service.impl;

import com.hollykunge.model.ExtToken;
import com.hollykunge.repository.ExtTokenRepository;
import com.hollykunge.service.ExtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:39 2019/10/22
 */
@Slf4j
@Service
public class ExtTokenServiceImp implements ExtTokenService {
    private final ExtTokenRepository extTokenRepository;

    @Autowired
    public ExtTokenServiceImp(ExtTokenRepository extTokenRepository) {
        this.extTokenRepository = extTokenRepository;
    }

    @Override
    public String getToken(String clentIp,String interfaceAddress) throws Exception {
        String token = "vote_token_" + clentIp + System.currentTimeMillis();
        ExtToken extToken = new ExtToken();
        extToken.setToken(token);
        extToken.setIp(clentIp);
        extToken.setInterfaceAddress(interfaceAddress);
        if (!StringUtils.isEmpty(clentIp)&&!StringUtils.isEmpty(interfaceAddress)) {
            List<ExtToken> byToken = extTokenRepository.findByIpAndInterfaceAddress(clentIp,interfaceAddress);
            if (byToken.size() > 0) {
                return byToken.get(0).getToken();
            }
        }
        extTokenRepository.saveAndFlush(extToken);
        return token;
    }

    @Override
    public List<ExtToken> findToken(String token) {
        List<ExtToken> listTokens = extTokenRepository.findByToken(token);
        return listTokens;
    }
    @Override
    public boolean deleteToken(List<ExtToken> tokenList){
        //获取成功后删除key
        tokenList.stream().forEach(extToken -> {
            extTokenRepository.delete(extToken.getId());
        });
        return true;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void testTasks() {
        Date end = new Date();
        Date start = getDate(end);
        List<ExtToken> byCreateDateBetween = extTokenRepository.findByCreateDateBefore(start);
        byCreateDateBetween.forEach(extToken -> {
            extTokenRepository.delete(extToken);
            log.info("ip地址中的{},没用token已经删除..",extToken.getIp());
        });
    }

    private Date getDate(Date start){
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        return calendar.getTime();
    }

}
